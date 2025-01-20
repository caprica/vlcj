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
 * Enumeration of video stereo modes.
 */
public enum VideoStereoMode {

    AUTO        ( 0),
    STEREO      ( 1),
    LEFT_EYE    ( 2),
    RIGHT_EYE   ( 3),
    SIDE_BY_SIDE( 4);

    private static final Map<Integer, VideoStereoMode> INT_MAP = new HashMap<Integer, VideoStereoMode>();

    static {
        for (VideoStereoMode value : VideoStereoMode.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static VideoStereoMode videoStereoMode(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    VideoStereoMode(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
