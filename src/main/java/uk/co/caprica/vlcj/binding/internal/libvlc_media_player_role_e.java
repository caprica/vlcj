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
 * Copyright 2009-2017 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.binding.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of media player roles.
 */
public enum libvlc_media_player_role_e {

    libvlc_role_None         (0), /* Don't use a media player role */
    libvlc_role_Music        (1), /* Music (or radio) playback */
    libvlc_role_Video        (2), /* Video playback */
    libvlc_role_Communication(3), /* Speech, real-time communication */
    libvlc_role_Game         (4), /* Video game */
    liblvc_role_Notification (5), /* User interaction feedback */
    libvlc_role_Animation    (6), /* Embedded animation (e.g. in web page) */
    libvlc_role_Production   (7), /* Audio editting/production */
    libvlc_role_Accessibility(8), /* Accessibility */
    libvlc_role_Test         (9); /* Testing */

    private static final Map<Integer, libvlc_media_player_role_e> INT_MAP = new HashMap<Integer, libvlc_media_player_role_e>();

    static {
        for(libvlc_media_player_role_e role : libvlc_media_player_role_e.values()) {
            INT_MAP.put(role.intValue, role);
        }
    }

    public static libvlc_media_player_role_e role(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    private libvlc_media_player_role_e(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
