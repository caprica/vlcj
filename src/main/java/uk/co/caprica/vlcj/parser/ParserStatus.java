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

package uk.co.caprica.vlcj.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of parser statuses.
 */
public enum ParserStatus {

    FAILED   (0),
    TIMEOUT  (1),
    CANCELLED(2),
    DONE     (3);

    private static final Map<Integer, ParserStatus> INT_MAP = new HashMap<Integer, ParserStatus>();

    static {
        for (ParserStatus event : ParserStatus.values()) {
            INT_MAP.put(event.intValue, event);
        }
    }

    public static ParserStatus parserStatus(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    ParserStatus(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
