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
import uk.co.caprica.vlcj.binding.internal.libvlc_rd_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_renderer_discoverer_t;
import uk.co.caprica.vlcj.binding.support.size_t;
import uk.co.caprica.vlcj.player.renderer.RendererDiscoverer;
import uk.co.caprica.vlcj.player.renderer.RendererDiscovererDescription;

import java.util.ArrayList;
import java.util.List;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_renderer_discoverer_list_get;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_renderer_discoverer_list_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_renderer_discoverer_new;

/**
 * Behaviour pertaining to renderer discovery.
 */
public final class RendererApi extends BaseApi {

    RendererApi(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Get the list of renderer discoverer descriptions.
     *
     * @return descriptions, will not be <code>null</code>
     */
    public List<RendererDiscovererDescription> discoverers() {
        PointerByReference ref = new PointerByReference();
        size_t size = libvlc_renderer_discoverer_list_get(libvlcInstance, ref);
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
            libvlc_renderer_discoverer_list_release(ref.getValue(), size);
        }
    }

    /**
     * Get a named renderer discoverer.
     * <p>
     * Use {@link #discoverers()} to get the name.
     *
     * @param name discoverer name
     * @return discoverer, may be <code>null</code>
     */
    public RendererDiscoverer discoverer(String name) {
        libvlc_renderer_discoverer_t discoverer = libvlc_renderer_discoverer_new(libvlcInstance, name);
        if (discoverer != null) {
            return new RendererDiscoverer(discoverer);
        } else {
            return null;
        }
    }

}
