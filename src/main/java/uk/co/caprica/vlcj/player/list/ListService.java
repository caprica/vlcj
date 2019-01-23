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

import uk.co.caprica.vlcj.medialist.MediaList;

public final class ListService extends BaseService {

    private MediaList mediaList;

    ListService(DefaultMediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    /**
     * Set the media list (i.e. the "play" list).
     * <p>
     * The caller still "owns" the {@link MediaList} and is responsible for invoke {@link MediaList#release()} at the
     * appropriate time.
     *
     * @param mediaList media list
     */
    public void setMediaList(MediaList mediaList) {
        libvlc.libvlc_media_list_player_set_media_list(mediaListPlayerInstance, mediaList.mediaListInstance());
        this.mediaList = mediaList;
    }

    /**
     * Get the media list.
     *
     * @return media list
     */
    public MediaList getMediaList() {
        return mediaList;
    }

}
