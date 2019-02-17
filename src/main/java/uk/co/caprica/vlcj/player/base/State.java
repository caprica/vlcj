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
 * Enumeration of native media/player states.
 */
public enum State {

    NOTHING_SPECIAL(0),
    OPENING        (1),
    BUFFERING      (2), // Deprecated, use  libvlc_MediaPlayerBuffering events instead
    PLAYING        (3),
    PAUSED         (4),
    STOPPED        (5),
    ENDED          (6),
    ERROR          (7);

    private static final Map<Integer, State> INT_MAP = new HashMap<Integer, State>();

    static {
        for (State event : State.values()) {
            INT_MAP.put(event.intValue, event);
        }
    }

    /**
     * Get an enumerated value for a native value.
     *
     * @param intValue native value
     * @return enumerated value
     */
    public static State state(int intValue) {
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
    State(int intValue) {
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
