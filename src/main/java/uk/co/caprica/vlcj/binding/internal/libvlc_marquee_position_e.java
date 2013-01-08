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
 * Enumeration of marquee positions.
 */
public enum libvlc_marquee_position_e {

    centre      ( 0),
    left        ( 1),
    right       ( 2),

    top         ( 4),
    top_left    ( 5),
    top_right   ( 6),

    bottom      ( 8),
    bottom_left ( 9),
    bottom_right(10);

    private static final Map<Integer, libvlc_marquee_position_e> INT_MAP = new HashMap<Integer, libvlc_marquee_position_e>();

    static {
        for(libvlc_marquee_position_e value : libvlc_marquee_position_e.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static libvlc_marquee_position_e position(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    private libvlc_marquee_position_e(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
