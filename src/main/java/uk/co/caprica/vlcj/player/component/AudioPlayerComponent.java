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
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * Implementation of an audio player.
 * <p>
 * When the component is no longer needed, it should be released by invoking the {@link #release()} method.
 */
public class AudioPlayerComponent extends AudioPlayerComponentBase implements MediaPlayerComponent {

    /**
     * Default factory initialisation arguments.
     */
    static final String[] DEFAULT_FACTORY_ARGUMENTS = MediaPlayerComponentDefaults.AUDIO_MEDIA_PLAYER_ARGS;

    /**
     * Flag true if this component created the media player factory, or false if it was supplied by the caller.
     */
    private final boolean ownFactory;

    /**
     * Media player factory.
     */
    private final MediaPlayerFactory mediaPlayerFactory;

    /**
     * Media player.
     */
    private final MediaPlayer mediaPlayer;

    /**
     * Construct a media player component.
     * <p>
     * Any constructor parameter may be <code>null</code>, in which case a reasonable default will be used.
     *
     * @param mediaPlayerFactory media player factory
     */
    public AudioPlayerComponent(MediaPlayerFactory mediaPlayerFactory) {
        this.ownFactory = mediaPlayerFactory == null;
        this.mediaPlayerFactory = initMediaPlayerFactory(mediaPlayerFactory);

        this.mediaPlayer = onCreateMediaPlayer();
        this.mediaPlayer.events().addMediaPlayerEventListener(this);
        this.mediaPlayer.events().addMediaEventListener(this);

        onAfterConstruct();
    }

    /**
     * Construct a media player component from a builder.
     *
     * @param spec builder
     */
    public AudioPlayerComponent(MediaPlayerSpecs.AudioPlayerSpec spec) {
        this(spec.factory);
    }

    /**
     * Construct a media player component with reasonable defaults.
     */
    public AudioPlayerComponent() {
        this((MediaPlayerFactory) null);
    }

    private MediaPlayerFactory initMediaPlayerFactory(MediaPlayerFactory mediaPlayerFactory) {
        if (mediaPlayerFactory == null) {
            mediaPlayerFactory = new MediaPlayerFactory(DEFAULT_FACTORY_ARGUMENTS);
        }
        return mediaPlayerFactory;
    }

    /**
     * Get the media player reference.
     * <p>
     * An application uses this handle to control the media player, add listeners and so on.
     *
     * @return media player
     */
    public final MediaPlayer mediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Release the media player component and the associated native media player resources.
     */
    public final void release() {
        onBeforeRelease();

        mediaPlayer.release();

        if (ownFactory) {
            mediaPlayerFactory.release();
        }

        onAfterRelease();
    }

    @Override
    public final MediaPlayerFactory mediaPlayerFactory() {
        return mediaPlayerFactory;
    }

    /**
     * Template method invoked to create a media player instance.
     * <p>
     * Intended to be overridden only for sub-classes (like {@link AudioListPlayerComponent} that provide their own
     * media player instance.
     *
     * @return media player
     */
    protected MediaPlayer onCreateMediaPlayer() {
        return this.mediaPlayerFactory.mediaPlayers().newMediaPlayer();
    }

    /**
     * Template method invoked at the end of the media player constructor.
     */
    protected void onAfterConstruct() {
    }

    /**
     * Template method invoked immediately prior to releasing the media player and media player
     * factory instances.
     */
    protected void onBeforeRelease() {
    }

    /**
     * Template method invoked immediately after releasing the media player and media player factory
     * instances.
     */
    protected void onAfterRelease() {
    }

}
