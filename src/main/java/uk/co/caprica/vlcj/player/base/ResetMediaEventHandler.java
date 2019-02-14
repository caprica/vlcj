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

/**
 * Event listener implementation that "resets" the media after it has finished playing.
 * <p>
 * For some reason, we must invoke stop here to get the media to play again with the subsequent play - note that we
 * don't just make play itself always invoke stop first, since play may be resuming from a paused state or the media is
 * already playing (we wouldn't want to stop playback in those circumstances).
 * <p>
 * This internal handler must be added before {@link RepeatPlayEventHandler}.
 */
final class ResetMediaEventHandler extends MediaPlayerEventAdapter {

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        // It is not allowed to call back into LibVLC from this native thread, so offload to a task to repeat play
        mediaPlayer.submit(new ResetMediaTask(mediaPlayer));
    }

    private static class ResetMediaTask implements Runnable {

        private final MediaPlayer mediaPlayer;

        private ResetMediaTask(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
        }

        @Override
        public void run() {
            mediaPlayer.controls().stop();
        }
    }

}
