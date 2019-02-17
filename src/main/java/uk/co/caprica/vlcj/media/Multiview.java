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
 * Enumeration of video multiview types.
 */
public enum Multiview {

    TWO_D       (0), /** No stereoscopy: 2D picture. */
    STEREO_SBS  (1), /** Side-by-side */
    STEREO_TB   (2), /** Top-bottom */
    STEREO_ROW  (3), /** Row sequential */
    STEREO_COL  (4), /** Column sequential */
    STEREO_FRAME(5), /** Frame sequential */
    CHECKERBOARD(6); /** Checkerboard pattern */

    private static final Map<Integer, Multiview> INT_MAP = new HashMap<Integer, Multiview>();

    static {
        for (Multiview multiview : Multiview.values()) {
            INT_MAP.put(multiview.intValue, multiview);
        }
    }

    public static Multiview multiview(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    Multiview(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
