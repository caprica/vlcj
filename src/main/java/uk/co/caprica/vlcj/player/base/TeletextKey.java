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

package uk.co.caprica.vlcj.player.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of teletext keys.
 */
public enum TeletextKey {

    RED   ('r' << 16),
    GREEN ('g' << 16),
    YELLOW('y' << 16),
    BLUE  ('b' << 16),
    INDEX ('i' << 16);

    private static final Map<Integer, TeletextKey> INT_MAP = new HashMap<Integer, TeletextKey>();

    static {
        for (TeletextKey key : TeletextKey.values()) {
            INT_MAP.put(key.intValue, key);
        }
    }

    public static TeletextKey key(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    TeletextKey(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
