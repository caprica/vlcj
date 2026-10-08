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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.support.callback;

import com.sun.jna.Structure;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.support.events.EventNotification;
import uk.co.caprica.vlcj.support.events.PostEventListener;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public abstract class NativeCallbackHandler<T, C extends Structure, L> {

    /**
     * Source of the event, e.g. a {@link MediaPlayer} or some other component.
     */
    protected final Supplier<T> sourceSupplier;

    /**
     * Native callback structure.
     */
    private final C cbs;

    /**
     * Collection of registered event listeners.
     * <p>
     * A copy-on-write collection is used to defend against listeners being added/removed whilst the native thread is
     * dispatching an event to already registered listeners.
     */
    private final List<L> eventListenerList = new CopyOnWriteArrayList<L>();

    /**
     * Collection of registered "post" event listeners.
     * <p>
     * These are special listeners that must run after all application listeners have finished.
     */
    private final List<L> postEventListenerList = new CopyOnWriteArrayList<L>();

    protected NativeCallbackHandler(Supplier<T> sourceSupplier) {
        this.sourceSupplier = sourceSupplier;
        this.cbs = initCallbacks();
    }

    protected NativeCallbackHandler(T source) {
        this(() -> source);
    }

    protected abstract C initCallbacks();

    protected final T source() {
        return sourceSupplier.get();
    }

    /**
     * Add a component to be notified of events.
     *
     * @param listener component to notify
     */
    public final void addEventListener(L listener) {
        Objects.requireNonNull(listener, "Listener must not be null");
        if (!listener.getClass().isAnnotationPresent(PostEventListener.class)) {
            eventListenerList.add(listener);
        } else {
            postEventListenerList.add(listener);
        }
    }

    /**
     * Remove a component that was previously interested in notifications of events.
     *
     * @param listener component to stop notifying
     */
    public final void removeEventListener(L listener) {
        if (!listener.getClass().isAnnotationPresent(PostEventListener.class)) {
            eventListenerList.remove(listener);
        } else {
            postEventListenerList.remove(listener);
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
        Objects.requireNonNull(event, "Event must not be null");
        for (L listener : eventListenerList) {
            event.notify(listener);
        }
        for (L listener : postEventListenerList) {
            event.notify(listener);
        }
    }

    /**
     * Release this component.
     */
    public final void release() {
        eventListenerList.clear();
        postEventListenerList.clear();
    }

    public C callbacks() {
        return cbs;
    }
}
