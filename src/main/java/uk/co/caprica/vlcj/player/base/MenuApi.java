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

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_navigate;

/**
 * Behaviour pertaining to the menu (e.g. DVD and Bluray menus).
 */
public final class MenuApi extends BaseApi {

    MenuApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Activate a menu.
     */
    public void activate() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_activate);
    }

    /**
     * Navigate up a menu.
     */
    public void up() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_up);
    }

    /**
     * Navigate down a menu.
     */
    public void down() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_down);
    }

    /**
     * Navigate left a menu.
     */
    public void left() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_left);
    }

    /**
     * Navigate right a menu.
     */
    public void right() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_right);
    }

    private void doMenuAction(libvlc_navigate_mode_e action) {
        libvlc_media_player_navigate(mediaPlayerInstance, action.intValue());
    }

}
