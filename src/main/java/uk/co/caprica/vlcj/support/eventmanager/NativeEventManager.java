/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.support.eventmanager;

import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.internal.libvlc_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_event_attach;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_event_detach;

/**
 * Common implementation for a component that deals with a native LibVlc event manager.
 * <p>
 * Native events will only be delivered if there is at least one event listener.
 *
 * @param <E> type of the event object
 * @param <L> type of the event listener
 */
abstract public class NativeEventManager<E,L> {

    /**
     * Native library instance.
     */
    private final libvlc_instance_t libvlcInstance;

    /**
     * Component that generates events.
     */
    private final E eventObject;

    /**
     * First event in the range of native events to register.
     */
    private final libvlc_event_e firstEvent;

    /**
     * Last even in the range of native events to register.
     */
    private final libvlc_event_e lastEvent;

    /**
     * Name to use for the callback thread initialiser.
     */
    private final String callbackName;

    /**
     * Collection of registered event listeners.
     * <p>
     * A copy-on-write collection is used to defend against listeners being added/removed whilst the native thread is
     * dispatching an event to already registered listeners.
     */
    private final List<L> eventListenerList = new CopyOnWriteArrayList<L>();

    /**
     * Native event callback.
     */
    private EventCallback callback;

    /**
     * Flag tracking if the native event manager callback is active or not.
     * <p>
     * It is only registered if there is at least one active event listener, and care must be taken not to de-register
     * more than once.
     */
    private boolean callbackRegistered;

    /**
     * Create a new component to manage native events.
     *
     * @param libvlcInstance native library instance, this is allowed to be <code>null</code> for some implementations
     * @param eventObject component that generates events
     * @param firstEvent first event in the range of events to register
     * @param lastEvent last event in the range of events to register
     * @param callbackName name to use for the callback thread initialiser
     */
    protected NativeEventManager(libvlc_instance_t libvlcInstance, E eventObject, libvlc_event_e firstEvent, libvlc_event_e lastEvent, String callbackName) {
        this.libvlcInstance = libvlcInstance;
        this.eventObject = eventObject;
        this.firstEvent = firstEvent;
        this.lastEvent = lastEvent;
        this.callbackName = callbackName;
    }

    /**
     * Add a component to be notified of events.
     *
     * @param listener component to notify
     */
    public final void addEventListener(L listener) {
        if (listener != null) {
            eventListenerList.add(listener);
            addNativeEventListener();
        } else {
            throw new IllegalArgumentException("Listener must not be null");
        }
    }

    /**
     * Remove a component that was previously interested in notifications of events.
     *
     * @param listener component to stop notifying
     */
    public final void removeEventListener(L listener) {
        eventListenerList.remove(listener);
        removeNativeEventListener();
    }

    /**
     * Register a call-back to receive native events.
     */
    private void addNativeEventListener() {
        if (!callbackRegistered && !eventListenerList.isEmpty()) {
            callbackRegistered = true;
            callback = new EventCallback();
            libvlc_event_manager_t mediaEventManager = onGetEventManager(eventObject);
            for (libvlc_event_e event : libvlc_event_e.values()) {
                if (event.intValue() >= firstEvent.intValue() && event.intValue() <= lastEvent.intValue()) {
                    libvlc_event_attach(mediaEventManager, event.intValue(), callback, null);
                }
            }
        }
    }

    /**
     * De-register the call-back used to receive native events.
     */
    private void removeNativeEventListener() {
        if (callbackRegistered && eventListenerList.isEmpty()) {
            callbackRegistered = false;
            libvlc_event_manager_t mediaEventManager = onGetEventManager(eventObject);
            for (libvlc_event_e event : libvlc_event_e.values()) {
                if (event.intValue() >= firstEvent.intValue() && event.intValue() <= lastEvent.intValue()) {
                    libvlc_event_detach(mediaEventManager, event.intValue(), callback, null);
                }
            }
            callback = null;
        }
    }

    /**
     * Raise a new event (dispatch it to listeners).
     * <p>
     * Events are processed on the <em>native</em> callback thread, so must execute quickly and certainly must never
     * block.
     * <p>
     * It is also generally <em>forbidden</em> for an event handler to call back into LibVLC.
     *
     * @param event event to raise, may be <code>null</code> and if so will be ignored
     */
    public final void raiseEvent(EventNotification<L> event) {
        if (event != null && !eventListenerList.isEmpty()) {
            for (L listener : eventListenerList) {
                 event.notify(listener);
            }
        }
    }

    /**
     * Release this component.
     */
    public final void release() {
        eventListenerList.clear();
        removeNativeEventListener();
    }

    /**
     * Native event callback.
     */
    private class EventCallback implements libvlc_callback_t {

        private final CallbackThreadInitializer cti;

        private EventCallback() {
            this.cti = new CallbackThreadInitializer(true, false, callbackName);
            Native.setCallbackThreadInitializer(this, cti);
        }

        @Override
        public void callback(libvlc_event_t event, Pointer userData) {
            raiseEvent(onCreateEvent(libvlcInstance, event, eventObject));
        }

    }

    /**
     * Get the native event manager.
     *
     * @param eventObject component for which to get the event manager (it must provide access to a native resource)
     * @return native event manager, never <code>null</code>
     */
    protected abstract libvlc_event_manager_t onGetEventManager(E eventObject);

    /**
     * Create an event.
     *
     * @param libvlcInstance native library instance
     * @param event native event
     * @param eventObject component that generated the event
     * @return an event, of a type that can intrinsically notify a listener
     */
    protected abstract EventNotification<L> onCreateEvent(libvlc_instance_t libvlcInstance, libvlc_event_t event, E eventObject);

}
