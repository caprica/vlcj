/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of media option flags.
 */
public enum OptionFlag {

    TRUSTED(0X2),
    UNIQUE (0X100);

    private static final Map<Integer, OptionFlag> INT_MAP = new HashMap<Integer, OptionFlag>();

    static {
        for (OptionFlag value : OptionFlag.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static OptionFlag optionFlag(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    OptionFlag(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
