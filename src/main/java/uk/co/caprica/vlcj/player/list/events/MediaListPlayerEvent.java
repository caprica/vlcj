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
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.list.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;
import uk.co.caprica.vlcj.support.eventmanager.BaseEvent;

/**
 * Base implementation for media list player events.
 * <p>
 * Every instance of an event refers to an associated media list player.
 */
abstract class MediaListPlayerEvent extends BaseEvent<MediaListPlayer, MediaListPlayerEventListener> {

    /**
     * Create a media player event.
     *
     * @param libvlcInstance native library instance
     * @param mediaListPlayer media player that the event relates to
     */
    protected MediaListPlayerEvent(libvlc_instance_t libvlcInstance, MediaListPlayer mediaListPlayer) {
        super(libvlcInstance, mediaListPlayer);
    }

}
