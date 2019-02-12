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

package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.medialist.EventService;
import uk.co.caprica.vlcj.medialist.ItemService;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListFactory;
import uk.co.caprica.vlcj.model.MediaListRef;

public final class ListService extends BaseService {

    /**
     * Current media list.
     * <p>
     * It is an important principle that this component "owns" this {@link MediaList} instance and that the instance is
     * never exposed directly to client applications. The instance will be freed each time a new media list is set.
     * <p>
     * Various aspects of the media list are exposed to clients by methods here that delegate to the corresponding
     * methods on the media list instance.
     * <p>
     * This media list instance may be <code>null</code>.
     * <p>
     * A client application must therefore be prepared to handle the case where these delegating methods actually return
     * <code>null</code> - either by testing the return value from those methods, or by checking the result of
     * {@link #isValid()} beforehand.
     */
    private MediaList mediaList;

    ListService(DefaultMediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    @Deprecated
    public void setMediaList(MediaList mediaList) {
        libvlc.libvlc_media_list_player_set_media_list(mediaListPlayerInstance, mediaList.mediaListInstance());
        this.mediaList = mediaList;
    }

    /**
     * Set a new media list.
     * <p>
     * The supplied {@link MediaListRef} <em>must</em> be released by the caller when it no longer has any use for it.
     *
     * @param mediaListRef media list
     */
    public void setMediaList(MediaListRef mediaListRef) {
        if (this.mediaList != null) {
            this.mediaList.release();
        }
        this.mediaList = MediaListFactory.newMediaList(libvlc, mediaListRef);
        // FIXME if we're going to restore listeners, it must go here, before the next call
        libvlc.libvlc_media_list_player_set_media_list(mediaListPlayerInstance, mediaListRef.mediaListInstance());
    }

    public boolean isValid() {
        return mediaList != null;
    }

    public EventService events() {
        return mediaList != null ? mediaList.events() : null;
    }

    public ItemService items() {
        return mediaList != null ? mediaList.items() : null;
    }

    @Override
    protected void release() {
        if (mediaList != null) {
            mediaList.release();
        }
    }

}
