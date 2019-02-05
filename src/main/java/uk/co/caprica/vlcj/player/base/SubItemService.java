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

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.list.DefaultMediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

// FIXME really need to nail down and document who owns the MediaList and when/where it is released
// FIXME do i need to expose the medialistplayer somehow? i shouldn't really have a getter but i could set loop mode and the controls and so on?
//          controls() in here could return subitem controls used to access the media list player?

// FIXME in light of the new insight gained into playing sub-items and media lists, we should get rid of this service
//       since it does hardly anything, and merge its functionality into a more appropriate place

public final class SubItemService extends BaseService {

    /**
     * Media list player used to play the sub-items.
     */
    private final MediaListPlayer mediaListPlayer;

    SubItemService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);

        this.mediaListPlayer = new DefaultMediaListPlayer(libvlc, libvlcInstance);

        mediaListPlayer.mediaPlayer().setMediaPlayer(mediaPlayer);
    }

    public MediaListPlayer player() {
        return mediaListPlayer;
    }

    /**
     *
     *
     * Set a new list for this media's sub-items - this component owns the list so must release it at the appropriate
     * time.
     * <p>
     * If media was played, the sub-item list will start playing automatically. If you do <em>not</em> want this, then
     * instead of playing the media you should prepare it, then parse it, and wait for the parsed status changed media
     * event.
     *
     * @param media
     */
    void changeMedia(Media media) {
        releaseMediaList();
        mediaListPlayer.list().setMediaList(media.subitems().get());
    }

    private void releaseMediaList() {
        MediaList oldList = mediaListPlayer.list().getMediaList();
        if (oldList != null) {
            oldList.release();
        }
    }

    @Override
    protected void release() {
        releaseMediaList();
        mediaListPlayer.release();
    }

}
