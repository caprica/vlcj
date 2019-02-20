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

package uk.co.caprica.vlcj.player.list;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of media list player playback modes.
 */
public enum PlaybackMode {

    DEFAULT(0),
    LOOP   (1),
    REPEAT (2);

    private static final Map<Integer, PlaybackMode> INT_MAP = new HashMap<Integer, PlaybackMode>();

    static {
        for (PlaybackMode event : PlaybackMode.values()) {
            INT_MAP.put(event.intValue, event);
        }
    }

    public static PlaybackMode playbackMode(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    PlaybackMode(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
