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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.binding.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of media parsed statuses.
 */
public enum libvlc_media_parsed_status_e {

    libvlc_media_parsed_status_skipped(1),
    libvlc_media_parsed_status_failed (2),
    libvlc_media_parsed_status_timeout(3),
    libvlc_media_parsed_status_done   (4);

    private static final Map<Integer, libvlc_media_parsed_status_e> INT_MAP = new HashMap<Integer, libvlc_media_parsed_status_e>();

    static {
        for(libvlc_media_parsed_status_e value : libvlc_media_parsed_status_e.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static libvlc_media_parsed_status_e mediaParsedStatus(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    private libvlc_media_parsed_status_e(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
