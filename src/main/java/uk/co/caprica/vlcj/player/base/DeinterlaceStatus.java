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

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of deinterlace statuses.
 */
public enum DeinterlaceStatus {

    /**
     * Automatically selected.
     */
    AUTOMATIC(-1),

    /**
     * Forced disabled.
     */
    DISABLED(0),

    /**
     * Forced enabled.
     */
    ENABLED(1);

    private static final Map<Integer, DeinterlaceStatus> INT_MAP = new HashMap<Integer, DeinterlaceStatus>();

    static {
        for (DeinterlaceStatus value : DeinterlaceStatus.values()) {
            INT_MAP.put(value.status, value);
        }
    }

    public static DeinterlaceStatus deinterlaceStatus(int intValue) {
        return INT_MAP.get(intValue);
    }

    /**
     * Native mode value.
     */
    private final int status;

    /**
     * Create an enumerated value.
     *
     * @param status native status value
     */
    DeinterlaceStatus(int status) {
        this.status = status;
    }

    /**
     * Get the native mode value.
     *
     * @return mode value
     */
    public final int intValue() {
        return status;
    }

}
