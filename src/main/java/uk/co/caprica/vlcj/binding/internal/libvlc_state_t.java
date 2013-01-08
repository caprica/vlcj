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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.binding.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of native media/player states.
 */
public enum libvlc_state_t {

    libvlc_NothingSpecial(0),
    libvlc_Opening       (1),
    libvlc_Buffering     (2),
    libvlc_Playing       (3),
    libvlc_Paused        (4),
    libvlc_Stopped       (5),
    libvlc_Ended         (6),
    libvlc_Error         (7);

    private static final Map<Integer, libvlc_state_t> INT_MAP = new HashMap<Integer, libvlc_state_t>();

    static {
        for(libvlc_state_t event : libvlc_state_t.values()) {
            INT_MAP.put(event.intValue, event);
        }
    }

    /**
     * Get an enumerated value for a native value.
     *
     * @param intValue native value
     * @return enumerated value
     */
    public static libvlc_state_t state(int intValue) {
        return INT_MAP.get(intValue);
    }

    /**
     * Native value.
     */
    private final int intValue;

    /**
     * Create an enumerated value.
     *
     * @param intValue native value
     */
    private libvlc_state_t(int intValue) {
        this.intValue = intValue;
    }

    /**
     * Get the native value.
     *
     * @return value
     */
    public int intValue() {
        return intValue;
    }
}
