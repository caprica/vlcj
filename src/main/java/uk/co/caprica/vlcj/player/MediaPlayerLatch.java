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

package uk.co.caprica.vlcj.player;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import uk.co.caprica.vlcj.logger.Logger;

/**
 * This class implements a mechanism to play a media item and wait for it to start (or wait for it
 * to raise an error instead of starting).
 * <p>
 * Ordinarily a call to play new media returns immediately and the native media player attempts to
 * open and start playing the media asynchronously. This can make it a little difficult for
 * application code to know if the media successfully started or failed to start because of an
 * error.
 * <p>
 * It is possible for application code to respond to media player events to determine whether the
 * media started successfully or failed because of an error but this class serves as a convenient
 * encapsulation of that functionality.
 * <p>
 * The strategy is simply to block the play call until a media player "playing" or "error" event is
 * received.
 * <p>
 * Example usage:
 *
 * <pre>
 * mediaPlayer.prepareMedia(mrl, options);
 * boolean definitelyStarted = new MediaPlayerLatch(mediaPlayer).play();
 * </pre>
 *
 * The {@link DefaultMediaPlayer} uses this class for the "play and wait..." implementation, see
 * {@link MediaPlayer#startMedia(String, String...)}.
 * <p>
 * Most applications are not expected to need this class and use the "start media" functionality on
 * the media player instead.
 */
public class MediaPlayerLatch {

    /**
     * Media player instance.
     */
    private final MediaPlayer mediaPlayer;

    /**
     * Create a new media player latch.
     *
     * @param mediaPlayer media player instance
     */
    public MediaPlayerLatch(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    /**
     * Play the media and wait for it to either start playing or error.
     *
     * @return true if the media definitely started playing and false if it did not or the thread
     *          was interrupted while waiting (unlikely, but in which case the media player
     *          <em>might</em> still start)
     */
    public boolean play() {
        Logger.debug("play()");
        // If the media player is already playing, then the latch will wait for an event that
        // will never arrive and so will block incorrectly
        if(!mediaPlayer.isPlaying()) {
            CountDownLatch latch = new CountDownLatch(1);
            LatchListener listener = new LatchListener(latch);
            mediaPlayer.addMediaPlayerEventListener(listener);
            mediaPlayer.play();
            try {
                Logger.debug("Waiting for media playing or error...");
                latch.await();
                Logger.debug("Finished waiting.");
                boolean started = listener.playing.get();
                Logger.debug("started={}", started);
                return started;
            }
            catch(InterruptedException e) {
                Logger.debug("Interrupted while waiting for media player", e);
                return false;
            }
            finally {
                mediaPlayer.removeMediaPlayerEventListener(listener);
            }
        }
        else {
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
