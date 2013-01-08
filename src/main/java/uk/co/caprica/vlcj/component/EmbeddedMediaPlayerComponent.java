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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.component;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 * Encapsulation of an embedded media player.
 * <p>
 * This component encapsulates a media player and an associated video surface suitable for embedding
 * inside a graphical user interface.
 * <p>
 * Most implementation details, like creating a factory and connecting the various objects together,
 * are encapsulated.
 * <p>
 * The default implementation will work out-of-the-box, but there are various template methods
 * available to sub-classes to tailor the behaviour of the component.
 * <p>
 * This class implements the most the most common use-case for an embedded media player and is
 * intended to enable a developer to get quickly started with the vlcj framework. More advanced
 * applications are free to directly use the {@link MediaPlayerFactory}, if required, as has always
 * been the case.
 * <p>
 * This component also implements the various media player listener interfaces, consequently an
 * implementation sub-class can simply override those listener methods to handle events.
 * <p>
 * Applications can get a handle to the underlying media player object by invoking
 * {@link #getMediaPlayer()}.
 * <p>
 * To use, simply create an instance of this class and add it to a visual container component like a
 * {@link JPanel} (or any other {@link Container}).
 * <p>
 * For example, here a media player component is used directly as the content pane of a
 * {@link JFrame}, and only two lines of code that use vlcj are required:
 *
 * <pre>
 * frame = new JFrame();
 * mediaPlayerComponent = new EmbeddedMediaPlayerComponent(); // &lt;--- 1
 * frame.setContentPane(mediaPlayerComponent);
 * frame.setSize(1050, 600);
 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 * frame.setVisible(true);
 * mediaPlayerComponent.getMediaPlayer().playMedia(mrl); // &lt;--- 2
 * </pre>
 *
 * An example of a sub-class to tailor behaviours and override event handlers:
 *
 * <pre>
 * mediaPlayerComponent = new EmbeddedMediaPlayerComponent() {
 *     protected String[] onGetMediaPlayerFactoryArgs() {
 *         return new String[] {&quot;--no-video-title-show&quot;};
 *     }
 *
 *     protected FullScreenStrategy onGetFullScreenStrategy() {
 *         return new XFullScreenStrategy(frame);
 *     }
 *
 *     public void videoOutputAvailable(MediaPlayer mediaPlayer, boolean videoOutput) {
 *     }
 *
 *     public void error(MediaPlayer mediaPlayer) {
 *     }
 *
 *     public void finished(MediaPlayer mediaPlayer) {
 *     }
 * };
 * </pre>
 * When the media player component is no longer needed, it should be released by invoking the
 * {@link #release()} method.
 * <p>
 * Since the media player factory associated by this component may be created by this component
 * itself or may be shared with some other media player resources it is the responsibility of
 * the application to also release the media player factory at the appropriate time.
 */
@SuppressWarnings("serial")
public class EmbeddedMediaPlayerComponent extends Panel implements MediaPlayerEventListener {

    /**
     * Default factory initialisation arguments.
     * <p>
     * Sub-classes may totally disregard these arguments and provide their own.
     * <p>
     * A sub-class has access to these default arguments so new ones could be merged with these if
     * required.
     */
    protected static final String[] DEFAULT_FACTORY_ARGUMENTS = {
        "--no-plugins-cache",
        "--no-video-title-show",
        "--no-snapshot-preview",
        "--quiet",
        "--quiet-synchro",
        "--intf",
        "dummy"
    };

    /**
     * Media player factory.
     */
    private final MediaPlayerFactory mediaPlayerFactory;

    /**
     * Media player.
     */
    private final EmbeddedMediaPlayer mediaPlayer;

    /**
     * Video surface canvas.
     */
    private final Canvas canvas;

    /**
     * Video surface encapsulation.
     */
    private final CanvasVideoSurface videoSurface;

    /**
     * Blank cursor to use when the cursor is disabled.
     */
    private Cursor blankCursor;

    /**
     * Construct a media player component.
     */
    public EmbeddedMediaPlayerComponent() {
        // Create the native resources
        mediaPlayerFactory = onGetMediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(onGetFullScreenStrategy());
        canvas = onGetCanvas();
        videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);
        // Prepare the user interface
        setBackground(Color.black);
        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);
        // Register listeners
        mediaPlayer.addMediaPlayerEventListener(this);
        // Set the overlay
        mediaPlayer.setOverlay(onGetOverlay());
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
     * Get the embedded media player reference.
     * <p>
     * An application uses this handle to control the media player, add listeners and so on.
     *
     * @return media player
     */
    public final EmbeddedMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Get the video surface {@link Canvas} component.
     * <p>
     * An application may want to add key/mouse listeners to the video surface component.
     *
     * @return video surface component
     */
    public final Canvas getVideoSurface() {
        return canvas;
    }

    /**
     * Enable or disable the mouse cursor when it is over the component.
     *
     * @param enabled <code>true</code> to enable (show) the cursor; <code>false</code> to disable (hide) it
     */
    public final void setCursorEnabled(boolean enabled) {
        setCursor(enabled ? null : getBlankCursor());
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
     * Create a blank 1x1 image to use when the cursor is disabled.
     *
     * @return cursor
     */
    private Cursor getBlankCursor() {
        if(blankCursor == null) {
            Image blankImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankImage, new Point(0, 0), "");
        }
        return blankCursor;
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
        return new MediaPlayerFactory(onGetMediaPlayerFactoryArgs());
    }

    /**
     * Template method to obtain the initialisation arguments used to create the media player
     * factory instance.
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
     * Template method to obtain a full-screen strategy implementation.
     * <p>
     * The default implementation does not provide any full-screen strategy.
     *
     * @return full-screen strategy implementation
     */
    protected FullScreenStrategy onGetFullScreenStrategy() {
        return null;
    }

    /**
     * Template method to obtain a video surface {@link Canvas} component.
     * <p>
     * The default implementation simply returns an ordinary Canvas with a black background.
     *
     * @return video surface component
     */
    protected Canvas onGetCanvas() {
        Canvas canvas = new Canvas();
        canvas.setBackground(Color.black);
        return canvas;
    }

    /**
     * Template method to obtain an overlay component.
     * <p>
     * The default implementation does not provide an overlay.
     * <p>
     * The overlay component may be a {@link Window} or a <code>Window</code> sub-class such as
     * {@link JWindow}.
     *
     * @return overlay component
     */
    protected Window onGetOverlay() {
        return null;
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
    public void mediaFreed(MediaPlayer mediaPlayer) {
    }

    @Override
    public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {
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
}
