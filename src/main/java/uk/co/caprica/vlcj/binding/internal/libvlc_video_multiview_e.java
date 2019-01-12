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
 * Enumeration of video multiview.
 */
public enum libvlc_video_multiview_e {

    libvlc_video_multiview_2d(0),                  /**< No stereoscopy: 2D picture. */
    libvlc_video_multiview_stereo_sbs(1),          /**< Side-by-side */
    libvlc_video_multiview_stereo_tb(2),           /**< Top-bottom */
    libvlc_video_multiview_stereo_row(3),          /**< Row sequential */
    libvlc_video_multiview_stereo_col(4),          /**< Column sequential */
    libvlc_video_multiview_stereo_frame(5),        /**< Frame sequential */
    libvlc_video_multiview_stereo_checkerboard(6); /**< Checkerboard pattern */

    private static final Map<Integer, libvlc_video_multiview_e> INT_MAP = new HashMap<Integer, libvlc_video_multiview_e>();

    static {
        for(libvlc_video_multiview_e multiview : libvlc_video_multiview_e.values()) {
            INT_MAP.put(multiview.intValue, multiview);
        }
    }

    public static libvlc_video_multiview_e multiview(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    private libvlc_video_multiview_e(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
