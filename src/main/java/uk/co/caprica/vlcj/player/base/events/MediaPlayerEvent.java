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

package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.support.eventmanager.EventNotification;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

/**
 * Base implementation for media list player events.
 * <p>
 * Every instance of an event refers to an associated media player.
 */
public abstract class MediaPlayerEvent implements EventNotification<MediaPlayerEventListener> {

    /**
     * The media player the event relates to.
     */
    protected final MediaPlayer mediaPlayer;

    /**
     * Create a media player event.
     *
     * @param mediaPlayer media player that the event relates to
     */
    protected MediaPlayerEvent(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

}
