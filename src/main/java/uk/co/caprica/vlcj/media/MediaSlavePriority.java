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

package uk.co.caprica.vlcj.media;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of media slave priorities.
 */
public enum MediaSlavePriority {

    LOWEST (0),
    LOW    (1),
    MEDIUM (2),
    HIGH   (3),
    HIGHEST(4);

    private static final Map<Integer, MediaSlavePriority> INT_MAP = new HashMap<Integer, MediaSlavePriority>();

    static {
        for (MediaSlavePriority value : MediaSlavePriority.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static MediaSlavePriority mediaSlavePriority(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    MediaSlavePriority(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
