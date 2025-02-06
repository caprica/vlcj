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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of audio mix modes.
 */
public enum AudioMixMode {

    UNSET(0),
    STEREO(1),
    BINAURAL(2),
    FOUR_ZERO(3),
    FIVE_ONE(4),
    SEVEN_ONE(5);

    private static final Map<Integer, AudioMixMode> INT_MAP = new HashMap<Integer, AudioMixMode>();

    static {
        for (AudioMixMode value : AudioMixMode.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static AudioMixMode audioMixMode(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    AudioMixMode(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
