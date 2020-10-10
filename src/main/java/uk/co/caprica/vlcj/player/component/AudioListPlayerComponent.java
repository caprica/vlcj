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

package uk.co.caprica.vlcj.player.component;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

/**
 * Implementation of an audio list player.
 * <p>
 * When the component is no longer needed, it should be released by invoking the {@link #release()} method.
 */
public class AudioListPlayerComponent extends AudioListPlayerComponentBase {

    /**
     * Media list player.
     */
    private MediaListPlayer mediaListPlayer;

    /**
     * Media list.
     */
    private MediaList mediaList;

    /**
     * Construct an audio list player component.
     * <p>
     * Any constructor parameter may be <code>null</code>, in which case a reasonable default will be used.
     *
     * @param mediaPlayerFactory media player factory
     */
    public AudioListPlayerComponent(MediaPlayerFactory mediaPlayerFactory) {
        super(mediaPlayerFactory);
        applyMediaList();
        onAfterConstruct();
    }

    /**
     * Construct an audio list player component from a builder.
     *
     * @param spec builder
     */
    public AudioListPlayerComponent(MediaPlayerSpecs.AudioPlayerSpec spec) {
        this(spec.factory);
    }

    /**
     * Construct an audio list player component with reasonable defaults.
     */
    public AudioListPlayerComponent() {
        this((MediaPlayerFactory) null);
    }

    @Override
    final protected MediaPlayer onCreateMediaPlayer() {
        this.mediaListPlayer = mediaPlayerFactory().mediaPlayers().newMediaListPlayer();
        this.mediaListPlayer.events().addMediaListPlayerEventListener(this);

        this.mediaList = mediaPlayerFactory().media().newMediaList();
        this.mediaList.events().addMediaListEventListener(this);

        // Use the native media player instance already associated with the media list player
        return mediaPlayerFactory().mediaPlayers().newMediaPlayer(this.mediaListPlayer);
    }

    private void applyMediaList() {
        MediaListRef mediaListRef = mediaList.newMediaListRef();
        try {
            this.mediaListPlayer.list().setMediaList(mediaListRef);
        }
        finally {
            mediaListRef.release();
        }
    }

    /**
     * Get the embedded media list player reference.
     *
     * @return media list player
     */
    public final MediaListPlayer mediaListPlayer() {
        return mediaListPlayer;
    }

    @Override
    protected final void onBeforeRelease() {
        mediaListPlayer.release();
        mediaList.release();
    }

}
