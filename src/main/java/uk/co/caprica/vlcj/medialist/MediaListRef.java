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

package uk.co.caprica.vlcj.medialist;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_list_release;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_list_retain;

/**
 * An opaque reference to a media list.
 * <p>
 * This is used to pass around media list references without requiring the full-blown {@link MediaList} component.
 */
public final class MediaListRef {

    /**
     * Native media list instance.
     */
    private final libvlc_media_list_t mediaListInstance;

    /**
     * Create a media list reference.
     * <p>
     * The caller <em>must</em> release this new {@link MediaListRef} when it has no further use for it.
     */
    public MediaListRef(libvlc_media_list_t mediaListInstance) {
        libvlc_media_list_retain(mediaListInstance);
        this.mediaListInstance = mediaListInstance;
    }

    /**
     * Create a new {@link MediaListRef} for this {@link MediaListRef}.
     * <p>
     * The caller <em>must</em> release the new {@link MediaListRef} when it has no further use for it.
     *
     * @return media list reference
     */
    public MediaListRef newMediaListRef() {
        return new MediaListRef(mediaListInstance);
    }

    /**
     * Create a new {@link MediaList} for this {@link MediaListRef}.
     * <p>
     * The caller <em>must</em> release the new {@link MediaList} when it has no further use for it.
     *
     * @return media list
     */
    public MediaList newMediaList() {
        return new MediaList(mediaListInstance);
    }

    /**
     * Release associated native media list instance.
     * <p>
     * This component must no longer be used.
     */
    public void release() {
        libvlc_media_list_release(mediaListInstance);
    }

    /**
     * Get the native media list instance.
     *
     * @return native media list instance
     */
    public libvlc_media_list_t mediaListInstance() {
        return mediaListInstance;
    }
}
