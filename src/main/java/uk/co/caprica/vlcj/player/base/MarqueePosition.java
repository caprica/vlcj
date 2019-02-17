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

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of marquee positions.
 */
public enum MarqueePosition {

    CENTRE      ( 0),
    LEFT        ( 1),
    RIGHT       ( 2),

    TOP         ( 4),
    TOP_LEFT    ( 5),
    TOP_RIGHT   ( 6),

    BOTTOM      ( 8),
    BOTTOM_LEFT ( 9),
    BOTTOM_RIGHT(10);

    private static final Map<Integer, MarqueePosition> INT_MAP = new HashMap<Integer, MarqueePosition>();

    static {
        for (MarqueePosition value : MarqueePosition.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static MarqueePosition position(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    MarqueePosition(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
