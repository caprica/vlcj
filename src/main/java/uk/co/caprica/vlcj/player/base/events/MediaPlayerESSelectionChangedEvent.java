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
import uk.co.caprica.vlcj.binding.internal.media_player_es_selection_changed;
import uk.co.caprica.vlcj.binding.support.strings.NativeString;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

/**
 * Encapsulation of a media player elementary stream updated event.
 */
final class MediaPlayerESSelectionChangedEvent extends MediaPlayerEvent {

    private final int type;

    private final String unselectedStreamId;

    private final String selectedStreamId;

    MediaPlayerESSelectionChangedEvent(MediaPlayer mediaPlayer, libvlc_event_t event) {
        super(mediaPlayer);

        this.type               = ((media_player_es_selection_changed) event.u.getTypedValue(media_player_es_selection_changed.class)).i_type;
        this.unselectedStreamId = NativeString.copyNativeString(((media_player_es_selection_changed) event.u.getTypedValue(media_player_es_selection_changed.class)).psz_unselected_id);
        this.selectedStreamId   = NativeString.copyNativeString(((media_player_es_selection_changed) event.u.getTypedValue(media_player_es_selection_changed.class)).psz_selected_id);
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.elementaryStreamSelected(mediaPlayer, TrackType.trackType(type), unselectedStreamId, selectedStreamId);
    }

}
