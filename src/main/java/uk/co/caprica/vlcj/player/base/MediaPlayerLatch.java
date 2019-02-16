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

package uk.co.caprica.vlcj.player.base;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class implements a mechanism to play a media item and wait for it to start (or wait for it to raise an error
 * instead of starting).
 * <p>
 * Ordinarily a call to play new media returns immediately and the native media player attempts to open and start
 * playing the media asynchronously. This can make it a little difficult for application code to know if the media
 * successfully started or failed to start because of an error.
 * <p>
 * It is possible for application code to respond to media player events to determine whether the media started
 * successfully or failed because of an error but this class serves as a convenient encapsulation of that functionality.
 * <p>
 * The strategy is simply to block the play call until a media player "playing" or "error" event is received.
 */
final class MediaPlayerLatch {

    /**
     * Media player instance.
     */
    private final MediaPlayer mediaPlayer;

    /**
     * Create a new media player latch.
     *
     * @param mediaPlayer media player instance
     */
    MediaPlayerLatch(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    /**
     * Play the media and wait for it to either start playing or error.
     *
     * @return true only if the media definitely started playing; <code>false</code> otherwise, it may yet have started
     */
    boolean play() {
        // If the media player is already playing, then the latch will wait for an event that
        // will never arrive and so will block incorrectly
        if (!mediaPlayer.status().isPlaying()) {
            CountDownLatch latch = new CountDownLatch(1);
            LatchListener listener = new LatchListener(latch);
            mediaPlayer.events().addMediaPlayerEventListener(listener);
            mediaPlayer.controls().play();
            try {
                latch.await();
                return listener.playing.get();
            }
            catch(InterruptedException e) {
                return false;
            }
            finally {
                mediaPlayer.events().removeMediaPlayerEventListener(listener);
            }
        } else {
            return true;
        }
    }

    /**
     * Short-lived listener to wait for playing/error events.
     */
    private static final class LatchListener extends MediaPlayerEventAdapter {

        /**
         * Synchronisation latch.
         */
        private final CountDownLatch latch;

        /**
         * True if the media started, otherwise false.
         */
        private final AtomicBoolean playing = new AtomicBoolean();

        /**
         * Create a new listener.
         *
         * @param latch synchronisation latch.
         */
        private LatchListener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void playing(MediaPlayer mediaPlayer) {
            playing.set(true);
            latch.countDown();
        }

        @Override
        public void error(MediaPlayer mediaPlayer) {
            playing.set(false);
            latch.countDown();
        }
    }

}
