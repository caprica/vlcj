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

import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.component.callback.CallbackImagePainter;
import uk.co.caprica.vlcj.component.callback.ScaledCallbackImagePainter;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.callback.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.embedded.callback.format.RV32BufferFormat;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 *
 */
@SuppressWarnings("serial")
public class CallbackMediaPlayerComponent extends EmbeddedMediaPlayerComponentBase implements MediaPlayerComponent {

    /**
     * Default factory initialisation arguments.
     */
    static final String[] DEFAULT_FACTORY_ARGUMENTS = MediaPlayerComponentDefaults.EMBEDDED_MEDIA_PLAYER_ARGS;

    /**
     * Flag if this component created its own {@link MediaPlayerFactory}.
     */
    private boolean ownFactory;

    /**
     * Media player factory.
     */
    protected final MediaPlayerFactory mediaPlayerFactory;

    /**
     * Default render callback implementation, will be <code>null</code> if the client application provides its own
     * render callback.
     */
    private final DefaultRenderCallback defaultRenderCallback;

    /**
     * Painter used to render the video, will be <code>null</code>. if the client application provides its own render
     * callback.
     */
    private final CallbackImagePainter imagePainter;

    /**
     * Component used as the video surface.
     */
    private final JComponent videoSurfaceComponent;

    /**
     * Media player.
     */
    private final EmbeddedMediaPlayer mediaPlayer;

    /**
     * Image used to render the video.
     */
    private BufferedImage image;

    /**
     * Create a media player component that renders video frames received via native callbacks.
     * <p>
     * This component will provide a reasonable default implementation, but a client application is free to override
     * these defaults with their own implementation.
     * <p>
     * To rely on the defaults and have this component render the video, do not supply a <code>renderCallback</code>.
     * <p>
     * If a client application wishes to perform its own rendering, provide a <code>renderCallback</code>, and also a
     * <code>videoSurfaceComponent</code> if the client application wants the video surface they are rendering in to be
     * incorporated into this component's layout.
     *
     * @param mediaPlayerFactory
     * @param fullScreenStrategy
     * @param inputEvents
     * @param lockBuffers
     * @param renderCallback
     * @param bufferFormatCallback
     * @param videoSurfaceComponent
     */
    public CallbackMediaPlayerComponent(MediaPlayerFactory mediaPlayerFactory, FullScreenStrategy fullScreenStrategy, InputEvents inputEvents, boolean lockBuffers, CallbackImagePainter imagePainter, RenderCallback renderCallback, BufferFormatCallback bufferFormatCallback, JComponent videoSurfaceComponent) {
        this.ownFactory = mediaPlayerFactory == null;
        this.mediaPlayerFactory = initMediaPlayerFactory(mediaPlayerFactory);

        validateArguments(imagePainter, renderCallback, bufferFormatCallback, videoSurfaceComponent);

        if (renderCallback == null) {
            this.defaultRenderCallback = new DefaultRenderCallback();
            this.imagePainter          = imagePainter == null ? new ScaledCallbackImagePainter() : imagePainter;
            this.videoSurfaceComponent = new DefaultVideoSurfaceComponent();
            bufferFormatCallback       = new DefaultBufferFormatCallback();
            renderCallback             = this.defaultRenderCallback;
        } else {
            this.defaultRenderCallback = null;
            this.imagePainter          = null;
            this.videoSurfaceComponent = videoSurfaceComponent;
        }

        this.mediaPlayer = this.mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        this.mediaPlayer.fullScreen().setFullScreenStrategy(fullScreenStrategy);
        this.mediaPlayer.events().addMediaPlayerEventListener(this);

        this.mediaPlayer.videoSurface().setVideoSurface(this.mediaPlayerFactory.videoSurfaces().newVideoSurface(bufferFormatCallback, renderCallback, lockBuffers));

        setBackground(Color.black);
        setLayout(new BorderLayout());
        add(this.videoSurfaceComponent, BorderLayout.CENTER);

        initInputEvents(inputEvents);

        onAfterConstruct();
    }

    public CallbackMediaPlayerComponent(MediaPlayerFactory mediaPlayerFactory, FullScreenStrategy fullScreenStrategy, InputEvents inputEvents, boolean lockBuffers, CallbackImagePainter imagePainter) {
        this(mediaPlayerFactory, fullScreenStrategy, inputEvents, lockBuffers, null, null, null, null);
    }

    public CallbackMediaPlayerComponent(MediaPlayerFactory mediaPlayerFactory, FullScreenStrategy fullScreenStrategy, InputEvents inputEvents, boolean lockBuffers, RenderCallback renderCallback, BufferFormatCallback bufferFormatCallback, JComponent videoSurfaceComponent) {
        this(mediaPlayerFactory, fullScreenStrategy, inputEvents, lockBuffers, null, renderCallback, bufferFormatCallback, videoSurfaceComponent);
    }

    public CallbackMediaPlayerComponent(MediaPlayerSpecs.CallbackMediaPlayerSpec spec) {
        this(spec.factory, spec.fullScreenStrategy, spec.inputEvents, spec.lockedBuffers, spec.imagePainter, spec.renderCallback, spec.bufferFormatCallback, spec.videoSurfaceComponent);
    }

