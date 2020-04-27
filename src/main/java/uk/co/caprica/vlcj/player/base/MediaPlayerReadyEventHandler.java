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

import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.player.base.events.MediaPlayerEventFactory;

/**
 * Event listener implementation that waits for the first position changed event and raises a synthetic media player
 * ready event.
 * <p>
 * Some media player operations require that the media be definitively playing before they are effective and the
 * "playing" event itself does not guarantee this.
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
    public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
        if (!fired && newPosition > 0) {
            fired = true;
            mediaPlayer.events().raiseEvent(MediaPlayerEventFactory.createMediaPlayerReadyEvent(mediaPlayer));
        }
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        fired = false;
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        fired = false;
    }

}
