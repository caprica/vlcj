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

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.enums.MediaPlayerRole;

// FIXME rename to simple get/set ? so we end up mediaPlayer().role().set(...)

public final class RoleService extends BaseService {

    RoleService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the media player role.
     *
     * @return media player role
     */
    public MediaPlayerRole getRole() {
        return MediaPlayerRole.role(libvlc.libvlc_media_player_get_role(mediaPlayerInstance));
    }

    /**
     * Set the media player role.
     *
     * @param role media player role
     */
    public void setRole(MediaPlayerRole role) {
        libvlc.libvlc_media_player_set_role(mediaPlayerInstance, role.intValue());
    }

}
