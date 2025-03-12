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

package uk.co.caprica.vlcj.player.base;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class implements a mechanism to stop a media player and wait for it to stop (or wait for it to raise an error
 * instead of cleanly stopping).
 * <p>
 * Ordinarily a call to stop a media player returns immediately and the native media player will stop asynchronously.
 * This can make it a little difficult for application code to know if the media player has definitely stopped before
 * the application performs any clean-up, like releasing the media player (which might cause a crash).
 * <p>
 * It is possible for application code to respond to media player events to determine whether the media player stopped
 * definitively but this class serves as a convenient encapsulation of that functionality.
 * <p>
 * The strategy is simply to block the stop call until a media player "stopped" or "error" event is received.
 */
final class MediaPlayerStopLatch {

    /**
     * Media player instance.
     */
    private final MediaPlayer mediaPlayer;

    /**
     * Create a new media player latch.
     *
     * @param mediaPlayer media player instance
     */
    MediaPlayerStopLatch(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    boolean stop() {
        CountDownLatch latch = new CountDownLatch(1);
        LatchListener listener = new LatchListener(latch);
        mediaPlayer.events().addMediaPlayerEventListener(listener);
        try {
            boolean stopping = mediaPlayer.controls().stopAsync();
            if (stopping) {
                latch.await();
                return listener.stopped.get();
            } else {
                return false;
            }
        }
        catch (InterruptedException e) {
            return false;
        }
        finally {
            mediaPlayer.events().removeMediaPlayerEventListener(listener);
        }
    }

    /**
     * Short-lived listener to wait for stopped/error events.
     */
    private static final class LatchListener extends MediaPlayerEventAdapter {

        /**
         * Synchronisation latch.
         */
        private final CountDownLatch latch;

        /**
         * True if the media stopped, otherwise false.
         */
        private final AtomicBoolean stopped = new AtomicBoolean();

        /**
         * Create a new listener.
         *
         * @param latch synchronisation latch.
         */
        private LatchListener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void stopped(MediaPlayer mediaPlayer) {
            stopped.set(true);
            latch.countDown();
        }

        @Override
        public void error(MediaPlayer mediaPlayer) {
            stopped.set(false);
            latch.countDown();
        }
    }

}
