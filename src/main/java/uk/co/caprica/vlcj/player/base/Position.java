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
 * Enumeration of positions, e.g. for the video title.
 */
public enum Position {

    DISABLE     (-1),

    CENTER      ( 0),
    LEFT        ( 1),
    RIGHT       ( 2),

    TOP         ( 3),
    TOP_LEFT    ( 4),
    TOP_RIGHT   ( 5),

    BOTTOM      ( 6),
    BOTTOM_LEFT ( 7),
    BOTTOM_RIGHT( 8);

    private static final Map<Integer, Position> INT_MAP = new HashMap<Integer, Position>();

    static {
        for (Position value : Position.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static Position position(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    Position(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
