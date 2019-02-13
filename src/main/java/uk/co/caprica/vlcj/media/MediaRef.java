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

package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

public final class MediaRef {

    private final LibVlc libvlc;

    private final libvlc_instance_t libvlcInstance;

    private final libvlc_media_t mediaInstance;

    public MediaRef(LibVlc libvlc, libvlc_instance_t libvlcInstance, libvlc_media_t mediaInstance) {
        this.libvlc = libvlc;
        this.libvlcInstance = libvlcInstance;
        this.mediaInstance = mediaInstance;
    }

    public libvlc_media_t mediaInstance() {
        return mediaInstance;
    }

    /**
     * Return a new {@link Media} component for this {@link MediaRef}.
     * <p>
     * The returned media component shares the native media instance with any others that may be created subsequently.
     * <p>
     * The caller <em>must</em> release the returned media when it is of no further use.
     *
     * @return
     */
    public Media newMedia() {
        libvlc.libvlc_media_retain(mediaInstance);
        return new Media(libvlc, libvlcInstance, mediaInstance);
    }

    public MediaRef newMediaRef() {
        libvlc.libvlc_media_retain(mediaInstance);
        return new MediaRef(libvlc, libvlcInstance, mediaInstance);
    }

    public Media duplicateMedia() {
        return new Media(libvlc, libvlcInstance, libvlc.libvlc_media_duplicate(mediaInstance));
    }

    public MediaRef duplicateMediaRef() {
        return new MediaRef(libvlc, libvlcInstance, libvlc.libvlc_media_duplicate(mediaInstance));
    }

    public void release() {
        libvlc.libvlc_media_release(mediaInstance);
    }

}
