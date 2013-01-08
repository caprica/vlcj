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

package uk.co.caprica.vlcj.player.list.events;

import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;

/**
 *
 */
class MediaListMediaDurationChangedEvent extends AbstractMediaListPlayerEvent {

    /**
     * Duration.
     */
    private final long newDuration;

    /**
     * Create a media list player event.
     *
     * @param mediaListPlayer media list player the event relates to
     * @param newDuration duration
     */
    MediaListMediaDurationChangedEvent(MediaListPlayer mediaListPlayer, long newDuration) {
        super(mediaListPlayer);
        this.newDuration = newDuration;
    }

    @Override
    public void notify(MediaListPlayerEventListener listener) {
        listener.mediaDurationChanged(mediaListPlayer, newDuration);
    }
}
