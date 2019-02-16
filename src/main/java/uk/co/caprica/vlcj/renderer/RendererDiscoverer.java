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

/**
 * Encapsulation of a native renderer discoverer instance.
 */
public final class RendererDiscoverer {

    /**
     * Native library.
     */
    protected final LibVlc libvlc;

    /**
     * Native renderer discoverer instance.
     */
    protected final libvlc_renderer_discoverer_t discovererInstance;

    private final EventService eventService;
    private final ListService  listService;

    public RendererDiscoverer(LibVlc libvlc, libvlc_renderer_discoverer_t discoverer) {
        this.libvlc             = libvlc;
        this.discovererInstance = discoverer;

        this.eventService = new EventService(this);
        this.listService  = new ListService (this);
    }

    /**
     * Behaviour pertaining to events.
     *
     * @return event behaviour
     */
    public EventService events() {
        return eventService;
    }

    /**
     * Behaviour pertaining to the list of discovered renderer items.
     *
     * @return renderer discoverer item list behaviour
     */
    public ListService list() {
        return listService;
    }

    /**
     * Start discovery.
     *
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean start() {
        return libvlc.libvlc_renderer_discoverer_start(discovererInstance) == 0;
    }

    /**
     * Stop discovery.
     */
    public void stop() {
        libvlc.libvlc_renderer_discoverer_stop(discovererInstance);
    }

    /**
     * Get the associated native renderer discoverer instance.
     *
     * @return renderer discoverer instance
     */
    public libvlc_renderer_discoverer_t rendererDiscovererInstance() {
        return discovererInstance;
    }

    /**
     * Release this component and the associated native resources.
     * <p>
     * The component must no longer be used.
     */
    public void release() {
        eventService.release();
        listService.release();

        libvlc.libvlc_renderer_discoverer_release(discovererInstance);
    }

}
