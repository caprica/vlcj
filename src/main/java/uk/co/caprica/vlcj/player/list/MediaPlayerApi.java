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

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_set_media_player;

/**
 * Behaviour pertaining to the associated media player.
 */
public final class MediaPlayerApi extends BaseApi {

    /**
     * Media player associated with the media list player.
     * <p>
     * We pin this reference here in case the client application has not.
     */
    private MediaPlayer mediaPlayer;

    MediaPlayerApi(MediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    /**
     * Associate an actual media player with the media list player.
     *
     * @param mediaPlayer media player
     */
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        libvlc_media_list_player_set_media_player(mediaListPlayerInstance, mediaPlayer.mediaPlayerInstance());
    }

    /**
     * Get the media player currently associated with this media list player.
     *
     * @return media player, may be <code>null</code>
     */
    public MediaPlayer mediaPlayer() {
        return mediaPlayer;
    }

    /**
     * If there is an associated media player then make sure the video surface is attached.
     */
    void attachVideoSurface() {
        if (mediaPlayer instanceof EmbeddedMediaPlayer) {
            ((EmbeddedMediaPlayer) mediaPlayer).videoSurface().attachVideoSurface();
        }
    }

}
