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

package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.player.base.State;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_get_state;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_is_playing;

/**
 * Behaviour pertaining to media list player status.
 */
public final class StatusApi extends BaseApi {

    StatusApi(MediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    /**
     * Is the media list player currently playing?
     *
     * @return <code>true</code> if playing; <code>false</code> if not
     */
    public boolean isPlaying() {
        return libvlc_media_list_player_is_playing(mediaListPlayerInstance) != 0;
    }

    /**
     * Get the current media list player state.
     *
     * @return state
     */
    public State getMediaListPlayerState() {
        return State.state(libvlc_media_list_player_get_state(mediaListPlayerInstance));
    }

}
