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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.media_player_title_selection_changed;
import uk.co.caprica.vlcj.binding.support.strings.NativeString;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.base.TitleDescription;

/**
 * Encapsulation of a media player title changed event.
 */
final class MediaPlayerTitleSelectionChangedEvent extends MediaPlayerEvent {

    private final TitleDescription title;

    private final int index;

    MediaPlayerTitleSelectionChangedEvent(MediaPlayer mediaPlayer, libvlc_event_t event) {
        super(mediaPlayer);
        media_player_title_selection_changed data = (media_player_title_selection_changed) event.u.getTypedValue(media_player_title_selection_changed.class);
        this.title = new TitleDescription(data.title.i_duration, NativeString.copyNativeString(data.title.psz_name), data.title.i_flags);
        this.index = data.index;
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.titleSelectionChanged(mediaPlayer, title, index);
    }

}
