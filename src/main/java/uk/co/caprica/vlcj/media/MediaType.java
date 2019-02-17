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
 * Enumeration of media types.
 */
public enum MediaType {

    UNKNOWN  (0),
    FILE     (1),
    DIRECTORY(2),
    DISC     (3),
    STREAM   (4),
    PLAYLIST (5);

    private static final Map<Integer, MediaType> INT_MAP = new HashMap<Integer, MediaType>();

    static {
        for (MediaType value : MediaType.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static MediaType mediaType(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    MediaType(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
