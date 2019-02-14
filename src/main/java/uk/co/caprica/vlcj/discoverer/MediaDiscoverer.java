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

package uk.co.caprica.vlcj.discoverer;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.*;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListRef;

/**
 * Media discoverer component.
 */
public final class MediaDiscoverer {

    /**
     * Native library.
     */
    private final LibVlc libvlc;

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
     * @param libvlc native library
     * @param libvlcInstance native library instance
     * @param discovererInstance native media discoverer instance
     * @return media discoverer
     */
    MediaDiscoverer(LibVlc libvlc, libvlc_instance_t libvlcInstance, libvlc_media_discoverer_t discovererInstance) {
        this.libvlc = libvlc;
        this.discoverer = discovererInstance;
        this.mediaList = new MediaList(libvlc, libvlcInstance, libvlc.libvlc_media_discoverer_media_list(discovererInstance));
    }

    /**
     * Start media discovery.
     *
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean start() {
        return libvlc.libvlc_media_discoverer_start(discoverer) == 0;
    }

    /**
     * Stop media discovery.
     */
    public void stop() {
        libvlc.libvlc_media_discoverer_stop(discoverer);
    }

    /**
     * Is media discovery running?
     *
     * @return <code>true</code> if discovery is running; <code>false</code> if it is not
     */
    public boolean isRunning() {
        return libvlc.libvlc_media_discoverer_is_running(discoverer) != 0;
    }

    /**
     * Get the discovered media list.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaList} when it has no further use for it.
     *
     * @return media list
     */
    public MediaList newNediaList() {
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
        libvlc.libvlc_media_discoverer_release(discoverer);
    }

}
