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

import uk.co.caprica.vlcj.medialist.EventApi;
import uk.co.caprica.vlcj.medialist.MediaApi;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListRef;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_set_media_list;

/**
 * Behaviour pertaining to the list of media items.
 */
public final class ListApi extends BaseApi {

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

    ListApi(MediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    /**
     * Set a new media list.
     * <p>
     * The supplied {@link MediaListRef} is not kept by this component and <em>must</em> be released by the caller when
     * the caller no longer has any use for it.
     *
     * @param mediaListRef media list
     */
    public void setMediaList(MediaListRef mediaListRef) {
        if (this.mediaList != null) {
            this.mediaList.release();
        }
        this.mediaList = mediaListRef.newMediaList();
        libvlc_media_list_player_set_media_list(mediaListPlayerInstance, mediaListRef.mediaListInstance());
    }

    /**
     * Is the media list valid?
     * <p>
     * This method could be used to check there is a list before using the various behaviours.
     *
     * @return <code>true</code> if there is a valid list; <code>false</code> if there is not
     */
    public boolean isValid() {
        return mediaList != null;
    }

    /**
     * Delegated behaviour pertaining to the associated media list events.
     *
     * @return events behaviour, may be <code>null</code> if there is currently no media list
     */
    public EventApi events() {
        return mediaList != null ? mediaList.events() : null;
    }

    /**
     * Delegated behaviour pertaining to the associated media list items.
     *
     * @return item behaviour, may be <code>null</code> if there is currently no media list
     */
    public MediaApi media() {
        return mediaList != null ? mediaList.media() : null;
    }

    /**
     * Create a new {@link MediaList} for the associated media list.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaList} when it has no further use for it.
     *
     * @return media list reference
     */
    public MediaList newMediaList() {
        if (mediaList != null) {
            return mediaList.newMediaList();
        } else {
            return null;
        }
    }

    /**
     * Create a new {@link MediaListRef} for the associated media list.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaListRef} when it has no further use for it.
     *
     * @return media list reference
     */
    public MediaListRef newMediaListRef() {
        if (mediaList != null) {
            return mediaList.newMediaListRef();
        } else {
            return null;
        }
    }

    @Override
    protected void release() {
        if (mediaList != null) {
            mediaList.release();
        }
    }

}
