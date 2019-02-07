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
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;

import javax.swing.*;
import java.awt.*;

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
 * This component also provides template methods for mouse and keyboard events - these are events
 * for the <em>video surface</em> and not for the <code>Panel</code> that this component is itself
 * contained in. If for some reason you need events for the <code>Panel</code> you can just add
 * them by calling the usual add listener methods.
 * <p>
 * You can use template methods and/or add your own listeners depending on your needs.
 * <p>
 * Key events will only be delivered if the video surface has the focus. It is up to you to manage
 * that.
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
@SuppressWarnings("serial")
public class EmbeddedMediaPlayerComponent extends EmbeddedMediaPlayerComponentBase implements MediaPlayerComponent {

    /**
     * Default factory initialisation arguments.
     */
    static final String[] DEFAULT_FACTORY_ARGUMENTS = MediaPlayerComponentDefaults.EMBEDDED_MEDIA_PLAYER_ARGS;

    /**
     *
     */
    private boolean ownFactory;

    /**
     * Media player factory.
     */
    protected final MediaPlayerFactory mediaPlayerFactory;

    /**
     *
     */
    private final Component videoSurfaceComponent;

    /**
     * Media player.
     */
    private final EmbeddedMediaPlayer mediaPlayer;

    /**
     *
     *
     * @param mediaPlayerFactory
     * @param videoSurfaceComponent
     * @param fullScreenStrategy
     * @param inputEvents
     * @param overlay
     */
    public EmbeddedMediaPlayerComponent(MediaPlayerFactory mediaPlayerFactory, Component videoSurfaceComponent, FullScreenStrategy fullScreenStrategy, InputEvents inputEvents, Window overlay) {
        this.ownFactory = mediaPlayerFactory == null;
        this.mediaPlayerFactory = initMediaPlayerFactory(mediaPlayerFactory);

        this.videoSurfaceComponent = initVideoSurfaceComponent(videoSurfaceComponent);

        this.mediaPlayer = this.mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();

        this.mediaPlayer.videoSurface().setVideoSurface(this.mediaPlayerFactory.videoSurfaces().newVideoSurface(this.videoSurfaceComponent));
        this.mediaPlayer.fullScreen().setFullScreenStrategy(fullScreenStrategy);
        this.mediaPlayer.overlay().setOverlay(overlay);

        setBackground(Color.black);
        setLayout(new BorderLayout());
        add(this.videoSurfaceComponent, BorderLayout.CENTER);

        initInputEvents(inputEvents);

        onAfterConstruct();
    }

    public EmbeddedMediaPlayerComponent(MediaPlayerSpecs.EmbeddedMediaPlayerSpec spec) {
        this(spec.factory, spec.videoSurfaceComponent, spec.fullScreenStrategy, spec.inputEvents, spec.overlay);
    }

    /**
     * Construct a media player component.
     */
    public EmbeddedMediaPlayerComponent() {
        this(null, null, null, null, null);
    }

    private MediaPlayerFactory initMediaPlayerFactory(MediaPlayerFactory mediaPlayerFactory) {
        if (mediaPlayerFactory == null) {
            mediaPlayerFactory = new MediaPlayerFactory(DEFAULT_FACTORY_ARGUMENTS);
        }
        return mediaPlayerFactory;
    }

    private Component initVideoSurfaceComponent(Component videoSurfaceComponent) {
        if (videoSurfaceComponent == null) {
            videoSurfaceComponent = new Canvas();
            videoSurfaceComponent.setBackground(Color.black);
        }
        return videoSurfaceComponent;
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
    public final Component getVideoSurfaceComponent() {
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

}
