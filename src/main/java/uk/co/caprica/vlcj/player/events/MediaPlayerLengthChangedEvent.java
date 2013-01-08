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

package uk.co.caprica.vlcj.player.events;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/**
 *
 */
class MediaPlayerLengthChangedEvent extends AbstractMediaPlayerEvent {

    /**
     * Length, in milliseconds.
     */
    private final long newLength;

    /**
     * Create a media player event.
     *
     * @param mediaPlayer media player the event relates to
     * @param newLength length, in milliseconds
     */
    MediaPlayerLengthChangedEvent(MediaPlayer mediaPlayer, long newLength) {
        super(mediaPlayer);
        this.newLength = newLength;
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.lengthChanged(mediaPlayer, newLength);
    }
}
