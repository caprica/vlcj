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

package uk.co.caprica.vlcj.media.discoverer;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_discoverer_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListRef;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_discoverer_is_running;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_discoverer_media_list;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_discoverer_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_discoverer_start;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_discoverer_stop;

/**
 * Media discoverer component.
 */
public final class MediaDiscoverer {

    /**
     * Native discoverer instance.
     */
    private final libvlc_media_discoverer_t discoverer;

    /**
     * List of discovered media.
     */
    private final MediaList mediaList;

    /**
     * Create a media discoverer
     *
     * @param libvlcInstance native library instance
     * @param discovererInstance native media discoverer instance
     * @return media discoverer
     */
    MediaDiscoverer(libvlc_instance_t libvlcInstance, libvlc_media_discoverer_t discovererInstance) {
        this.discoverer = discovererInstance;
        this.mediaList = new MediaList(libvlcInstance, libvlc_media_discoverer_media_list(discovererInstance));
    }

    /**
     * Start media discovery.
     *
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean start() {
        return libvlc_media_discoverer_start(discoverer) == 0;
    }

    /**
     * Stop media discovery.
     */
    public void stop() {
        libvlc_media_discoverer_stop(discoverer);
    }

    /**
     * Is media discovery running?
     *
     * @return <code>true</code> if discovery is running; <code>false</code> if it is not
     */
    public boolean isRunning() {
        return libvlc_media_discoverer_is_running(discoverer) != 0;
    }

    /**
     * Get the discovered media list.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaList} when it has no further use for it.
     *
     * @return media list
     */
    public MediaList newMediaList() {
        return mediaList.newMediaList();
    }

    /**
     * Get the discovered media list as a {@link MediaListRef}.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaList} when it has no further use for it.
     *
     * @return media list
     */
    public MediaListRef newNediaListRef() {
        return mediaList.newMediaListRef();
    }

    /**
     * Release the media discoverer and any associated native resources.
     */
    public void release() {
        mediaList.release();
        libvlc_media_discoverer_release(discoverer);
    }

}
