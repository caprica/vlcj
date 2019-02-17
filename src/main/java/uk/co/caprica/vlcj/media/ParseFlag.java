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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media;

import java.util.HashMap;
import java.util.Map;

/**
 * Parse flags used by libvlc_media_parse_with_options().
 */
public enum ParseFlag {

    /**
     * Parse media if it's a local file
     */
    PARSE_LOCAL(0x00),

    /**
     * Parse media even if it's a network file
     */
    PARSE_NETWORK(0x01),

    /**
     * Fetch meta and covert art using local resources
     */
    FETCH_LOCAL(0x02),

    /**
     * Fetch meta and covert art using network resources
     */
    FETCH_NETWORK(0x04),

    /**
     * Interact with the user (via libvlc_dialog_cbs) when preparsing this item
     * (and not its sub items). Set this flag in order to receive a callback
     * when the input is asking for credentials.
     */
    DO_INTERACT(0x08);

    private static final Map<Integer, ParseFlag> INT_MAP = new HashMap<Integer, ParseFlag>();

    static {
        for (ParseFlag value : ParseFlag.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static ParseFlag parseFlag(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    ParseFlag(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
