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

package uk.co.caprica.vlcj.factory;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.vlcj.binding.internal.libvlc_chapter_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_rd_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_renderer_discoverer_t;
import uk.co.caprica.vlcj.binding.support.size_t;
import uk.co.caprica.vlcj.renderer.RendererDiscoverer;
import uk.co.caprica.vlcj.renderer.RendererDiscovererDescription;

import java.util.ArrayList;
import java.util.List;

public final class RendererService extends BaseService {

    RendererService(MediaPlayerFactory factory) {
        super(factory);
    }

    public List<RendererDiscovererDescription> discoverers() {
        PointerByReference ref = new PointerByReference();
        size_t size = libvlc.libvlc_renderer_discoverer_list_get(instance, ref);
        try {
            int count = size.intValue();
            List<RendererDiscovererDescription> result = new ArrayList<RendererDiscovererDescription>(count);
            if (count > 0) {
                Pointer[] pointers = ref.getValue().getPointerArray(0, count);
                for (Pointer pointer : pointers) {
                    libvlc_rd_description_t description = Structure.newInstance(libvlc_rd_description_t.class, pointer);
                    description.read();
                    result.add(new RendererDiscovererDescription(description.psz_name, description.psz_longname));
                }
            }
            return result;
        }
        finally {
            libvlc.libvlc_renderer_discoverer_list_release(ref.getValue(), size);
        }
    }

    public RendererDiscoverer discoverer(String name) {
        libvlc_renderer_discoverer_t discoverer = libvlc.libvlc_renderer_discoverer_new(instance, name);
        if (discoverer != null) {
            return new RendererDiscoverer(libvlc, discoverer);
        } else {
            return null;
        }
    }

}
