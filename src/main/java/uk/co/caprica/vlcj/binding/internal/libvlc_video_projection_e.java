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

package uk.co.caprica.vlcj.binding.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of video projections.
 */
public enum libvlc_video_projection_e {

    libvlc_video_projection_rectangular            (0),
    libvlc_video_projection_equirectangular        (1),     /* 360 spherical */
    libvlc_video_projection_cubemap_layout_standard(0x100);

    private static final Map<Integer, libvlc_video_projection_e> INT_MAP = new HashMap<Integer, libvlc_video_projection_e>();

    static {
        for(libvlc_video_projection_e projection : libvlc_video_projection_e.values()) {
            INT_MAP.put(projection.intValue, projection);
        }
    }

    public static libvlc_video_projection_e projection(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    private libvlc_video_projection_e(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
