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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of media meta data types.
 */
public enum Meta {

    TITLE       ( 0),
    ARTIST      ( 1),
    GENRE       ( 2),
    COPYRIGHT   ( 3),
    ALBUM       ( 4),
    TRACK_NUMBER( 5),
    DESCRIPTION ( 6),
    RATING      ( 7),
    DATE        ( 8),
    SETTING     ( 9),
    URL         (10),
    LANGUAGE    (11),
    NOW_PLAYING (12),
    PUBLISHER   (13),
    ENCODED_BY  (14),
    ARTWORK_URL (15),
    TRACK_ID    (16),
    TRACK_TOTAL (17),
    DIRECTOR    (18),
    SEASON      (19),
    EPISODE     (20),
    SHOW_NAME   (21),
    ACTORS      (22),
    ALBUM_ARTIST(23),
    DISC_NUMBER (24),
    DISC_TOTAL  (25);

    private static final Map<Integer, Meta> INT_MAP = new HashMap<Integer, Meta>();

    static {
        for (Meta value : Meta.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static Meta meta(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    Meta(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
