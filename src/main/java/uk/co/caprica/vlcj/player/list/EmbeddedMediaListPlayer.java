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

package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerCallbackHandler;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.support.callback.Holder;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_list_player_get_media_player;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_list_player_new;

public class EmbeddedMediaListPlayer extends MediaListPlayer {

    /**
     * Create a new media list player.
     *
     * @param libvlcInstance libvlc instance
     */
    public EmbeddedMediaListPlayer(libvlc_instance_t libvlcInstance) {
        super(libvlcInstance);
    }

    @Override
    protected MediaPlayer createMediaPlayer() {
        var holder = new Holder<MediaPlayer>();
        return new MediaPlayer(libvlcInstance) {
            @Override
            protected MediaPlayerCallbackHandler initCallbackHandler() {
                return new MediaPlayerCallbackHandler(holder);
            }

            @Override
            protected libvlc_media_player_t initMediaPlayer(MediaPlayerCallbackHandler callbackHandler) {
                holder.set(this);
                mediaListPlayerInstance = libvlc_media_list_player_new(libvlcInstance, callbackHandler.callbacks(), null);
                return libvlc_media_list_player_get_media_player(mediaListPlayerInstance);
            }
        };
    }

    @Override
    protected final void onBeforePlay() {
        mediaPlayer().videoSurface().attachVideoSurface();
    }

    @Override
    public EmbeddedMediaPlayer mediaPlayer() {
        return (EmbeddedMediaPlayer) super.mediaPlayer();
    }
}
