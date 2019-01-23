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

import uk.co.caprica.vlcj.binding.internal.libvlc_navigate_mode_e;

public final class MenuService extends BaseService {

    MenuService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Activate a menu.
     */
    public void menuActivate() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_activate);
    }

    /**
     * Navigate up a menu.
     */
    public void menuUp() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_up);
    }

    /**
     * Navigate down a menu.
     */
    public void menuDown() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_down);
    }

    /**
     * Navigate left a menu.
     */
    public void menuLeft() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_left);
    }

    /**
     * Navigate right a menu.
     */
    public void menuRight() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_right);
    }

    private void doMenuAction(libvlc_navigate_mode_e action) {
        libvlc.libvlc_media_player_navigate(mediaPlayerInstance, action.intValue());
    }

}
