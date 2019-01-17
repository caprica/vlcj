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

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;

import com.sun.jna.Memory;

/**
 * Encapsulation of a direct-rendering media player.
 * <p>
 * The default behaviour is to provide the video data via the {@link #display(DirectMediaPlayer, Memory[], BufferFormat)} method.
 * <p>
 * Sub-classes may override this method to implement their own processing, or alternately return an
 * implementation of a {@link RenderCallback} by overriding the {@link #onGetRenderCallback()}
 * template method.
 * <p>
 * An example:
 * <pre>
 * mediaPlayerComponent = new DirectMediaPlayerComponent() {
 *
 *     protected String[] onGetMediaPlayerFactoryArgs() { return new String[] {&quot;--no-video-title-show&quot;}; }
 *
 *     public void videoOutputAvailable(MediaPlayer mediaPlayer, boolean videoOutput) { }
 *
 *     public void error(MediaPlayer mediaPlayer) { }
 *
 *     public void finished(MediaPlayer mediaPlayer) { }
 *
 *     public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) { // Do something with the native video memory... }
 * };
 * </pre>
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
public class DirectMediaPlayerComponent extends DirectMediaPlayerComponentBase {

    /**
     * Default factory initialisation arguments.
     * <p>
     * Sub-classes may totally disregard these arguments and provide their own.
     * <p>
     * A sub-class has access to these default arguments so new ones could be merged with these if
     * required.
     */
    static final String[] DEFAULT_FACTORY_ARGUMENTS = {
        "--no-snapshot-preview",
        "--quiet-synchro",
        "--sub-filter=logo:marq",
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
    private final DirectMediaPlayer mediaPlayer;

    public DirectMediaPlayerComponent(MediaPlayerFactory mediaPlayerFactory, BufferFormatCallback bufferFormatCallback, RenderCallback renderCallback) {
        this.ownFactory = mediaPlayerFactory == null;
        this.mediaPlayerFactory = initMediaPlayerFactory(mediaPlayerFactory);

        this.mediaPlayer = this.mediaPlayerFactory.mediaPlayers().newDirectMediaPlayer(bufferFormatCallback, renderCallback != null ? renderCallback : this);
        this.mediaPlayer.events().addMediaPlayerEventListener(this);

        onAfterConstruct();
    }

    /**
     * Construct a media player component.
     *
     * @param bufferFormatCallback callback used to set video buffer characteristics
     */
    public DirectMediaPlayerComponent(BufferFormatCallback bufferFormatCallback) {
        this(null, bufferFormatCallback, null);
    }

    public DirectMediaPlayerComponent(BufferFormatCallback bufferFormatCallback, RenderCallback renderCallback) {
        this(null, bufferFormatCallback, renderCallback);
    }

    private MediaPlayerFactory initMediaPlayerFactory(MediaPlayerFactory mediaPlayerFactory) {
        if (mediaPlayerFactory == null) {
            mediaPlayerFactory = new MediaPlayerFactory(DEFAULT_FACTORY_ARGUMENTS);
        }
        return mediaPlayerFactory;
    }

    /**
     * Get the media player factory reference.
     *
     * @return media player factory
     */
    public final MediaPlayerFactory getMediaPlayerFactory() {
        return mediaPlayerFactory;
    }

    /**
     * Get the direct media player reference.
     * <p>
     * An application uses this handle to control the media player, add listeners and so on.
     *
     * @return media player
     */
    public final DirectMediaPlayer getMediaPlayer() {
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
