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

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_next;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_pause;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_play;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_play_item_at_index;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_previous;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_set_pause;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_set_playback_mode;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_stop;

/**
 * Behaviour pertaining to media list player controls, like play, pause, stop of the list as a whole, play a specific
 * item, play next item, play previous item.
 */
public final class ControlsApi extends BaseApi {

    ControlsApi(MediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    /**
     * Play the media list.
     * <p>
     * If the play mode is {@link PlaybackMode#REPEAT} no item will be played and a "media list end reached"
     * event will be raised.
     */
    public void play() {
        attachVideoSurface();
        libvlc_media_list_player_play(mediaListPlayerInstance);
    }

    /**
     * Toggle-pause the media list.
     */
    public void pause() {
        libvlc_media_list_player_pause(mediaListPlayerInstance);
    }

    /**
     * Pause/un-pause the media list.
     *
     * @param pause <code>true</code> to pause; <code>false</code> to un-pause
     */
    public void setPause(boolean pause) {
        libvlc_media_list_player_set_pause(mediaListPlayerInstance, pause ? 1 : 0);
    }

    /**
     * Stop the media list.
     */
    public void stop() {
        libvlc_media_list_player_stop(mediaListPlayerInstance);
    }

    /**
     * Play a particular item on the media list.
     * <p>
     * When the mode is {@link PlaybackMode#REPEAT} this method is the only way to successfully
     * play media in the list.
     *
     * @param itemIndex index of the item to play
     * @return <code>true</code> if the item could be played, otherwise <code>false</code>
     */
    public boolean play(int itemIndex) {
        attachVideoSurface();
        return libvlc_media_list_player_play_item_at_index(mediaListPlayerInstance, itemIndex) == 0;
    }

    /**
     * Play the next item in the media list.
     * <p>
     * When the mode is {@link PlaybackMode#REPEAT} this method will replay the current media,
     * not the next one.
     *
     * @return <code>true</code> if the next item could be played, otherwise <code>false</code>
     */
    public boolean playNext() {
        attachVideoSurface();
        return libvlc_media_list_player_next(mediaListPlayerInstance) == 0;
    }

    /**
     * Play the previous item in the media list.
     * <p>
     * When the mode is {@link PlaybackMode#REPEAT} this method will replay the current media,
     * not the previous one.
     *
     * @return <code>true</code> if the previous item could be played, otherwise <code>false</code>
     */
    public boolean playPrevious() {
        attachVideoSurface();
        return libvlc_media_list_player_previous(mediaListPlayerInstance) == 0;
    }

    /**
     * Set the media list play mode.
     * <p>
     * Note that if setting the play mode to {@link PlaybackMode#REPEAT} you can not simply play the media list,
     * you must instead play a particular item (by its index).
     *
     * @param mode mode
     * @return <code>true</code> on success; <code>false</code> on error
     */
    public boolean setMode(PlaybackMode mode) {
        if (mode != null) {
            libvlc_media_list_player_set_playback_mode(mediaListPlayerInstance, mode.intValue());
            return true;
        } else {
            return false;
        }
    }

    private void attachVideoSurface() {
        mediaListPlayer.mediaPlayer().attachVideoSurface();
    }

}
