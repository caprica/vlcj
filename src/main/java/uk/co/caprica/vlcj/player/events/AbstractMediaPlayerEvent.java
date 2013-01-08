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

/**
 * Base implementation for media player events.
 * <p>
 * Every instance of an event refers to an associated media player.
 */
abstract class AbstractMediaPlayerEvent implements MediaPlayerEvent {

    /**
     * The media player the event relates to.
     */
    protected final MediaPlayer mediaPlayer;

    /**
     * Create a media player event.
     *
     * @param mediaPlayer media player that the event relates to
     */
    protected AbstractMediaPlayerEvent(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
