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

package uk.co.caprica.vlcj.player.renderer;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_renderer_discoverer_t;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_renderer_discoverer_destroy;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_renderer_discoverer_new;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_renderer_discoverer_start;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_renderer_discoverer_stop;

/**
 * Encapsulation of a native renderer discoverer instance.
 */
public final class RendererDiscoverer {

    /**
     * Callback handler for native events.
     */
    final RendererDiscovererCallbackHandler callbackHandler;

    /**
     * Native renderer discoverer instance.
     */
    private final libvlc_renderer_discoverer_t discovererInstance;

    private final EventApi eventApi;
    private final ListApi  listApi;

    public RendererDiscoverer(libvlc_instance_t instance, String name) {
        this.callbackHandler = new RendererDiscovererCallbackHandler(this);
        this.discovererInstance = libvlc_renderer_discoverer_new(instance, name, callbackHandler.callbacks(), null);

        this.eventApi = new EventApi(this);
        this.listApi  = new ListApi(this);
    }

    /**
     * Behaviour pertaining to events.
     *
     * @return event behaviour
     */
    public EventApi events() {
        return eventApi;
    }

    /**
     * Behaviour pertaining to the list of discovered renderer items.
     *
     * @return renderer discoverer item list behaviour
     */
    public ListApi list() {
        return listApi;
    }

    /**
     * Start discovery.
     *
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean start() {
        return libvlc_renderer_discoverer_start(discovererInstance) == 0;
    }

    /**
     * Stop discovery.
     */
    public void stop() {
        libvlc_renderer_discoverer_stop(discovererInstance);
    }

    /**
     * Release this component and the associated native resources.
     * <p>
     * The component must no longer be used.
     */
    public void release() {
        eventApi.release();
        listApi .release();

        libvlc_renderer_discoverer_destroy(discovererInstance);
    }
}
