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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.binding.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of media list player playback modes.
 */
public enum libvlc_playback_mode_e {

    libvlc_playback_mode_default (0),
    libvlc_playback_mode_loop    (1),
    libvlc_playback_mode_repeat  (2);

    private static final Map<Integer, libvlc_playback_mode_e> INT_MAP = new HashMap<Integer, libvlc_playback_mode_e>();

    static {
        for(libvlc_playback_mode_e event : libvlc_playback_mode_e.values()) {
            INT_MAP.put(event.intValue, event);
        }
    }

    public static libvlc_playback_mode_e event(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    private libvlc_playback_mode_e(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
