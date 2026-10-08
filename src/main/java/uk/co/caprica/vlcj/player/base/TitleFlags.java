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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enumeration of title flags.
 */
public enum TitleFlags {

    MENU(1),
    INTERACTIVE(2);

    private static final Map<Integer, TitleFlags> INT_MAP = new HashMap<Integer, TitleFlags>();

    static {
        for (TitleFlags event : TitleFlags.values()) {
            INT_MAP.put(event.intValue, event);
        }
    }

    /**
     * Get an enumerated value for a native value.
     *
     * @param intValue native value
     * @return enumerated value
     */
    public static EnumSet<TitleFlags> titleFlags(int intValue) {
        return Arrays.stream(values())
            .filter(f -> (intValue & f.intValue()) != 0)
            .collect(Collectors.toCollection(() -> EnumSet.noneOf(TitleFlags.class)));
    }

    /**
     * Native value.
     */
    private final int intValue;

    /**
     * Create an enumerated value.
     *
     * @param intValue native value
     */
    TitleFlags(int intValue) {
        this.intValue = intValue;
    }

    /**
     * Get the native value.
     *
     * @return value
     */
    public int intValue() {
        return intValue;
    }

}
