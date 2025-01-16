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

package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_thumbnail_request_t;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_thumbnail_request_destroy;

/**
 * Encapsulation of a thumbnail request.
 * <p>
 * The internal implementation is opaque to client applications.
 */
public final class ThumbnailRequest {

    private final libvlc_media_thumbnail_request_t request;

    ThumbnailRequest(libvlc_media_thumbnail_request_t request) {
        this.request = request;
    }

    /**
     * Cancel this thumbnail request.
     * <p>
     * This is effectively the same as calling {@link #release()}.
     */
    public void cancel() {
        libvlc_media_thumbnail_request_destroy(request);
    }

    /**
     * Release this thumbnail request.
     * <p>
     * The thumbnail request must be released when it is no longer needed.
     */
    public void release() {
        libvlc_media_thumbnail_request_destroy(request);
    }

}
