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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.component;

import uk.co.caprica.vlcj.binding.support.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.VideoTrack;
import uk.co.caprica.vlcj.player.component.callback.CallbackImagePainter;
import uk.co.caprica.vlcj.player.component.callback.SampleAspectRatioCallbackImagePainter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallbackAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.StandardBufferFormat;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Implementation of a callback "direct-rendering" media player.
 * <p>
 * This component renders video frames received via native callbacks.
 * <p>
 * The component may be added directly to a user interface layout - this is optional, you can use this component without
 * adding it directly to a user interface, in which case you would simply render the video however you like.
 * <p>
 * When the component is no longer needed, it should be released by invoking the {@link #release()} method.
 */
@SuppressWarnings("serial")
public class CallbackMediaPlayerComponent extends EmbeddedMediaPlayerComponentBase implements MediaPlayerComponent {

    /**
     * Default factory initialisation arguments.
     */
    static final String[] DEFAULT_FACTORY_ARGUMENTS = MediaPlayerComponentDefaults.EMBEDDED_MEDIA_PLAYER_ARGS;

    /**
     * Flag true if this component created the media player factory, or false if it was supplied by the caller.
     */
    private final boolean ownFactory;

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
     * Painter used to render the video, will be <code>null</code> if the client application provides its own render
     * callback.
     * <p>
     * Ordinarily set via constructor, but may be changed via {@link #setImagePainter(CallbackImagePainter)}.
     */
    private CallbackImagePainter imagePainter;

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
     * Stable native identifier of the currently selected video track.
     */
    private String selectedVideoTrackId;

    /**
     * Construct a callback media player component.
     * <p>
     * This component will provide a reasonable default implementation, but a client application is free to override
     * these defaults with their own implementation.
     * <p>
     * To rely on the defaults and have this component render the video, do not supply a <code>renderCallback</code>.
     * <p>
     * If a client application wishes to perform its own rendering, provide a <code>renderCallback</code>, a
     * <code>BufferFormatCallback</code>, and optionally (but likely) a <code>videoSurfaceComponent</code> if the client
     * application wants the video surface they are rendering in to be incorporated into this component's layout.
     *
     * @param mediaPlayerFactory media player factory
     * @param fullScreenStrategy full screen strategy
     * @param inputEvents keyboard/mouse input event configuration
     * @param lockBuffers <code>true</code> if the native video buffer should be locked; <code>false</code> if not
     * @param imagePainter image painter (video renderer)
     * @param renderCallback render callback
     * @param bufferFormatCallback buffer format callback
     * @param videoSurfaceComponent lightweight video surface component
     */
    public CallbackMediaPlayerComponent(MediaPlayerFactory mediaPlayerFactory, FullScreenStrategy fullScreenStrategy, InputEvents inputEvents, boolean lockBuffers, CallbackImagePainter imagePainter, RenderCallback renderCallback, BufferFormatCallback bufferFormatCallback, JComponent videoSurfaceComponent) {
        this.ownFactory = mediaPlayerFactory == null;
        this.mediaPlayerFactory = initMediaPlayerFactory(mediaPlayerFactory);

        validateArguments(imagePainter, renderCallback, bufferFormatCallback, videoSurfaceComponent);

        if (renderCallback == null) {
            this.defaultRenderCallback = new DefaultRenderCallback();
            this.imagePainter          = imagePainter == null ? new SampleAspectRatioCallbackImagePainter() : imagePainter;
            this.videoSurfaceComponent = new DefaultVideoSurfaceComponent();
            bufferFormatCallback       = new DefaultBufferFormatCallback();
            renderCallback             = this.defaultRenderCallback;
        } else {
            this.defaultRenderCallback = null;
            this.imagePainter          = null;
            this.videoSurfaceComponent = videoSurfaceComponent;
        }

        this.mediaPlayer = this.mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        this.mediaPlayer.fullScreen().strategy(fullScreenStrategy);
        this.mediaPlayer.events().addMediaPlayerEventListener(this);
        this.mediaPlayer.events().addMediaEventListener(this);

        this.mediaPlayer.events().addMediaPlayerEventListener(new VideoTrackListener());

        this.mediaPlayer.videoSurface().set(this.mediaPlayerFactory.videoSurfaces().newVideoSurface(bufferFormatCallback, renderCallback, lockBuffers));

        setBackground(Color.black);
        setLayout(new BorderLayout());
        if (this.videoSurfaceComponent != null) {
            add(this.videoSurfaceComponent, BorderLayout.CENTER);
        }

        initInputEvents(inputEvents);

        onAfterConstruct();
    }

    /**
     * Construct a callback media list player component for intrinsic rendering (by this component).
     *
     * @param mediaPlayerFactory media player factory
     * @param fullScreenStrategy full screen strategy
     * @param inputEvents keyboard/mouse input event configuration
     * @param lockBuffers <code>true</code> if the native video buffer should be locked; <code>false</code> if not
     * @param imagePainter image painter (video renderer)
     */
    public CallbackMediaPlayerComponent(MediaPlayerFactory mediaPlayerFactory, FullScreenStrategy fullScreenStrategy, InputEvents inputEvents, boolean lockBuffers, CallbackImagePainter imagePainter) {
        this(mediaPlayerFactory, fullScreenStrategy, inputEvents, lockBuffers, imagePainter, null, null, null);
    }

    /**
     * Construct a callback media list player component for external rendering (by the client application).
     *
     * @param mediaPlayerFactory media player factory
     * @param fullScreenStrategy full screen strategy
     * @param inputEvents keyboard/mouse input event configuration
     * @param lockBuffers <code>true</code> if the native video buffer should be locked; <code>false</code> if not
     * @param renderCallback render callback
     * @param bufferFormatCallback buffer format callback
     * @param videoSurfaceComponent lightweight video surface component
     */
    public CallbackMediaPlayerComponent(MediaPlayerFactory mediaPlayerFactory, FullScreenStrategy fullScreenStrategy, InputEvents inputEvents, boolean lockBuffers, RenderCallback renderCallback, BufferFormatCallback bufferFormatCallback, JComponent videoSurfaceComponent) {
        this(mediaPlayerFactory, fullScreenStrategy, inputEvents, lockBuffers, null, renderCallback, bufferFormatCallback, videoSurfaceComponent);
    }

    /**
     * Create a callback media player component from a builder.
     *
     * @param spec builder
     */
    public CallbackMediaPlayerComponent(MediaPlayerSpecs.CallbackMediaPlayerSpec spec) {
        this(spec.factory, spec.fullScreenStrategy, spec.inputEvents, spec.lockedBuffers, spec.imagePainter, spec.renderCallback, spec.bufferFormatCallback, spec.videoSurfaceComponent);
    }

    /**
     * Create a callback media player component with LibVLC initialisation arguments and reasonable defaults.
     *
     * @param libvlcArgs LibVLC initialisation arguments
     */
    public CallbackMediaPlayerComponent(String... libvlcArgs) {
        this(new MediaPlayerFactory(libvlcArgs), null, null, true, null, null, null, null);
    }

    /**
     * Create a callback media player component with reasonable defaults.
     */
    public CallbackMediaPlayerComponent() {
        this(null, null, null, true, null, null, null, null);
    }

    /**
     * Validate that arguments are passed for either intrinsic or external rendering, but not both.
     *
     * @param imagePainter image painter (video renderer)
     * @param renderCallback render callback
     * @param bufferFormatCallback buffer format callback
     * @param videoSurfaceComponent video surface component
     */
    private void validateArguments(CallbackImagePainter imagePainter, RenderCallback renderCallback, BufferFormatCallback bufferFormatCallback, JComponent videoSurfaceComponent) {
        if (renderCallback == null) {
            if (bufferFormatCallback  != null) throw new IllegalArgumentException("Do not specify bufferFormatCallback without a renderCallback");
            if (videoSurfaceComponent != null) throw new IllegalArgumentException("Do not specify videoSurfaceComponent without a renderCallback");
        } else {
            if (imagePainter          != null) throw new IllegalArgumentException("Do not specify imagePainter with a renderCallback");
            if (bufferFormatCallback  == null) throw new IllegalArgumentException("bufferFormatCallback is required with a renderCallback");
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
                mediaPlayer.input().enableKeyInputHandling(false);
                mediaPlayer.input().enableMouseInputHandling(false);
                // Case fall-through is by design
            case DEFAULT:
                if (videoSurfaceComponent != null) {
                    videoSurfaceComponent.addMouseListener(this);
                    videoSurfaceComponent.addMouseMotionListener(this);
                    videoSurfaceComponent.addMouseWheelListener(this);
                    videoSurfaceComponent.addKeyListener(this);
                }
                break;
        }
    }

    /**
     * Set a new image painter.
     * <p>
     * The image painter should only be changed when the media is stopped, changing an image painter during playback has
     * undefined behaviour.
     * <p>
     * This is <em>not</em> used if the application has supplied its own {@link RenderCallback} on instance creation.
     *
     * @param imagePainter image painter
     */
    public final void setImagePainter(CallbackImagePainter imagePainter) {
        this.imagePainter = imagePainter;
    }

    /**
     * Get the embedded media player reference.
     * <p>
     * An application uses this handle to control the media player, add listeners and so on.
     *
     * @return media player
     */
    public final EmbeddedMediaPlayer mediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Get the video surface {@link Canvas} component.
     * <p>
     * An application may want to add key/mouse listeners to the video surface component.
     *
     * @return video surface component
     */
    public final JComponent videoSurfaceComponent() {
        return videoSurfaceComponent;
    }

    /**
     * Release the media player component and the associated native media player resources.
     */
    public final void release() {
        onBeforeRelease();

        // It is safe to remove listeners like this even if none were added (depends on configured InputEvents in the
        // constructor)
        if (videoSurfaceComponent != null) {
            videoSurfaceComponent.removeMouseListener(this);
            videoSurfaceComponent.removeMouseMotionListener(this);
            videoSurfaceComponent.removeMouseWheelListener(this);
            videoSurfaceComponent.removeKeyListener(this);
        }

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
     * Default implementation of a video surface component that uses a {@link CallbackImagePainter} to render the video
     * image.
     */
    private class DefaultVideoSurfaceComponent extends JPanel {

        private DefaultVideoSurfaceComponent() {
            setBackground(Color.black);
            setIgnoreRepaint(true);
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
    private class DefaultBufferFormatCallback extends BufferFormatCallbackAdapter {
        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            return new StandardBufferFormat(sourceWidth, sourceHeight);
        }

        @Override
        public void newFormatSize(int bufferWidth, int bufferHeight, int displayWidth, int displayHeight) {
            newVideoBuffer(displayWidth, displayHeight);
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
        // The StandardBufferFormat provides correctly RGBA, but that native buffer byte ordering is not correct for an
        // ARGB buffered image (the byte ordering is reversed), so cheat by picking the BGR format for the buffered
        // image
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        defaultRenderCallback.setImageBuffer(image);
        if (videoSurfaceComponent != null) {
            videoSurfaceComponent.setPreferredSize(new Dimension(width, height));
        }
        if (imagePainter != null) {
            imagePainter.newVideoBuffer(width, height);
        }
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
        protected void onDisplay(MediaPlayer mediaPlayer, int[] buffer) {
            videoSurfaceComponent.repaint();
        }
    }

    /**
     * Event listener implementation that handles video track selection changes.
     * <p>
     * When the video track selection changes, notify the associated {@link CallbackImagePainter}.
     * <p>
     * This implementation assumes a "selected" event will always precede the "updated" event.
     */
    private class VideoTrackListener extends MediaPlayerEventAdapter {
        @Override
        public void elementaryStreamSelected(MediaPlayer mediaPlayer, TrackType type, String unselectedStreamId, String selectedStreamId) {
            if (imagePainter == null || TrackType.VIDEO != type) {
                return;
            }
            if (unselectedStreamId != null && unselectedStreamId.equals(selectedVideoTrackId)) {
                imagePainter.videoTrackChanged(null);
            }
            selectedVideoTrackId = selectedStreamId;
        }

        @Override
        public void elementaryStreamUpdated(MediaPlayer mediaPlayer, TrackType type, int id, String streamId) {
            if (imagePainter == null || !streamId.equals(CallbackMediaPlayerComponent.this.selectedVideoTrackId)) {
                return;
            }
            VideoTrack track = (VideoTrack) mediaPlayer.tracks().track(streamId);
            try {
                imagePainter.videoTrackChanged(track);
            } finally {
                if (track != null) {
                    track.release();
                }
            }
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
