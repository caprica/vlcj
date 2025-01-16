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

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.player.list.ControlsApi;
import uk.co.caprica.vlcj.player.list.EventApi;
import uk.co.caprica.vlcj.player.list.ListApi;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.StatusApi;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_list_new;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_subitems;

/**
 * Behaviour pertaining to media subitems.
 */
public final class SubitemApi extends BaseApi {

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

    /**
     * Sub-item handling mode, by default the first sub-item will be played automatically.
     */
    private SubItemMode mode = SubItemMode.PLAY_FIRST_SUB_ITEM;

    SubitemApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);

        this.mediaListPlayer = new MediaListPlayer(libvlcInstance);

        mediaListPlayer.mediaPlayer().setMediaPlayer(mediaPlayer);
    }

    /**
     * Delegated controls behaviour on the associated media list player.
     *
     * @return media list player controls behaviour
     */
    public ControlsApi controls() {
        return mediaListPlayer.controls();
    }

    /**
     * Delegated events behaviour on the associated media list player.
     *
     * @return media list player event behaviour
     */
    public EventApi events() {
        return mediaListPlayer.events();
    }

    /**
     * Delegated media item list behaviour on the associated media list player.
     *
     * @return media list player item list behaviour
     */
    public ListApi list() {
        return mediaListPlayer.list();
    }

    /**
     * Delegated status behaviour on the associated media list player.
     *
     * @return media list player status behaviour
     */
    public StatusApi status() {
        return mediaListPlayer.status();
    }

    /**
     * Set the sub-item handling mode for the media player.
     * <p>
     * The new mode will take effect the next time media is changed.
     *
     * @param mode new mode
     */
    public void mode(SubItemMode mode) {
        this.mode = mode;
        if (mode == SubItemMode.NO_ACTION) {
            // Replace the current list with a blank one to make sure any sub-items from previous media are gone
            MediaListRef emptyListRef = new MediaListRef(libvlcInstance, libvlc_media_list_new(libvlcInstance));
            try {
                mediaListPlayer.list().setMediaList(emptyListRef);
            } finally {
                emptyListRef.release();
            }
        }
    }

    /**
     * Invoked when the media has changed.
     * <p>
     * Set a new list for this media's sub-items.
     * <p>
     * If media was played, the sub-item list will start playing automatically. If you do <em>not</em> want this, then
     * instead of playing the media you should prepare it, then parse it, and wait for the parsed status changed media
     * event.
     * <p>
     * Alternatively, and more simply, you can set {@link SubItemMode#NO_ACTION} via {@link #mode(SubItemMode)}.
     *
     * @param media new media instance
     */
    void changeMedia(libvlc_media_t media) {
        if (mode == SubItemMode.NO_ACTION) {
            return;
        }
        MediaListRef mediaListRef = new MediaListRef(libvlcInstance, libvlc_media_subitems(media));
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
