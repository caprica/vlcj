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

package uk.co.caprica.vlcj.player.list.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

/**
 * A factory that creates a media list player event instance for a native media list player event.
 */
public final class MediaListPlayerEventFactory {

    /**
     * Create a new media list player event for a given native event.
     *
     * @param libvlcInstance native library instance
     * @param mediaListPlayer component the event relates to
     * @param event native event
     * @return media list player event, or <code>null</code> if not a known event
     */
    public static MediaListPlayerEvent createEvent(libvlc_instance_t libvlcInstance, MediaListPlayer mediaListPlayer, libvlc_event_t event) {
        switch (libvlc_event_e.event(event.type)) {
            case libvlc_MediaListPlayerPlayed     : return new MediaListPlayerPlayedEvent     (libvlcInstance, mediaListPlayer       );
            case libvlc_MediaListPlayerNextItemSet: return new MediaListPlayerNextItemSetEvent(libvlcInstance, mediaListPlayer, event);
            case libvlc_MediaListPlayerStopped    : return new MediaListPlayerStoppedEvent    (libvlcInstance, mediaListPlayer       );

            default                               : return null;
        }
    }

    private MediaListPlayerEventFactory() {
    }

}
