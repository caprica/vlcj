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

package uk.co.caprica.vlcj.player;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of track types.
 */
public enum TrackType {

    /**
     * Unknown.
     */
    UNKNOWN (-1),

    /**
     * Audio track.
     */
    AUDIO   ( 0),

    /**
     * Video track.
     */
    VIDEO   ( 1),

    /**
     * Text track.
     * <p>
     * Also known as "subtitle" or "sub-picture".
     */
    TEXT    ( 2);

    private static final Map<Integer, TrackType> INT_MAP = new HashMap<Integer, TrackType>();

    static {
        for(TrackType value : TrackType.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static TrackType trackType(int intValue) {
        return INT_MAP.get(intValue);
    }

    /**
     * Raw value.
     */
    private final int intValue;

    /**
     * Raw value.
     *
     * @param value raw value
     */
    private TrackType(int value) {
        this.intValue = value;
    }

    /**
     * Get the raw value.
     *
     * @return raw value
     */
    public int value() {
        return intValue;
    }
}
