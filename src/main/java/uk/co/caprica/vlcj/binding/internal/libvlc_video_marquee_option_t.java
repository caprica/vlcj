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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.binding.internal;

/**
 * Enumeration of marquee options.
 */
public enum libvlc_video_marquee_option_t {

    libvlc_marquee_Enable  (0),
    libvlc_marquee_Text    (1),        /** string argument */
    libvlc_marquee_Color   (2),
    libvlc_marquee_Opacity (3),
    libvlc_marquee_Position(4),
    libvlc_marquee_Refresh (5),
    libvlc_marquee_Size    (6),
    libvlc_marquee_Timeout (7),
    libvlc_marquee_X       (8),
    libvlc_marquee_Y       (9);

    private final int intValue;

    private libvlc_video_marquee_option_t(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
