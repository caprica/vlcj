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
 * Copyright 2009-2017 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of video orientations.
 */
public enum VideoOrientation {

    TOP_LEFT    (0),   /* Normal. Top line represents top, left column left. */
    TOP_RIGHT   (1),   /* Flipped horizontally */
    BOTTOM_LEFT (2),   /* Flipped vertically */
    BOTTOM_RIGHT(3),   /* Rotated 180 degrees */
    LEFT_TOP    (4),   /* Transposed */
    LEFT_BOTTOM (5),   /* Rotated 90 degrees clockwise (or 270 anti-clockwise) */
    RIGHT_TOP   (6),   /* Rotated 90 degrees anti-clockwise */
    RIGHT_BOTTOM(7);   /* Anti-transposed */

    private static final Map<Integer, VideoOrientation> INT_MAP = new HashMap<Integer, VideoOrientation>();

    static {
        for (VideoOrientation orientation : VideoOrientation.values()) {
            INT_MAP.put(orientation.intValue, orientation);
        }
    }

    public static VideoOrientation videoOrientation(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    VideoOrientation(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
