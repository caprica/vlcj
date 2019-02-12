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

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.model.MediaListRef;
import uk.co.caprica.vlcj.player.list.ControlsService;
import uk.co.caprica.vlcj.player.list.DefaultMediaListPlayer;
import uk.co.caprica.vlcj.player.list.EventService;
import uk.co.caprica.vlcj.player.list.ListService;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.ModeService;
import uk.co.caprica.vlcj.player.list.StatusService;
import uk.co.caprica.vlcj.player.list.UserDataService;

// FIXME similar to thinking about "persistent" media listener, what about a MediaListListener in the same context too? in this case it would be a bit easier because the medialistplayer never changes instance

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

    public ControlsService controls() {
        return mediaListPlayer.controls();
    }

    public EventService events() {
        return mediaListPlayer.events();
    }

    public ListService list() {
        return mediaListPlayer.list();
    }

    public ModeService mode() {
        return mediaListPlayer.mode();
    }

    public StatusService status() {
        return mediaListPlayer.status();
    }

    public UserDataService userData() {
        return mediaListPlayer.userData();
    }

    /**
     *
     *
     * Set a new list for this media's sub-items.
     * <p>
     * If media was played, the sub-item list will start playing automatically. If you do <em>not</em> want this, then
     * instead of playing the media you should prepare it, then parse it, and wait for the parsed status changed media
     * event.
     *
     * @param media
     */
    void changeMedia(libvlc_media_t media) {
        MediaListRef mediaListRef = new MediaListRef(libvlc, libvlc.libvlc_media_subitems(media));
        try {
            mediaListPlayer.list().setMediaList(new MediaListRef(libvlc, libvlc.libvlc_media_subitems(media)));
        }
        finally {
            mediaListRef.release();
        }
    }

    @Override
    protected void release() {
        mediaListPlayer.release();
    }

}
