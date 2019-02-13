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

package uk.co.caprica.vlcj.medialist;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;

public final class MediaListRef {

    private final LibVlc libvlc;

    private final libvlc_instance_t libvlcInstance;

    private final libvlc_media_list_t mediaListInstance;

    public MediaListRef(LibVlc libvlc, libvlc_instance_t libvlcInstance, libvlc_media_list_t mediaListInstance) {
        this.libvlc = libvlc;
        this.libvlcInstance = libvlcInstance;
        this.mediaListInstance = mediaListInstance;
    }

    public libvlc_media_list_t mediaListInstance() {
        return mediaListInstance;
    }

    /**
     *
     * <p>
     * The caller <em>must</em> release the returned {@link MediaList} when it has no further use for it.
     *
     * @return
     */
    public MediaList newMediaList() {
        libvlc.libvlc_media_list_retain(mediaListInstance);
        return new MediaList(libvlc, libvlcInstance, mediaListInstance);
    }

    public MediaListRef newMediaListRef() {
        libvlc.libvlc_media_list_retain(mediaListInstance);
        return new MediaListRef(libvlc, libvlcInstance, mediaListInstance);
    }

    public void release() {
        libvlc.libvlc_media_list_release(mediaListInstance);
    }

}
