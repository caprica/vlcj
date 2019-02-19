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
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.player.list.ControlsService;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.EventService;
import uk.co.caprica.vlcj.player.list.ListService;
import uk.co.caprica.vlcj.player.list.StatusService;

// FIXME similar to thinking about "persistent" media listener, what about a MediaListListener in the same context too? in this case it would be a bit easier because the medialistplayer never changes instance

/**
 * Behaviour pertaining to media subitems.
 */
public final class SubitemService extends BaseService {

    /**
     * Media list player used to play the sub-items.
     * <p>
     * It is an important principle that the associated media list player is "owned" by this component and that it is
     * never exposed directly to client applications.
     * <p>
     * Various aspects of the media list player are exposed to clients by methods here that delegate to the
     * corresponding methods on the media list player instance.
     */
    private final MediaListPlayer mediaListPlayer;

    SubitemService(MediaPlayer mediaPlayer) {
        super(mediaPlayer);

        this.mediaListPlayer = new MediaListPlayer(libvlc, libvlcInstance);

        mediaListPlayer.mediaPlayer().setMediaPlayer(mediaPlayer);
    }

    /**
     * Delegated controls behaviour on the associated media list player.
     *
     * @return media list player controls behaviour
     */
    public ControlsService controls() {
        return mediaListPlayer.controls();
    }

    /**
     * Delegated events behaviour on the associated media list player.
     *
     * @return media list player event behaviour
     */
    public EventService events() {
        return mediaListPlayer.events();
    }

    /**
     * Delegated media item list behaviour on the associated media list player.
     *
     * @return media list player item list behaviour
     */
    public ListService list() {
        return mediaListPlayer.list();
    }

    /**
     * Delegated status behaviour on the associated media list player.
     *
     * @return media list player status behaviour
     */
    public StatusService status() {
        return mediaListPlayer.status();
    }

    /**
     * Invoked when the media has changed.
     * <p>
     * Set a new list for this media's sub-items.
     * <p>
     * If media was played, the sub-item list will start playing automatically. If you do <em>not</em> want this, then
     * instead of playing the media you should prepare it, then parse it, and wait for the parsed status changed media
     * event.
     *
     * @param media new media instance
     */
    void changeMedia(libvlc_media_t media) {
        // Stop the media list player before setting a new list - without this, the new subitem list will not start
        // playing correctly
        mediaListPlayer.controls().stop();

        MediaListRef mediaListRef = new MediaListRef(libvlc, libvlcInstance, libvlc.libvlc_media_subitems(media));
        try {
            mediaListPlayer.list().setMediaList(mediaListRef);
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
