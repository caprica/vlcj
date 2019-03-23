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

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.NativeString;

import java.util.HashMap;
import java.util.Map;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_get_meta;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_save_meta;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_set_meta;

/**
 * Behaviour pertaining to media meta data.
 */
public final class MetaApi extends BaseApi {

    MetaApi(Media media) {
        super(media);
    }

    /**
     * Get the value for a particular type of meta data.
     *
     * @param meta type of meta data
     * @return meta data value
     */
    public String get(Meta meta) {
        return getMetaValue(libvlc_media_get_meta(mediaInstance, meta.intValue()));
    }

    /**
     * Set the value for a particular type of meta data.
     *
     * @param meta type of meta data
     * @param value meta data value
     */
    public void set(Meta meta, String value) {
        libvlc_media_set_meta(mediaInstance, meta.intValue(), value);
    }

    /**
     * Save the meta data to the underlying media.
     *
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean save() {
        return libvlc_media_save_meta(mediaInstance) != 0;
    }

    /**
     * Get all of the meta data values as a {@link MetaData} value lobject.
     * <p>
     * The returned object is immutable and "disconnected" from the underlying media.
     *
     * @return meta data
     */
    public MetaData asMetaData() {
        Map<Meta,String> values = new HashMap<Meta,String>(26);
        for (Meta meta : Meta.values()) {
            String value = get(meta);
            if (value != null) {
                values.put(meta, value);
            }
        }
        return new MetaData(values);
    }

    private String getMetaValue(Pointer pointer) {
        return NativeString.copyAndFreeNativeString(pointer);
    }

}
