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

import uk.co.caprica.vlcj.enums.PlaybackMode;

// FIXME probably move to Controls?

public final class ModeService extends BaseService {

    ModeService(DefaultMediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    /**
     * Set the media list play mode.
     * <p>
     * Note that if setting the play mode to {@link PlaybackMode#REPEAT} you can not simply play the media list,
     * you must instead play a particular item (by its index).
     *
     * @param mode mode
     */
    public boolean setMode(PlaybackMode mode) {
        if (mode != null) {
            libvlc.libvlc_media_list_player_set_playback_mode(mediaListPlayerInstance, mode.intValue());
            return true;
        } else {
            return false;
        }
    }

}
