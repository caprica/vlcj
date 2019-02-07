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

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;

/**
 * Encapsulation of an audio player.
 * <p>
 * Most implementation details, like creating a factory, are encapsulated.
 * <p>
 * The default implementation will work out-of-the-box, but there are various template methods
 * available to sub-classes to tailor the behaviour of the component.
 * <p>
 * This class implements the most the most common use-case for an audio player and is intended to
 * enable a developer to get quickly started with the vlcj framework. More advanced applications are
 * free to directly use the {@link MediaPlayerFactory}, if required, as has always been the case.
 * <p>
 * This component also adds implements the various media player listener interfaces, consequently an
 * implementation sub-class can simply override those listener methods to handle events.
 * <p>
 * Applications can get a handle to the underlying media player object by invoking
 * {@link #getMediaPlayer()}.
 * <p>
 * To use, simply create an instance of this class.
 * <p>
 * In this minimal example, only two lines of code are required to create an audio player and play
 * media:
 *
 * <pre>
 * mediaPlayerComponent = new AudioPlayerComponent(); // &lt;--- 1
 * mediaPlayerComponent.getMediaPlayer().playMedia(mrl); // &lt;--- 2
 * </pre>
 *
 * This is not quite as useful as the {@link EmbeddedMediaPlayerComponent} as audio players are
 * generally quite simple to create anyway.
 * <p>
 * An audio player may still have a user interface, but it will not have an associated video
 * surface.
 * <p>
 * When the media player component is no longer needed, it should be released by invoking the
 * {@link #release()} method.
 * <p>
 * Since the media player factory associated by this component may be created by this component
 * itself or may be shared with some other media player resources it is the responsibility of
 * the application to also release the media player factory at the appropriate time.
 * <p>
 * It is always a better strategy to reuse media player components, rather than repeatedly creating
 * and destroying instances.
 */
public class AudioPlayerComponent extends AudioPlayerComponentBase implements MediaPlayerComponent {

    /**
     * Default factory initialisation arguments.
     * <p>
     * Sub-classes may totally disregard these arguments and provide their own.
     * <p>
     * A sub-class has access to these default arguments so new ones could be merged with these if
     * required.
     */
    static final String[] DEFAULT_FACTORY_ARGUMENTS = MediaPlayerComponentDefaults.AUDIO_MEDIA_PLAYER_ARGS;

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
     */
    public AudioPlayerComponent(MediaPlayerFactory mediaPlayerFactory) {
        this.ownFactory = mediaPlayerFactory == null;
        this.mediaPlayerFactory = initMediaPlayerFactory(mediaPlayerFactory);

        this.mediaPlayer = this.mediaPlayerFactory.mediaPlayers().newMediaPlayer();
        this.mediaPlayer.events().addMediaPlayerEventListener(this);

        onAfterConstruct();
    }

    public AudioPlayerComponent(MediaPlayerSpecs.AudioPlayerSpec spec) {
        this(spec.factory);
    }

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
    public final MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Release the media player component and the associated native media player resources.
     * <p>
     * The associated media player factory will <em>not</em> be released, the client
     * application is responsible for releasing the factory at the appropriate time.
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
    public final MediaPlayerFactory getMediaPlayerFactory() {
        return mediaPlayerFactory;
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
