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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Immutable meta data value object.
 */
public final class MetaData {

    /**
     * Collection of meta data values.
     */
    private final Map<Meta,String> values;

    /**
     * Create a meta data value object.
     *
     * @param values meta data values
     */
    public MetaData(Map<Meta,String> values) {
        this.values = Collections.unmodifiableMap(values);
    }

    /**
     * Get a particular meta data value.
     *
     * @param meta meta data type
     * @return value meta data value
     */
    public String get(Meta meta) {
        return values.get(meta);
    }

    /**
     * Get all of the meta data values.
     *
     * @return copy of the meta data values collection
     */
    public Map<Meta,String> values() {
        return new HashMap<Meta,String>(values);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(300);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("values=").append(values).append(']');
        return sb.toString();
   }

}
