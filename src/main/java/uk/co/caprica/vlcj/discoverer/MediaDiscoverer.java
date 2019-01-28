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

public class MediaDiscoverer {

    private final LibVlc libvlc;

    private final libvlc_media_discoverer_t discoverer;

    private final MediaList mediaList;

    public MediaDiscoverer(LibVlc libvlc, libvlc_media_discoverer_t discoverer) {
        this.libvlc = libvlc;
        this.discoverer = discoverer;
        this.mediaList = new MediaList(libvlc, libvlc.libvlc_media_discoverer_media_list(discoverer));
    }

    public boolean start() {
        return libvlc.libvlc_media_discoverer_start(discoverer) == 0;
    }

    public void stop() {
        libvlc.libvlc_media_discoverer_stop(discoverer);
    }

    public boolean isRunning() {
        return libvlc.libvlc_media_discoverer_is_running(discoverer) != 0;
    }

    public MediaList mediaList() {
        return mediaList;
    }

    public void release() {
        libvlc.libvlc_media_discoverer_release(discoverer);
    }

}
