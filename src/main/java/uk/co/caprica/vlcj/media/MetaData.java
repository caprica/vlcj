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

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.support.strings.NativeString;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_get_meta;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_get_meta_extra;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_get_meta_extra_names;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_meta_extra_names_release;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_set_meta;

/**
 * Immutable metadata value object.
 */
public final class MetaData {

    /**
     * Collection of metadata values.
     */
    private final Map<Meta, String> values = new HashMap<>(Meta.values().length);

    /**
     * Collection of metadata extra values.
     */
    private final Map<String, String> extraValues = new TreeMap<>();

    /**
     * Create metadata values for the given media.
     *
     * @param media native media instance
     */
    public MetaData(libvlc_media_t media) {
        for (Meta meta : Meta.values()) {
            String value = getMetaValue(libvlc_media_get_meta(media, meta.intValue()));
            if (value != null) {
                values.put(meta, value);
            }
        }
        List<String> extraNames = getExtraNames(media);
        for (String extraName : extraNames) {
            String extraValue = getMetaValue(libvlc_media_get_meta_extra(media, extraName));
            extraValues.put(extraName, extraValue);
        }
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

    /**
     * Set the value for a particular type of metadata.
     * <p>
     * This does <strong>not</strong> save the media with the new meta, see {@link MediaPlayerFactory#meta()}.
     *
     * @param meta type of metadata
     * @param value meta data value
     */
    public void set(Meta meta, String value) {
        values.put(meta, value);
    }

    /**
     * Set the value for a particular type of metadata extra.
     * <p>
     * This does <strong>not</strong> save the media with the new meta, see {@link MediaPlayerFactory#meta()}.
     *
     * @param name type of extra metadata
     * @param value meta data value
     */
    public void set(String name, String value) {
        extraValues.put(name, value);
    }

    private static String getMetaValue(Pointer pointer) {
        return NativeString.copyAndFreeNativeString(pointer);
    }

    private static List<String> getExtraNames(libvlc_media_t mediaInstance) {
        PointerByReference namesPointer = new PointerByReference();
        int namesCount = libvlc_media_get_meta_extra_names(mediaInstance, namesPointer);
        List<String> result = new ArrayList<>(namesCount);
        Pointer[] namePointers = namesPointer.getValue().getPointerArray(0L, namesCount);
        for (Pointer namePointer : namePointers) {
            String name = NativeString.copyNativeString(namePointer);
            result.add(name);
        }
        libvlc_media_meta_extra_names_release(namesPointer.getValue(), namesCount);
        return result;
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
