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

import org.jspecify.annotations.Nullable;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerCallbackHandler;
import uk.co.caprica.vlcj.support.callback.Holder;

import java.util.Objects;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_list_player_get_media_player;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_list_player_new;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_list_player_release;

/**
 * Implementation of a media list player.
 * <p>
 * The native media list player will automatically deal properly with media that has subitems (like YouTube movies), so
 * simply adding an ordinary MRL/URL is all that is needed for such media.
 */
public class MediaListPlayer {

    protected final libvlc_instance_t libvlcInstance;

    private final MediaPlayer mediaPlayer;

    private final ControlsApi controlsApi;
    private final ListApi listApi;
    private final StatusApi statusApi;

    // This is unfortunate to be both protected and not final, but is required for subclassing
    @Nullable
    protected libvlc_media_list_player_t mediaListPlayerInstance;

    /**
     * Create a new media list player.
     *
     * @param libvlcInstance libvlc instance
     */
    public MediaListPlayer(libvlc_instance_t libvlcInstance) {
        this.libvlcInstance = libvlcInstance;
        this.mediaPlayer = createMediaPlayer();

        this.controlsApi = new ControlsApi(this);
        this.listApi     = new ListApi    (this);
        this.statusApi   = new StatusApi  (this);
    }

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

    public ControlsApi controls() {
        return controlsApi;
    }

    public ListApi list() {
        return listApi;
    }

    public StatusApi status() {
        return statusApi;
    }

    public MediaPlayer mediaPlayer() {
        return mediaPlayer;
    }

    public final void release() {
        onBeforeRelease();

        mediaPlayer.release();

        controlsApi.release();
        listApi    .release();
        statusApi  .release();

        libvlc_media_list_player_release(mediaListPlayerInstance);

        onAfterRelease();
    }

    protected void onBeforePlay() {
    }

    /**
     * Provided to enable sub-classes to implement their own clean-up immediately before the media list player resources
     * will be freed.
     */
    protected void onBeforeRelease() {
        // Base implementation does nothing
    }

    /**
     * Provided to enable sub-classes to implement their own clean-up immediately after the media list player resources
     * have been freed.
     */
    protected void onAfterRelease() {
        // Base implementation does nothing
    }

    public final libvlc_media_list_player_t mediaListPlayerInstance() {
        Objects.requireNonNull(mediaListPlayerInstance, "Media list player instance must not be null");
        return mediaListPlayerInstance;
    }
}
