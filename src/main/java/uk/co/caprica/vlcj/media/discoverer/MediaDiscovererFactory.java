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
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media.discoverer;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_discoverer_t;

/**
 * Factory to create {@link MediaDiscoverer} instances.
 * <p>
 * <em>This factory is <strong>not</strong> intended for use by client applications.</em>
 */
public final class MediaDiscovererFactory {

    /**
     * Create a media discoverer
     *
     * @param libvlcInstance native library instance
     * @param discovererInstance native media discoverer instance
     * @return media discoverer
     */
    public static MediaDiscoverer newMediaDiscoverer(libvlc_instance_t libvlcInstance, libvlc_media_discoverer_t discovererInstance) {
        return new MediaDiscoverer(libvlcInstance, discovererInstance);
    }

}
