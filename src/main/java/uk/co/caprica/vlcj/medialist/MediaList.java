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

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_retain;

/**
 * A media list.
 */
public final class MediaList {

    /**
     * Native library instance.
     */
    protected final libvlc_instance_t libvlcInstance;

    /**
     * Native media list instance.
     */
    protected final libvlc_media_list_t mediaListInstance;

    private final MediaApi itemApi;
    private final EventApi eventApi;

    /**
     * Create a new media list.
     *
     * @param mediaListInstance native media list, the caller must not release this opaque handle, it will be released by this component when it is no longer needed
     */
    public MediaList(libvlc_instance_t libvlcInstance, libvlc_media_list_t mediaListInstance) {
        this.libvlcInstance    = libvlcInstance;
        this.mediaListInstance = mediaListInstance;

        this.eventApi = new EventApi(this);
        this.itemApi  = new MediaApi(this);
    }

    /**
     * Behaviour pertaining to media list events.
     *
     * @return events behaviour
     */
    public EventApi events() {
        return eventApi;
    }

    /**
     * Behaviour pertaining to the items in the media list.
     *
     * @return list item behaviour
     */
    public MediaApi media() {
        return itemApi;
    }

    /**
     * Get the associated native media list instance
     *
     * @return media list instance
     */
    public libvlc_media_list_t mediaListInstance() {
        return mediaListInstance;
    }

    /**
     * Create a new {@link MediaListRef} for this media list.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaListRef} when it has no further use for it.
     *
     * @return media list reference
     */
    public MediaListRef newMediaListRef() {
        libvlc_media_list_retain(mediaListInstance);
        return new MediaListRef(libvlcInstance, mediaListInstance);
    }

    /**
     * Create a new {@link MediaList} for this media list.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaList} when it has no further use for it.
     *
     * @return media list
     */
    public MediaList newMediaList() {
        libvlc_media_list_retain(mediaListInstance);
        return new MediaList(libvlcInstance, mediaListInstance);
    }

    /**
     * Release this component and the associated native resources.
     * <p>
     * The component must no longer be used.
     */
    public void release() {
        eventApi.release();
        itemApi .release();

        libvlc_media_list_release(mediaListInstance);
    }

}
