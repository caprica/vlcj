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

package uk.co.caprica.vlcj.player.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of media player roles.
 */
public enum MediaPlayerRole {

    NONE         (0), /* Don't use a media player role */
    MUSIC        (1), /* Music (or radio) playback */
    VIDEO        (2), /* Video playback */
    COMMUNICATION(3), /* Speech, real-time communication */
    GAME         (4), /* Video game */
    NOTIFICATION (5), /* User interaction feedback */
    ANIMATION    (6), /* Embedded animation (e.g. in web page) */
    PRODUCTION   (7), /* Audio editting/production */
    ACCESSIBILITY(8), /* Accessibility */
    TEST         (9); /* Testing */

    private static final Map<Integer, MediaPlayerRole> INT_MAP = new HashMap<Integer, MediaPlayerRole>();

    static {
        for (MediaPlayerRole role : MediaPlayerRole.values()) {
            INT_MAP.put(role.intValue, role);
        }
    }

    public static MediaPlayerRole role(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    MediaPlayerRole(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
