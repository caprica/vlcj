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
import uk.co.caprica.vlcj.enums.Meta;
import uk.co.caprica.vlcj.binding.NativeString;
import uk.co.caprica.vlcj.model.MetaData;

import java.util.HashMap;
import java.util.Map;

public class MetaService extends BaseService {

    MetaService(Media media) {
        super(media);
    }

    public String get(Meta meta) {
        return getMetaValue(libvlc.libvlc_media_get_meta(mediaInstance, meta.intValue()));
    }

    public void set(Meta meta, String value) {
        libvlc.libvlc_media_set_meta(mediaInstance, meta.intValue(), value);
    }

    public boolean save() {
        return libvlc.libvlc_media_save_meta(mediaInstance) != 0;
    }

    public MetaData asMetaData() {
        Map<Meta,String> values = new HashMap<Meta,String>(26);
        for (Meta meta : Meta.values()) {
            values.put(meta, get(meta));
        }
        return new MetaData(values);
    }

    private String getMetaValue(Pointer pointer) {
        return NativeString.copyAndFreeNativeString(libvlc, pointer);
    }

}
