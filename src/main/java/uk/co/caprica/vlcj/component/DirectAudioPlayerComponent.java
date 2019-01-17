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
import uk.co.caprica.vlcj.player.directaudio.AudioCallback;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

/**
 * Encapsulation of a direct audio player.
 */
public class DirectAudioPlayerComponent extends DirectAudioPlayerComponentBase implements MediaPlayerComponent {

    /**
     * Default factory initialisation arguments.
     * <p>
     * Sub-classes may totally disregard these arguments and provide their own.
     * <p>
     * A sub-class has access to these default arguments so new ones could be merged with these if
     * required.
     */
    static final String[] DEFAULT_FACTORY_ARGUMENTS = {
        "--quiet-synchro",
        "--intf=dummy"
    };

    private final boolean ownFactory;

    /**
     * Media player factory.
     */
    private final MediaPlayerFactory mediaPlayerFactory;

    /**
     * Media player.
     */
    private final DirectAudioPlayer mediaPlayer;

    public DirectAudioPlayerComponent(MediaPlayerFactory mediaPlayerFactory, String format, int rate, int channels, AudioCallback audioCallback) {
        this.ownFactory = mediaPlayerFactory == null;
        this.mediaPlayerFactory = initMediaPlayerFactory(mediaPlayerFactory);

        this.mediaPlayer = this.mediaPlayerFactory.mediaPlayers().newDirectAudioPlayer(format, rate, channels, audioCallback != null ? audioCallback : this);
        this.mediaPlayer.events().addMediaPlayerEventListener(this);

        onAfterConstruct();
    }

    /**
     * Create a direct audio player component.
     *
     * @param format decoded audio buffer format
     * @param rate decoded audio rate
     * @param channels decoded audio channels
     */
    public DirectAudioPlayerComponent(String format, int rate, int channels) {
        this(null, format, rate, channels, null);
    }

    public DirectAudioPlayerComponent(String format, int rate, int channels, AudioCallback audioCallback) {
        this(null, format, rate, channels, audioCallback);
    }

    private MediaPlayerFactory initMediaPlayerFactory(MediaPlayerFactory mediaPlayerFactory) {
        if (mediaPlayerFactory == null) {
            mediaPlayerFactory = new MediaPlayerFactory(DEFAULT_FACTORY_ARGUMENTS);
        }
        return mediaPlayerFactory;
    }

    /**
     * Get the direct audio player reference.
     * <p>
     * An application uses this handle to control the media player, add listeners and so on.
     *
     * @return media player
     */
    public final DirectAudioPlayer getMediaPlayer() {
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
