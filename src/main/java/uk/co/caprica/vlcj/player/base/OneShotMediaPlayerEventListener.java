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
 * Copyright 2009-2020 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

/**
 * Base implementation for a component that supports "one-shot" event listeners, i.e. event listeners attached to a
 * media player instance that will remove themselves when an event of interest is fired.
 * <p>
 * A sub-class should override methods to implement the desired event behaviour, and from one or other of those methods
 * it should invoke {@link #done(MediaPlayer)} to cause the listener to be removed.
 * <p>
 * If is the event method implementations that decide if/when the event listener should be removed, so it may not
 * strictly be one received event only - for example if a listener were added to trigger when the playback time reached
 * five seconds, there would be multiple intermediate time changed events fired before the target time were reached.
 * <p>
 * Note that by default, the following methods will be implemented to invoke {@link #done(MediaPlayer)}, this behaviour
 * can be overriden by sub-classes:
 * <ul>
 *     <li>{@link #stopped(MediaPlayer)}</li>
 *     <li>{@link #finished(MediaPlayer)}</li>
 *     <li>{@link #error(MediaPlayer)}</li>
 * </ul>
 */
public abstract class OneShotMediaPlayerEventListener extends MediaPlayerEventAdapter {

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        done(mediaPlayer);
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        done(mediaPlayer);
    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
        done(mediaPlayer);
    }

    /**
     * Consume the event, thereby causing this event listener to be removed from the media player.
     *
     * @param mediaPlayer media player that raised the event
     */
    protected final void done(MediaPlayer mediaPlayer) {
        mediaPlayer.events().removeMediaPlayerEventListener(this);
        onDone(mediaPlayer);
    }

    /**
     * Optional template method that can be overridden by sub-classes to provide custom behaviour after the event
     * listener has been removed from the media player.
     *
     * @param mediaPlayer media player that raised the event
     */
    protected void onDone(MediaPlayer mediaPlayer) {
    }
}
