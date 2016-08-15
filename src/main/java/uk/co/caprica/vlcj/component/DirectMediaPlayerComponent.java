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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.component;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;

import com.sun.jna.Memory;

/**
 * Encapsulation of a direct-rendering media player.
 * <p>
 * The default behaviour is to provide the video data via the {@link #display(DirectMediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat)} method.
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
public class DirectMediaPlayerComponent implements MediaPlayerEventListener, RenderCallback {

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(DirectMediaPlayerComponent.class);

    /**
     * Default factory initialisation arguments.
     * <p>
     * Sub-classes may totally disregard these arguments and provide their own.
     * <p>
     * A sub-class has access to these default arguments so new ones could be merged with these if
     * required.
     */
    protected static final String[] DEFAULT_FACTORY_ARGUMENTS = {
        "--no-snapshot-preview",
        "--quiet-synchro",
        "--sub-filter=logo:marq",
        "--intf=dummy"
    };

    /**
     * Media player factory.
     */
    private final MediaPlayerFactory mediaPlayerFactory;

    /**
     * Media player.
     */
    private final DirectMediaPlayer mediaPlayer;

    /**
     * Construct a media player component.
     *
     * @param bufferFormatCallback callback used to set video buffer characteristics
     */
    public DirectMediaPlayerComponent(BufferFormatCallback bufferFormatCallback) {
        // Create the native resources
        mediaPlayerFactory = onGetMediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newDirectMediaPlayer(bufferFormatCallback, onGetRenderCallback());
        // Register listeners
        mediaPlayer.addMediaPlayerEventListener(this);
        // Sub-class initialisation
        onAfterConstruct();
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
        onAfterRelease();
    }

    /**
     * Release the media player component and the associated media player factory.
     * <p>
     * Optionally release the media player factory.
     * <p>
     * This method invokes {@link #release()}, then depending on the value of the <code>releaseFactory</code>
     * parameter the associated factory will also be released.
     *
     * @param releaseFactory <code>true</code> if the factory should also be released; <code>false</code> if it should not
     */
    public final void release(boolean releaseFactory) {
        release();
        if(releaseFactory) {
            mediaPlayerFactory.release();
        }
    }

    /**
     * Template method to create a media player factory.
     * <p>
     * The default implementation will invoke the {@link #onGetMediaPlayerFactoryArgs()} template
     * method.
     *
     * @return media player factory
     */
    protected MediaPlayerFactory onGetMediaPlayerFactory() {
        String[] args = Arguments.mergeArguments(onGetMediaPlayerFactoryArgs(), onGetMediaPlayerFactoryExtraArgs());
        logger.debug("args={}", Arrays.toString(args));
        return new MediaPlayerFactory(args);
    }

    /**
     * Template method to obtain the initialisation arguments used to create the media player
     * factory instance.
     * <p>
     * This can be used to provide arguments to use <em>instead</em> of the defaults.
     * <p>
     * If a sub-class overrides the {@link #onGetMediaPlayerFactory()} template method there is no
     * guarantee that {@link #onGetMediaPlayerFactoryArgs()} will be called.
     *
     * @return media player factory initialisation arguments
     */
    protected String[] onGetMediaPlayerFactoryArgs() {
        return DEFAULT_FACTORY_ARGUMENTS;
    }

    /**
     * Template method to obtain the extra initialisation arguments used to create the media player
     * factory instance.
     * <p>
     * This can be used to provide <em>additional</em> arguments to add to the hard-coded defaults.
     * <p>
     * If a sub-class overrides the {@link #onGetMediaPlayerFactory()} template method there is no
     * guarantee that {@link #onGetMediaPlayerFactoryExtraArgs()} will be called.
     *
     * @return media player factory initialisation arguments, or <code>null</code>
     */
    protected String[] onGetMediaPlayerFactoryExtraArgs() {
        return null;
    }

    /**
     * Template method to obtain a render callback implementation.
     * <p>
     * The default behaviour is simply to return this component instance itself so that sub-classes
     * may override {@link #display(DirectMediaPlayer, ByteBuffer[], BufferFormat)}.
     * <p>
     * A sub-class may provide any implementation of {@link RenderCallback} - including
     * {@link RenderCallbackAdapter}.
     *
     * @return render callback implementation
     */
    protected RenderCallback onGetRenderCallback() {
        return this;
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

    // === MediaPlayerEventListener =============================================

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
    }

    @Override
    public void opening(MediaPlayer mediaPlayer) {
    }

    @Override
    public void buffering(MediaPlayer mediaPlayer, float newCache) {
    }

    @Override
    public void playing(MediaPlayer mediaPlayer) {
    }

    @Override
    public void paused(MediaPlayer mediaPlayer) {
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
    }

    @Override
    public void forward(MediaPlayer mediaPlayer) {
    }

    @Override
    public void backward(MediaPlayer mediaPlayer) {
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
    }

    @Override
    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
    }

    @Override
    public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
    }

    @Override
    public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {
    }

    @Override
    public void pausableChanged(MediaPlayer mediaPlayer, int newSeekable) {
    }

    @Override
    public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {
    }

    @Override
    public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
    }

    @Override
    public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
    }

    @Override
    public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
    }

    @Override
    public void scrambledChanged(MediaPlayer mediaPlayer, int newScrambled) {
    }

    @Override
    public void elementaryStreamAdded(MediaPlayer mediaPlayer, int type, int id) {
    }

    @Override
    public void elementaryStreamDeleted(MediaPlayer mediaPlayer, int type, int id) {
    }

    @Override
    public void elementaryStreamSelected(MediaPlayer mediaPlayer, int type, int id) {
    }

    @Override
    public void corked(MediaPlayer mediaPlayer, boolean corked) {
    }

    @Override
    public void muted(MediaPlayer mediaPlayer, boolean muted) {
    }

    @Override
    public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
    }

    @Override
    public void audioDeviceChanged(MediaPlayer mediaPlayer, String audioDevice) {
    }

    @Override
    public void chapterChanged(MediaPlayer mediaPlayer, int newChapter) {
    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
    }

    @Override
    public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
    }

    @Override
    public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
    }

    @Override
    public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
    }

    @Override
    public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
    }

    @Override
    public void mediaParsedStatus(MediaPlayer mediaPlayer, int newStatus) {
    }

    @Override
    public void mediaFreed(MediaPlayer mediaPlayer) {
    }

    @Override
    public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {
    }

    @Override
    public void mediaSubItemTreeAdded(MediaPlayer mediaPlayer, libvlc_media_t item) {
    }

    @Override
    public void newMedia(MediaPlayer mediaPlayer) {
    }

    @Override
    public void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex) {
    }

    @Override
    public void subItemFinished(MediaPlayer mediaPlayer, int subItemIndex) {
    }

    @Override
    public void endOfSubItems(MediaPlayer mediaPlayer) {
    }

    // === RenderCallback =======================================================

    @Override
    public void display(DirectMediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
        // Default implementation does nothing, sub-classes should override this or
        // provide their own implementation of a RenderCallback
    }
}
