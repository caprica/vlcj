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

package uk.co.caprica.vlcj.waiter;

import uk.co.caprica.vlcj.waiter.media.MediaWaiter;
import uk.co.caprica.vlcj.waiter.mediaplayer.MediaPlayerWaiter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Base implementation for a component that waits for specific component state to occur.
 * <p>
 * This implementation works by adding a temporary event listener to an associated component then waiting, via
 * {@link #await()}, for an internal synchronisation object to trigger, via {@link #ready()} or {@link #ready(Object)}
 * when one or other of the event listener's implementation methods detects the desired component event. The temporary
 * event listener is then removed and the {@link #await()} method is unblocked and returns.
 * <p>
 * Commonly needed triggers are implemented for {@link #error()} and {@link #finished()}.
 * <p>
 * This facilitates a semblance of <em>synchronous</em> or sequential programming.
 * <p>
 * Sub-classes have access to the associated {@link #component} if needed.
 * <p>
 * Sub-classes may also override {@link #onBefore(Object)} and {@link #onAfter(Object, Object)} to implement behaviour
 * that executes respectively before awaiting the condition and after the condition state is reached.
 * <p>
 * Using {@link #onBefore(Object)} guarantees that the component event listener has been registered with the media
 * player before the condition implementation is executed (e.g. an implementation of this method could check if the
 * condition they would otherwise wait for has already been achieved).
 * <p>
 * Note that as with other media player and related component implementations the event callbacks are running in a
 * native thread.
 *
 * @param <C> type of component
 * @param <R> type of result that may be returned when the desired condition arises
 *
 * @see MediaWaiter
 * @see MediaPlayerWaiter
 */
public abstract class Waiter<C, R> {

    /**
     * Synchronisation object used to wait until the component state reaches the desired condition.
     */
    private final CountDownLatch completionLatch = new CountDownLatch(1);

    /**
     * Result status indicator.
     */
    private final AtomicReference<ResultStatus> resultStatus = new AtomicReference<ResultStatus>();

    /**
     * Optional result.
     */
    private final AtomicReference<R> result = new AtomicReference<R>();

    /**
     * Flag indicating if this condition is already finished.
     */
    private final AtomicBoolean finished = new AtomicBoolean();

    /**
     * Associated component.
     */
    protected final C component;

    /**
     * Create a new waiter.
     *
     * @param component component to wait for
     */
    public Waiter(C component) {
        this.component = component;
    }

    /**
     * Wait for the required condition to occur.
     *
     * @return optional result
     * @throws InterruptedException if the condition was interrupted while waiting
     * @throws UnexpectedWaiterErrorException if an unexpected error occurred
     * @throws UnexpectedWaiterFinishedException if the condition finished unexpectedly
     */
    public final R await() throws InterruptedException, UnexpectedWaiterErrorException, UnexpectedWaiterFinishedException {
        startListening(component);
        if (onBefore(component)) {
            completionLatch.await();
            switch (resultStatus.get()) {
                case NORMAL:
                    onAfter(component, this.result.get());
                    return result.get();
                case ERROR:
                    throw new UnexpectedWaiterErrorException();
                case FINISHED:
                    throw new UnexpectedWaiterFinishedException();
                default:
                    throw new IllegalStateException("Unexpected result status: " + resultStatus.get());
            }
        } else {
            throw new BeforeWaiterAbortedException();
        }
    }

    /**
     * Trigger method invoked by a sub-class event handler when the desired component state is detected.
     * <p>
     * This is a convenience for {@link #ready(Object)} when no result is needed.
     */
    protected final void ready() {
        ready(null);
    }

    /**
     * Trigger method invoked by a sub-class event handler when the desired component state is detected.
     *
     * @param result optional result, may be <code>null</code>
     */
    protected final void ready(R result) {
        if (!finished.getAndSet(true)) {
            this.result.set(result);
            release(ResultStatus.NORMAL);
        }
    }

    /**
     * Trigger method invoked by a sub-class event handler when the media player reports an error has occurred.
     */
    protected final void error() {
        release(ResultStatus.ERROR);
    }

    /**
     * Trigger method invoked by a sub-class event handler when the media player reports the end of the media has been
     * reached.
     */
    protected final void finished() {
        release(ResultStatus.FINISHED);
    }

    /**
     * Template method invoked after the listener has been added but before the {@link #await()} is invoked.
     * <p>
     * Returning <code>false</code> will cause {@link BeforeWaiterAbortedException} to be thrown.
     *
     * @param component component
     * @return <code>true</code> to continue; <code>false</code> to abort
     */
    protected boolean onBefore(C component) {
        return true;
    }

    /**
     * Template method invoked after the component state has reached the desired condition.
     *
     * @param component component
     * @param result optional result
     */
    protected void onAfter(C component, R result) {
    }

    /**
     * Template method invoked to hook up the per-component event listener.
     *
     * @param component component to start listening to
     */
    protected abstract void startListening(C component);

    /**
     * Template method to remove the per-component event listener.
     *
     * @param component  component to stop listening to
     */
    protected abstract void stopListening(C component);

    /**
     * Release the waiter.
     *
     * @param resultStatus result status indicator
     */
    private void release(ResultStatus resultStatus) {
        stopListening(component);
        this.resultStatus.set(resultStatus);
        completionLatch.countDown();
    }

}
