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

package uk.co.caprica.vlcj.model;

import uk.co.caprica.vlcj.enums.Meta;

import java.util.Collections;
import java.util.Map;

/**
 * Immutable meta data value object.
 */
public final class MediaMetaData {

    private final Map<Meta,String> values;

    public MediaMetaData(Map<Meta,String> values) {
        this.values = Collections.unmodifiableMap(values);
    }

    public void set(Meta meta, String value) {
        values.put(meta, value);
    }

    public String get(Meta meta) {
        return values.get(meta);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(300);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("values=").append(values).append(']');
        return sb.toString();
   }

}