    public CallbackMediaPlayerComponent() {
        this(null, null, null, true, null, null, null, null);
    }

    private void validateArguments(CallbackImagePainter imagePainter, RenderCallback renderCallback, BufferFormatCallback bufferFormatCallback, JComponent videoSurfaceComponent) {
        if (renderCallback == null) {
            if (bufferFormatCallback  != null) throw new IllegalArgumentException("Do not specify bufferFormatCallback without a renderCallback");
            if (videoSurfaceComponent != null) throw new IllegalArgumentException("Do not specify videoSurfaceComponent without a renderCallback");
        } else {
            if (imagePainter          != null) throw new IllegalArgumentException("Do not specify imagePainter with a renderCallback");
            if (bufferFormatCallback  == null) throw new IllegalArgumentException("bufferFormatCallback is required with a renderCallback");
            if (videoSurfaceComponent == null) throw new IllegalArgumentException("videoSurfaceComponent is required with a renderCallback");
        }
    }

    private MediaPlayerFactory initMediaPlayerFactory(MediaPlayerFactory mediaPlayerFactory) {
        if (mediaPlayerFactory == null) {
            mediaPlayerFactory = new MediaPlayerFactory(DEFAULT_FACTORY_ARGUMENTS);
        }
        return mediaPlayerFactory;
    }

    private void initInputEvents(InputEvents inputEvents) {
        if (inputEvents == null) {
            inputEvents = RuntimeUtil.isNix() || RuntimeUtil.isMac() ? InputEvents.DEFAULT : InputEvents.DISABLE_NATIVE;
        }
        switch (inputEvents) {
            case NONE:
                break;
            case DISABLE_NATIVE:
                mediaPlayer.input().setEnableKeyInputHandling(false);
                mediaPlayer.input().setEnableMouseInputHandling(false);
                // Case fall-through is by design
            case DEFAULT:
                videoSurfaceComponent.addMouseListener(this);
                videoSurfaceComponent.addMouseMotionListener(this);
                videoSurfaceComponent.addMouseWheelListener(this);
                videoSurfaceComponent.addKeyListener(this);
                break;
        }
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
    public final JComponent getVideoSurfaceComponent() {
        return videoSurfaceComponent;
    }

    /**
     * Release the media player component and the associated native media player resources.
     * <p>
     * The associated media player factory will <em>not</em> be released, the client
     * application is responsible for releasing the factory at the appropriate time.
     */
    public final void release() {
        onBeforeRelease();

        // It is safe to remove listeners like this even if none were added (depends on configured InputEvents in the
        // constructor)
        videoSurfaceComponent.removeMouseListener(this);
        videoSurfaceComponent.removeMouseMotionListener(this);
        videoSurfaceComponent.removeMouseWheelListener(this);
        videoSurfaceComponent.removeKeyListener(this);

        mediaPlayer.release();

        if (ownFactory) {
            mediaPlayerFactory.release();
        }

        onAfterRelease();
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
     * Default implementation of a video surface component that uses a {@link CallbackImagePainter} to render the video
     * image.
     */
    private class DefaultVideoSurfaceComponent extends JPanel {

        private DefaultVideoSurfaceComponent() {
            setBackground(Color.black);
            setIgnoreRepaint(true); // FIXME dunno?
            // Set a reasonable default size for the video surface component in case the client application does
            // something like using pack() rather than setting a specific size
            setPreferredSize(new Dimension(640, 360));
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;

            imagePainter.prepare(g2, this);
            imagePainter.paint(g2, this, image);

            onPaintOverlay(g2);
        }
    }

    /**
     * Default implementation of a buffer format callback that returns a buffer format suitable for rendering into a
     * {@link BufferedImage}.
     */
    private class DefaultBufferFormatCallback implements BufferFormatCallback {

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            newVideoBuffer(sourceWidth, sourceHeight);
            return new RV32BufferFormat(sourceWidth, sourceHeight);
        }

    }

    /**
     * Used when the default buffer format callback is invoked to setup a new video buffer.
     * <p>
     * Here we create a new image to match the video size, and set the data buffer within that image as the data buffer
     * in the {@link DefaultRenderCallback}.
     * <p>
     * We also set a new preferred size on the video surface component in case the client application invalidates their
     * layout in anticipation of re-sizing their own window to accommodate the new video size.
     *
     * @param width width of the video
     * @param height height of the video
     */
    private void newVideoBuffer(int width, int height) {
        image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width, height);
        defaultRenderCallback.setImageBuffer(image);
        videoSurfaceComponent.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Default implementation of a render callback that copies video frame data directly to the data buffer of an image
     * raster.
     */
    private class DefaultRenderCallback extends RenderCallbackAdapter {

        private void setImageBuffer(BufferedImage image) {
            setBuffer(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
        }

        @Override
        protected void onDisplay(MediaPlayer mediaPlayer, int[] rgbBuffer) {
            videoSurfaceComponent.repaint();
        }

    }

    /**
     * Template methods to make it easy for a client application sub-class to render a lightweight overlay on top of the
     * video.
     * <p>
     * When this method is invoked the graphics context will already have a proper scaling applied according to the
     * video size.
     *
     * @param g2 graphics drawing context
     */
    protected void onPaintOverlay(Graphics2D g2) {
    }

}
