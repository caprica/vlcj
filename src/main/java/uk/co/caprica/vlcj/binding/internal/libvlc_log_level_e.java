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

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of native log levels.
 */
public enum libvlc_log_level_e {

    DEBUG  (0), // Debug message
    NOTICE (2), // Important informational message
    WARNING(3), // Warning (potential error) message
    ERROR  (4); // Error message

    private static final Map<Integer, libvlc_log_level_e> INT_MAP = new HashMap<Integer, libvlc_log_level_e>();

    static {
        for(libvlc_log_level_e event : libvlc_log_level_e.values()) {
            INT_MAP.put(event.intValue, event);
        }
    }

    public static libvlc_log_level_e level(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    private libvlc_log_level_e(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
