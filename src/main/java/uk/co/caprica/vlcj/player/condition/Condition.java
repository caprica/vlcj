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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.condition;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/**
 * Base implementation for a component that waits for specific media player state
 * to occur.
 * <p>
 * Instances of this class, or its sub-classes, are <em>not</em> reusable.
 * <p>
 * This implementation works by adding a temporary event listener to an associated
 * media player then waiting, via {@link #await()}, for an internal synchronisation
 * object to trigger, via {@link #ready()} or {@link #ready(Object)}  when one or
 * other of the event listener's implementation methods detects the desired media
 * player state. The temporary event listener is then removed and the {@link #await()}
 * method is unblocked and returns.
 * <p>
 * Commonly needed triggers are implemented for {@link #error()} and {@link #finished()}.
 * <p>
 * This facilitates a semblance of <em>synchronous</em> or sequential media player
 * programming.
 * <p>
 * Sub-classes have access to the associated {@link #mediaPlayer} if needed.
 * <p>
 * Sub-classes may also override {@link #onBefore()} and {@link #onAfter(Object)} to
 * implement behaviour that executes respectively before awaiting the condition and
 * after the condition state is reached.
 * <p>
 * Using {@link #onBefore()} guarantees that the media player event listener has been
 * registered with the media player before the condition implementation is executed.
 * <p>
 * Note that as with other {@link MediaPlayerEventListener} implementations the
 * event callbacks are running in a background thread.
 * <p>
 * Example:
 * <pre>
 *    try {
 *        Condition&lt;?&gt; playingCondition = new PlayingCondition(mediaPlayer) {
 *            {@literal @}Override
 *            protected void onBefore() {
 *                mediaPlayer.play();
 *            }
 *        };
 *        playingCondition.await();
 *
 *        // Do some interesting things, wait for some other conditions...
 *    }
 *    catch(UnexpectedErrorConditionException e) {
 *        // Whatever...
 *    }
 *    catch(UnexpectedFinishedConditionException e) {
 *        // Whatever...
 *    }
 * </pre>
 *
 * @param <T> type of result that may be returned when the desired condition arises
 *
 * @see DefaultCondition
 */
public abstract class Condition<T> extends MediaPlayerEventAdapter {

    /**
     * Synchronisation object used to wait until the media player state reaches
     * the desired condition.
     */
    private final CountDownLatch completionLatch = new CountDownLatch(1);

    /**
     * Result status indicator.
     */
    private final AtomicReference<ResultStatus> resultStatus = new AtomicReference<ResultStatus>();

    /**
     * Optional result.
     */
    private final AtomicReference<T> result = new AtomicReference<T>();

    /**
     * Flag indicating if this condition is already finished.
     */
    private final AtomicBoolean finished = new AtomicBoolean();

    /**
     * Associated media player.
     */
    protected final MediaPlayer mediaPlayer;

    /**
     * Simple flag to track whether or not this instance has been used before -
     * instances are <em>not</em> reusable.
     */
    private boolean used;

    /**
     * Create a new waiter.
     *
     * @param mediaPlayer media player
     */
    public Condition(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        // Listen for media player events
        mediaPlayer.addMediaPlayerEventListener(this);
    }

    /**
     * Wait for the required condition to occur.
     *
     * @return optional result
     * @throws InterruptedException
     * @throws UnexpectedErrorConditionException
     * @throws UnexpectedFinishedConditionException
     */
    public final T await() throws InterruptedException, UnexpectedErrorConditionException, UnexpectedFinishedConditionException {
        Logger.debug("await()");
        if(!used) {
            used = true;
            // Invoke the template method before waiting
            onBefore();
            // Wait for the completion latch to be triggered...
            completionLatch.await();
            // Depending on the result status...
            switch(resultStatus.get()) {
                case NORMAL:
                    // ...normal processing, first invoke the template method after finished
                    onAfter(this.result.get());
                    // ...then return the result
                    return result.get();
                case ERROR:
                    // ...an error occurred
                    throw new UnexpectedErrorConditionException();
                case FINISHED:
                    // ...the media finished unexpectedly
                    throw new UnexpectedFinishedConditionException();
                default:
                    // Can not happen
                    throw new IllegalStateException("Unexpected result status: " + resultStatus.get());
            }
        }
        else {
            throw new IllegalStateException("Can not re-use waiter instances, create a new instance instead");
        }
    }

    /**
     * Trigger method invoked by a sub-class event handler when the desired media
     * player state is detected.
     * <p>
     * This is a convenience for {@link #ready(Object)} when no result is needed.
     */
    protected final void ready() {
        Logger.debug("ready()");
        ready(null);
    }

    /**
     * Trigger method invoked by a sub-class event handler when the desired media
     * player state is detected.
     *
     * @param result optional result, may be <code>null</code>
     */
    protected final void ready(T result) {
        Logger.debug("ready(result={})", result);
        if(!finished.getAndSet(true)) {
            Logger.debug("Finished");
            // Store the result
            this.result.set(result);
            // Finish waiting and release the waiter
            release(ResultStatus.NORMAL);
        }
        else {
            Logger.debug("Already finished");
        }
    }

    /**
     * Trigger method invoked by a sub-class event handler when the media player
     * reports an error has occurred.
     */
    protected final void error() {
        Logger.debug("error()");
        // Finish waiting...
        release(ResultStatus.ERROR);
    }

    /**
     * Trigger method invoked by a sub-class event handler when the media player
     * reports the end of the media has been reached.
     */
    protected final void finished() {
        Logger.debug("finished()");
        // Finish waiting...
        release(ResultStatus.FINISHED);
    }

    /**
     * Template method invoked after the listener has been added but before the
     * {@link #await()} is invoked.
     */
    protected void onBefore() {
        // Default implementation does nothing
    }

    /**
     * Template method invoked after the media player state has reached the desired
     * condition.
     *
     * @param result optional result
     */
    protected void onAfter(T result) {
        // Default implementation does nothing
    }

    /**
     * Release the waiter.
     *
     * @param resultStatus result status indicator
     */
    private void release(ResultStatus resultStatus) {
        // Stop listening for media player events
        mediaPlayer.removeMediaPlayerEventListener(this);
        // Store the result status
        this.resultStatus.set(resultStatus);
        // Trigger the completion latch to release the waiter
        completionLatch.countDown();
    }

    /**
     * Enumeration of result status.
     */
    private enum ResultStatus {

        /**
         * Processing completed normally.
         */
        NORMAL,

        /**
         * An error occurred.
         */
        ERROR,

        /**
         * The media finished unexpectedly.
         */
        FINISHED
    }
}
