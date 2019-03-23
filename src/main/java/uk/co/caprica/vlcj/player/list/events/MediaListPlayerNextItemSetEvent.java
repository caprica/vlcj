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

import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.media_list_player_next_item_set;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;

/**
 * Encapsulation of a media list player next item set event.
 */
final class MediaListPlayerNextItemSetEvent extends MediaListPlayerEvent {

    /**
     * Media instance.
     */
    private final libvlc_media_t item;

    /**
     * Create a media player event.
     *
     * @param libvlcInstance native library instance
     * @param mediaListPlayer media player the event relates to
     */
    MediaListPlayerNextItemSetEvent(libvlc_instance_t libvlcInstance, MediaListPlayer mediaListPlayer, libvlc_event_t event) {
        super(libvlcInstance, mediaListPlayer);
        this.item = ((media_list_player_next_item_set) event.u.getTypedValue(media_list_player_next_item_set.class)).item;
    }

    @Override
    public void notify(MediaListPlayerEventListener listener) {
        listener.nextItem(component, temporaryMediaRef(item));
    }

}
