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

package uk.co.caprica.vlcj.media.discoverer;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of media discoverer categories.
 */
public enum MediaDiscovererCategory {

    DEVICES   (0), // devices, like portable music player
    LAN       (1), // lan/wan services, like upnp, smb, or sap
    PODCASTS  (2), // podcasts
    LOCAL_DIRS(3); // local directories, like video, music or pictures directories

    private static final Map<Integer, MediaDiscovererCategory> INT_MAP = new HashMap<Integer, MediaDiscovererCategory>();

    static {
        for (MediaDiscovererCategory value : MediaDiscovererCategory.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static MediaDiscovererCategory mediaDiscovererCategory(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    MediaDiscovererCategory(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
