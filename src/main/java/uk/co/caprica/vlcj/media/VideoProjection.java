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
 * Enumeration of video projections.
 */
public enum VideoProjection {

    RECTANGULAR            (0),
    EQUIRECTANGULAR        (1),     /* 360 spherical */
    CUBEMAP_LAYOUT_STANDARD(0x100);

    private static final Map<Integer, VideoProjection> INT_MAP = new HashMap<Integer, VideoProjection>();

    static {
        for (VideoProjection projection : VideoProjection.values()) {
            INT_MAP.put(projection.intValue, projection);
        }
    }

    public static VideoProjection videoProjection(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    VideoProjection(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
