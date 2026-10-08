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

package uk.co.caprica.vlcj.media.discoverer;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_discoverer_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListRef;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_discoverer_destroy;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_discoverer_is_running;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_discoverer_new;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_discoverer_start;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_discoverer_stop;

/**
 * Media discoverer component.
 */
public final class MediaDiscoverer {

    /**
     * Callback handler for native events.
     */
    final MediaDiscovererCallbackHandler callbackHandler;

    /**
     * Native media discoverer instance.
     */
    private final libvlc_media_discoverer_t discovererInstance;


    /**
     * Event API.
     */
    private final EventApi eventApi;

    /**
     * Create a media discoverer
     *
     * @param libvlcInstance native library instance
     * @param name native media discoverer name
     */
    public MediaDiscoverer(libvlc_instance_t libvlcInstance, String name) {
        this.callbackHandler = new MediaDiscovererCallbackHandler(this);
        this.discovererInstance = libvlc_media_discoverer_new(libvlcInstance, name, callbackHandler.callbacks(), null);

        this.eventApi = new EventApi(this);
    }

    /**
     * Start media discovery.
     *
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean start() {
        return libvlc_media_discoverer_start(discovererInstance) == 0;
    }

    /**
     * Stop media discovery.
     */
    public void stop() {
        libvlc_media_discoverer_stop(discovererInstance);
    }

    /**
     * Is media discovery running?
     *
     * @return <code>true</code> if discovery is running; <code>false</code> if it is not
     */
    public boolean isRunning() {
        return libvlc_media_discoverer_is_running(discovererInstance) != 0;
    }

    /**
     * Get the events API.
     *
     * @return events API
     */
    public EventApi events() {
        return eventApi;
    }

    /**
     * Release the media discoverer and any associated native resources.
     */
    public void release() {
        eventApi.release();
        libvlc_media_discoverer_destroy(discovererInstance);
    }
}
