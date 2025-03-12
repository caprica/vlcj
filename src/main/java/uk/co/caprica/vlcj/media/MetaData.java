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

package uk.co.caprica.vlcj.media;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Immutable metadata value object.
 */
public final class MetaData {

    /**
     * Collection of metadata values.
     */
    private final Map<Meta, String> values;

    /**
     * Collection of metadata extra values.
     */
    private final Map<String, String> extraValues;

    /**
     * Create a metadata value object.
     *
     * @param values metadata values
     * @param extraValues metadata extra values
     */
    public MetaData(Map<Meta, String> values, Map<String, String> extraValues) {
        this.values = Collections.unmodifiableMap(values);
        this.extraValues = Collections.unmodifiableMap(extraValues);
    }

    /**
     * Get a particular metadata value.
     *
     * @param meta metadata type
     * @return value metadata value
     */
    public String get(Meta meta) {
        return values.get(meta);
    }

    /**
     * Get a particular metadata extra value.
     *
     * @param name metadata extra name
     * @return value metadata extra value
     */
    public String get(String name) {
        return extraValues.get(name);
    }

    /**
     * Get all the metadata values.
     *
     * @return copy of the metadata values collection
     */
    public Map<Meta, String> values() {
        return new HashMap<>(values);
    }

    /**
     * Get all the metadata extra values.
     *
     * @return copy of the metadata exta values collection
     */
    public Map<String, String> extraValues() {
        return new HashMap<>(extraValues);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(300);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("values=").append(values).append(',');
        sb.append("extraValues").append(extraValues).append(']');
        return sb.toString();
   }

}
