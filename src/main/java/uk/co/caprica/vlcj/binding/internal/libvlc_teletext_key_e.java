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
 * Enumeration of teletext keys.
 */
public enum libvlc_teletext_key_e {

    libvlc_teletext_key_red   ('r' << 16),
    libvlc_teletext_key_green ('g' << 16),
    libvlc_teletext_key_yellow('y' << 16),
    libvlc_teletext_key_blue  ('b' << 16),
    libvlc_teletext_key_index ('i' << 16);

    private static final Map<Integer, libvlc_teletext_key_e> INT_MAP = new HashMap<Integer, libvlc_teletext_key_e>();

    static {
        for(libvlc_teletext_key_e key : libvlc_teletext_key_e.values()) {
            INT_MAP.put(key.intValue, key);
        }
    }

    public static libvlc_teletext_key_e key(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    private libvlc_teletext_key_e(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
