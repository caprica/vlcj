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
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;

// FIXME developer note - when you do insert, or remove, LibVLC is doing retain and release on the media instance, so we don't need to. we need to really nail down when retain and release are needed
// FIXME should this be done with services etc like Media ? yes, not currently sure about what has gone where

public final class MediaList {

    /**
     * Native library.
     */
    protected final LibVlc libvlc;

    /**
     * Native media list instance.
     */
    protected final libvlc_media_list_t mediaList;

    private final ItemService  itemService;
    private final EventService eventService;

    /**
     * Create a new media list.
     *
     * @param libvlc
     * @param mediaListInstance native media list, the caller must not release this opaque handle, it will be released by this component when it is no longer needed
     */
    public MediaList(LibVlc libvlc, libvlc_media_list_t mediaListInstance) {
        this.libvlc = libvlc;
        this.mediaList = mediaListInstance;

        this.eventService = new EventService(this);
        this.itemService  = new ItemService (this);
    }

    public EventService events() {
        return eventService;
    }

    public ItemService items() {
        return itemService;
    }

    public libvlc_media_list_t mediaListInstance() {
        return mediaList;
    }

    public void release() {
        eventService.release();
        itemService .release();

        libvlc.libvlc_media_list_release(mediaList);
    }

}
