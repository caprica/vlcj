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

package uk.co.caprica.vlcj.component;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

/**
 * Encapsulation of an audio media list player.
 * <p>
 * This component extends the {@link AudioPlayerComponent} to incorporate a
 * {@link MediaListPlayer} and an associated {@link MediaList}.
 */
public class AudioListPlayerComponent extends AudioListPlayerComponentBase {

    /**
     * Media list player.
     */
    private final MediaListPlayer mediaListPlayer;

    /**
     * Media list.
     */
    private final MediaList mediaList;

    /**
     * Construct a media list player component.
     */
    public AudioListPlayerComponent(MediaPlayerFactory mediaPlayerFactory) {
        super(mediaPlayerFactory);

        this.mediaListPlayer = getMediaPlayerFactory().mediaPlayers().newMediaListPlayer();
        this.mediaListPlayer.mediaPlayer().setMediaPlayer(getMediaPlayer());
        this.mediaListPlayer.events().addMediaListPlayerEventListener(this);

        this.mediaList = getMediaPlayerFactory().media().newMediaList();
        this.mediaList.events().addMediaListEventListener(this);

        this.mediaListPlayer.list().setMediaList(this.mediaList);

        onAfterConstruct();
    }

    public AudioListPlayerComponent(MediaPlayerSpecs.AudioPlayerSpec spec) {
        this(spec.factory);
    }

    public AudioListPlayerComponent() {
        this((MediaPlayerFactory) null);
    }

    /**
     * Get the embedded media list player reference.
     * <p>
     * An application uses this handle to control the media player, add listeners and so on.
     *
     * @return media list player
     */
    public final MediaListPlayer getMediaListPlayer() {
        return mediaListPlayer;
    }

    /**
     * Get the embedded media list reference.
     *
     * @return media list
     */
    public final MediaList getMediaList() {
        return mediaList;
    }

    @Override
    protected final void onBeforeRelease() {
        mediaListPlayer.release();
        mediaList.release();
    }

}
