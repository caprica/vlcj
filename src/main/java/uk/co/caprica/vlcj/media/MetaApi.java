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
import uk.co.caprica.vlcj.binding.support.strings.NativeString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_get_meta;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_get_meta_extra;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_get_meta_extra_names;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_meta_extra_names_release;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_save_meta;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_set_meta;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_set_meta_extra;

/**
 * Behaviour pertaining to media metadata.
 */
public final class MetaApi extends BaseApi {

    MetaApi(Media media) {
        super(media);
    }

    /**
     * Get the value for a particular type of metadata.
     *
     * @param meta type of metadata
     * @return meta metadata value
     */
    public String get(Meta meta) {
        return getMetaValue(libvlc_media_get_meta(mediaInstance, meta.intValue()));
    }

    /**
     * Set the value for a particular type of metadata.
     *
     * @param meta type of metadata
     * @param value meta data value
     */
    public void set(Meta meta, String value) {
        libvlc_media_set_meta(mediaInstance, meta.intValue(), value);
    }

    /**
     * Get the names of the available extra metadata fields.
     *
     * @return list of extra metadata field names
     */
    public List<String> getExtraNames() {
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

    /**
     * Get extra meta data.
     *
     * @param name name of the extra metadata field to get
     * @return metadata value, may be <code>NULL</code>
     */
    public String getExtra(String name) {
        return getMetaValue(libvlc_media_get_meta_extra(mediaInstance, name));
    }

    /**
     * Set extra meta data.
     *
     * @param name name of the extra metadata field to set
     */
    public void setExtra(String name, String value) {
        libvlc_media_set_meta_extra(mediaInstance, name, value);
    }

    /**
     * Save the metadata to the underlying media.
     *
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean save() {
        return libvlc_media_save_meta(libvlcInstance, mediaInstance) != 0;
    }

    /**
     * Get all the metadata values as a {@link MetaData} value object.
     * <p>
     * The returned object is immutable and "disconnected" from the underlying media.
     *
     * @return meta data
     */
    public MetaData asMetaData() {
        Map<Meta,String> values = new HashMap<Meta,String>(Meta.values().length);
        for (Meta meta : Meta.values()) {
            String value = get(meta);
            if (value != null) {
                values.put(meta, value);
            }
        }
        List<String> extraNames = getExtraNames();
        Map<String, String> extraMeta = new TreeMap<>();
        for (String extraName : extraNames) {
            String extraValue = getExtra((extraName));
            extraMeta.put(extraName, extraValue);
        }
        return new MetaData(values, extraMeta);
    }

    private String getMetaValue(Pointer pointer) {
        return NativeString.copyAndFreeNativeString(pointer);
    }
}
