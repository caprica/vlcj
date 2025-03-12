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

import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.player.base.events.MediaPlayerEventFactory;

/**
 * Event listener implementation that waits for the first position changed event and raises a synthetic media player
 * ready event.
 * <p>
 * Some media player operations require that the media be definitively playing before they are effective and the
 * "playing" event itself does not guarantee this.
 * <p>
 * Behaviour is this:
 * <ul>
 *     <li>each time new media is set, a "media changed" event is fired;</li>
 *     <li>if media reaches the end normally, a "finished" event is fired, followed by a "stopped" event;</li>
 *     <li>if media is stopped, a "finished" event is fired, followed by a "stopped" event;</li>
 *     <li>if new media is set before the current media expires, a "media changed" event is fired but neither "finished"
 *     nor "stopped" is fired.</li>
 * </ul>
 * The net result of the above is that the "ready" event state needs to be reset on a "stopped" event, and on a "media
 * changed" event.
 */
final class MediaPlayerReadyEventHandler extends MediaPlayerEventAdapter {

    /**
     * Flag if the event has fired since the media was last started or not.
     */
    private boolean fired;

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, MediaRef media) {
        fired = false;
    }

    @Override
    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
        if (!fired && newTime > 0) {
            fired = true;
            mediaPlayer.events().raiseEvent(MediaPlayerEventFactory.createMediaPlayerReadyEvent(mediaPlayer));
        }
    }

    @Override
    public void positionChanged(MediaPlayer mediaPlayer, double newPosition) {
        if (!fired && newPosition > 0) {
            fired = true;
            mediaPlayer.events().raiseEvent(MediaPlayerEventFactory.createMediaPlayerReadyEvent(mediaPlayer));
        }
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        fired = false;
    }
}
