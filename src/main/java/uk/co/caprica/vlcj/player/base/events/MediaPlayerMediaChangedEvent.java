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

package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.media_player_media_changed;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

/**
 * Encapsulation of a media player media changed event.
 */
final class MediaPlayerMediaChangedEvent extends MediaPlayerEvent {

    private final libvlc_instance_t libvlcInstance;

    private final libvlc_media_t newMedia;

    MediaPlayerMediaChangedEvent(libvlc_instance_t libvlcInstance, MediaPlayer mediaPlayer, libvlc_event_t event) {
        super(mediaPlayer);

        this.libvlcInstance = libvlcInstance;

        this.newMedia = ((media_player_media_changed)event.u.getTypedValue(media_player_media_changed.class)).md;
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.mediaChanged(mediaPlayer, new MediaRef(libvlcInstance, newMedia));
    }

}
