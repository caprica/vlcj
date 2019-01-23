package uk.co.caprica.vlcj.eventmanager;

import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
     * Native library.
     */
    private final LibVlc libvlc;

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
     * Native event callback.
     */
    private final EventCallback callback = new EventCallback();

    /**
     * Collection of registered event listeners.
     * <p>
     * A copy-on-write collection is used to defend against listeners being added/removed whilst the native thread is
     * dispatching an event to already registered listeners.
     */
    private final List<L> eventListenerList = new CopyOnWriteArrayList<L>();

    /**
     * Create a new component to manage native events.
     *
     * @param libvlc native library
     * @param eventObject component that generates events
     * @param firstEvent first event in the range of events to register
     * @param lastEvent last event in the range of events to register
     * @param callbackName name to use for the callback thread initialiser
     */
    NativeEventManager(LibVlc libvlc, E eventObject, libvlc_event_e firstEvent, libvlc_event_e lastEvent, String callbackName) {
        this.libvlc = libvlc;
        this.eventObject = eventObject;
        this.firstEvent = firstEvent;
        this.lastEvent = lastEvent;
        this.callbackName = callbackName;
    }

    /**
     * Add a component to be notified of media events.
     *
     * @param listener component to notify
     */
    public final void addEventListener(L listener) {
        eventListenerList.add(listener);
        addNativeEventListener();
    }

    /**
     * Remove a component that was previously interested in notifications of media events.
     *
     * @param listener component to stop notifying
     */
    public final void removeEventListener(L listener) {
        eventListenerList.remove(listener);
        removeNativeEventListener();
    }

    /**
     * Register a call-back to receive media native events.
     */
    private void addNativeEventListener() {
        // If the new listener list size is exactly one, register for native callbacks (we only want to register once
        // even if more listeners are subsequently added)
        if (eventListenerList.size() == 1) {
            libvlc_event_manager_t mediaEventManager = onGetEventManager(libvlc, eventObject);
            for (libvlc_event_e event : libvlc_event_e.values()) {
                if (event.intValue() >= firstEvent.intValue() && event.intValue() <= lastEvent.intValue()) {
                    libvlc.libvlc_event_attach(mediaEventManager, event.intValue(), callback, null);
                }
            }
        }
    }

    /**
     * De-register the call-back used to receive native media events.
     */
    private void removeNativeEventListener() {
        // Stop delivering native events if there are no listeners
        if (eventListenerList.isEmpty()) {
            libvlc_event_manager_t mediaEventManager = onGetEventManager(libvlc, eventObject);
            for (libvlc_event_e event : libvlc_event_e.values()) {
                if (event.intValue() >= firstEvent.intValue() && event.intValue() <= lastEvent.intValue()) {
                    libvlc.libvlc_event_detach(mediaEventManager, event.intValue(), callback, null);
                }
            }
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
        removeNativeEventListener();
        eventListenerList.clear();
    }

    /**
     * Native event callback.
     */
    private class EventCallback implements libvlc_callback_t {

        private EventCallback() {
            Native.setCallbackThreadInitializer(this, new CallbackThreadInitializer(true, false, callbackName));
        }

        @Override
        public void callback(libvlc_event_t event, Pointer userData) {
            raiseEvent(onCreateEvent(libvlc, event, eventObject));
        }
    }

    /**
     * Get the native event manager.
     *
     * @param libvlc native library
     * @param eventObject component for which to get the event manager (it must provide access to a native resource)
     * @return
     */
    protected abstract libvlc_event_manager_t onGetEventManager(LibVlc libvlc, E eventObject);

    /**
     * Create an event.
     *
     * @param libvlc native library
     * @param event native event
     * @param eventObject component that generated the event
     * @return
     */
    protected abstract EventNotification onCreateEvent(LibVlc libvlc, libvlc_event_t event, E eventObject);

}
