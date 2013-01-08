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
 * Enumeration of track types.
 */
public enum libvlc_track_type_t {

    libvlc_track_unknown(-1),
    libvlc_track_audio  ( 0),
    libvlc_track_video  ( 1),
    libvlc_track_text   ( 2);

    private final int intValue;

    private libvlc_track_type_t(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

    public static libvlc_track_type_t valueOf(int intValue) {
        for(libvlc_track_type_t type : values()) {
            if(type.intValue == intValue) {
                return type;
            }
        }
        throw new IllegalArgumentException("No such value " + intValue);
    }
}
