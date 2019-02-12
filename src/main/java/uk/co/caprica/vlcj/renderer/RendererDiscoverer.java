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

public final class RendererDiscoverer {

    protected final LibVlc libvlc;

    protected final libvlc_renderer_discoverer_t discovererInstance;

    private final EventService eventService;
    private final ListService  listService;

    public RendererDiscoverer(LibVlc libvlc, libvlc_renderer_discoverer_t discoverer) {
        this.libvlc             = libvlc;
        this.discovererInstance = discoverer;

        this.eventService = new EventService(this);
        this.listService  = new ListService (this);
    }

    public EventService events() {
        return eventService;
    }

    public ListService list() {
        return listService;
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
        eventService.release();
        listService.release();

        libvlc.libvlc_renderer_discoverer_release(discovererInstance);
    }

}
