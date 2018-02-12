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

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JWindow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.WindowedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.WindowVideoSurface;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Encapsulation of an window media player.
 * <p>
 * This component encapsulates a media player and an associated video surface suitable for mac.
 * You need to initialize the AWT window object before using this class.
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
 * To use, simply create an instance of AWT window and pass to constructor of this class.
 * <p>
 * For example, make a window that meets your own needs, and use it to build the player window.
 *
 * <pre>
 * Window window = new Window(null);
 * window.setBounds(200, 200, 800, 480);
 * window.setVisible(true);
 * StandaloneMediaPlayerWindow playerWindow = new StandaloneMediaPlayerWindow(window);
 * playerWindow.getMediaPlayer().playMedia(mrl); // &lt;--- 2
 * </pre>
 *
 * An example of a sub-class to tailor behaviours and override event handlers:
 *
 * <pre>
 * playerWindow = new StandaloneMediaPlayerWindow(window) {
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
 * Note: Key and mouse events may not work properly with this implementation.
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
public class StandaloneMediaPlayerWindow implements MediaPlayerEventListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    /**
     * Enumeration of flags for controller input (mouse and keyboard) event handling for the video
     * surface.
     */
    public enum InputEvents {

        /**
         * No input event handling, no mouse or keyboard listener events will fire.
         */
        NONE,

        /**
         * Default input event handling, mouse and keyboard listener events will fire.
         */
        DEFAULT,

        /**
         * Disable native input event handling, mouse and keyboard listener events will fire.
         * <p>
         * This is the mode that is usually required on Windows.
         */
        DISABLE_NATIVE
    }

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(StandaloneMediaPlayerWindow.class);

    /**
     * Default factory initialisation arguments.
     * <p>
     * Sub-classes may totally disregard these arguments and provide their own.
     * <p>
     * A sub-class has access to these default arguments so new ones could be merged with these if
     * required.
     */
    protected static final String[] DEFAULT_FACTORY_ARGUMENTS = {
        "--video-title=vlcj video output",
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
    private final WindowedMediaPlayer mediaPlayer;

    /**
     * Video surface window.
     */
    private final Window window;

    /**
     * Video surface encapsulation.
     */
    private final WindowVideoSurface videoSurface;

    /**
     * Blank cursor to use when the cursor is disabled.
     */
    private Cursor blankCursor;

    /**
     * Construct a media player component.
     * The window object must be visible.
     */
    public StandaloneMediaPlayerWindow(Window window) {
        // Create the native resources
        mediaPlayerFactory = onGetMediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newWindowedMediaPlayer(onGetFullScreenStrategy());
        this.window = window;
        videoSurface = mediaPlayerFactory.newVideoSurface(window);
        mediaPlayer.setVideoSurface(videoSurface);
        // Register listeners
        mediaPlayer.addMediaPlayerEventListener(this);
        switch (onGetInputEvents()) {
            case NONE:
                break;
            case DISABLE_NATIVE:
                mediaPlayer.setEnableKeyInputHandling(false);
                mediaPlayer.setEnableMouseInputHandling(false);
                // Case fall-through is by design
            case DEFAULT:
                window.addMouseListener(this);
                window.addMouseMotionListener(this);
                window.addMouseWheelListener(this);
                window.addKeyListener(this);
                break;
        }
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
    public final WindowedMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Get the video surface {@link Window} component.
     * <p>
     * An application may want to add key/mouse listeners to the video surface component.
     *
     * @return video surface component
     */
    public final Window getVideoSurface() {
        return window;
    }

    /**
     * Enable or disable the mouse cursor when it is over the component.
     * <p>
     * Note that you may see glitchy behaviour if you try and disable the cursor <em>after</em> you
     * show the window/frame that contains your video surface.
     * <p>
     * If you want to disable the cursor for this component you should do so before you show the
     * window.
     *
     * @param enabled <code>true</code> to enable (show) the cursor; <code>false</code> to disable (hide) it
     */
    public final void setCursorEnabled(boolean enabled) {
        window.setCursor(enabled ? null : getBlankCursor());
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
        logger.debug("release(releaseFactory={})", releaseFactory);
        release();
        if(releaseFactory) {
            logger.debug("Releasing media player factory");
            mediaPlayerFactory.release();
        }
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
     * Template method to determine how input events should be processed by the component.
     * <p>
     * By default keyboard and mouse listener events from the video surface will be dispatched to
     * the corresponding template methods in this component.
     * <p>
     * In addition, by Default on Windows the native input handling is disabled. This is usually
     * what you always want if you want mouse and/or keyboard events from the video surface on
     * Windows).
     * <p>
     * Override this method to change the default handling.
     * <p>
     * No matter what is chosen here, an application can still add its own listeners the usual way
     * and can still choose to disable (or not) the native input handling.
     *
     * @return value describing how to handle input events, never <code>null</code>
     */
    protected InputEvents onGetInputEvents() {
        if (RuntimeUtil.isNix() || RuntimeUtil.isMac()) {
            return InputEvents.DEFAULT;
        }
        else {
            return InputEvents.DISABLE_NATIVE;
        }
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

    // === MouseListener ========================================================

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    // === MouseMotionListener ==================================================

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    // === MouseWheelListener ===================================================

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    // === KeyListener ==========================================================

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
