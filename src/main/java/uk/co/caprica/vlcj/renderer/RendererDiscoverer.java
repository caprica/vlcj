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

package uk.co.caprica.vlcj.renderer;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_renderer_discoverer_t;
import uk.co.caprica.vlcj.renderer.events.RendererDiscovererEventListener;

// The native API doc implies the event handler has to call HOLD if it wants to use the item, but the API doc also
// says the item is valid until you get the same pointer in a deleted callback
// so i think you can probably choose to hold at any time TBH
// we will need to expose the item so it can be set on a mediaplayer

// FIXME this doesn't quite follow the pattern of the others...
//  although i think i can delete all EventService now, so probably move the add/remove listener back to the main class? or a common base class for all these components, e.g. MediaPlayer, Media, MediaListPlayer

public class RendererDiscoverer {

    private final LibVlc libvlc;

    private final libvlc_renderer_discoverer_t discovererInstance;

    private final RendererDiscovererNativeEventManager eventManager;

    public RendererDiscoverer(LibVlc libvlc, libvlc_renderer_discoverer_t discoverer) {
        this.libvlc             = libvlc;
        this.discovererInstance = discoverer;

        this.eventManager = new RendererDiscovererNativeEventManager(libvlc, this);
    }

    public void addRendererDiscovererEventListener(RendererDiscovererEventListener listener) {
        eventManager.addEventListener(listener);
    }

    public void removeRendererDiscovererEventListener(RendererDiscovererEventListener listener) {
        eventManager.removeEventListener(listener);
    }

    public boolean start() {
        return libvlc.libvlc_renderer_discoverer_start(discovererInstance) == 0;
    }

    public void stop() {
        libvlc.libvlc_renderer_discoverer_stop(discovererInstance);
    }

    public libvlc_renderer_discoverer_t rendererDiscovererInstance() {
        return discovererInstance;
    }

    public void release() {
        eventManager.release();
        libvlc.libvlc_renderer_discoverer_release(discovererInstance);
    }

}
