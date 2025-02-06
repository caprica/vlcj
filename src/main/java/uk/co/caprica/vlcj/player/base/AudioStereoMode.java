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
 * Enumeration of audio stereo modes.
 * <p>
 * <code>HEADPHONES</code> is no longer valid, use {@link AudioMixMode#BINAURAL} instead.
 */
public enum AudioStereoMode {

    UNSET     ( 0),
    STEREO    ( 1),
    RSTEREO   ( 2),
    LEFT      ( 3),
    RIGHT     ( 4),
    DOLBYS    ( 5),
    MONO      ( 7);

    private static final Map<Integer, AudioStereoMode> INT_MAP = new HashMap<Integer, AudioStereoMode>();

    static {
        for (AudioStereoMode value : AudioStereoMode.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static AudioStereoMode audioStereoMode(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    AudioStereoMode(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
