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

import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;

import java.awt.*;

/**
 * Implementation of an embedded media player.
 * <p>
 * The component may be added directly to a user interface layout.
 * <p>
 * When the component is no longer needed, it should be released by invoking the {@link #release()} method.
 */
@SuppressWarnings("serial")
public class EmbeddedMediaPlayerComponent extends EmbeddedMediaPlayerComponentBase implements MediaPlayerComponent {

    /**
     * Default factory initialisation arguments.
     */
    static final String[] DEFAULT_FACTORY_ARGUMENTS = MediaPlayerComponentDefaults.EMBEDDED_MEDIA_PLAYER_ARGS;

    /**
     * Flag true if this component created the media player factory, or false if it was supplied by the caller.
     */
    private boolean ownFactory;

    /**
     * Media player factory.
     */
    protected final MediaPlayerFactory mediaPlayerFactory;

    /**
     * Video surface component.
     */
    private final Component videoSurfaceComponent;

    /**
     * Media player.
     */
    private final EmbeddedMediaPlayer mediaPlayer;

    /**
     * Construct an embedded media player component.
     * <p>
     * Any constructor parameter may be <code>null</code>, in which case a reasonable default will be used.
     *
     * @param mediaPlayerFactory media player factory
     * @param videoSurfaceComponent heavyweight video surface component, will become part of this components UI layout
     * @param fullScreenStrategy full screen strategy
     * @param inputEvents keyboard/mouse input event configuration
     * @param overlay heavyweight overlay
     */
    public EmbeddedMediaPlayerComponent(MediaPlayerFactory mediaPlayerFactory, Component videoSurfaceComponent, FullScreenStrategy fullScreenStrategy, InputEvents inputEvents, Window overlay) {
        this.ownFactory = mediaPlayerFactory == null;
        this.mediaPlayerFactory = initMediaPlayerFactory(mediaPlayerFactory);

        this.videoSurfaceComponent = initVideoSurfaceComponent(videoSurfaceComponent);

        this.mediaPlayer = this.mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();

        this.mediaPlayer.videoSurface().set(this.mediaPlayerFactory.videoSurfaces().newVideoSurface(this.videoSurfaceComponent));
        this.mediaPlayer.fullScreen().strategy(fullScreenStrategy);
        this.mediaPlayer.overlay().set(overlay);
        this.mediaPlayer.events().addMediaPlayerEventListener(this);
        this.mediaPlayer.events().addMediaEventListener(this);

        setBackground(Color.black);
        setLayout(new BorderLayout());
        add(this.videoSurfaceComponent, BorderLayout.CENTER);

        initInputEvents(inputEvents);

        onAfterConstruct();
    }

    /**
     * Construct an embedded media player component from a builder.
     *
     * @param spec builder
     */
    public EmbeddedMediaPlayerComponent(MediaPlayerSpecs.EmbeddedMediaPlayerSpec spec) {
        this(spec.factory, spec.videoSurfaceComponent, spec.fullScreenStrategy, spec.inputEvents, spec.overlay);
    }

    /**
     * Construct an embedded media player component with reasonable defaults.
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
                mediaPlayer.input().enableKeyInputHandling(false);
                mediaPlayer.input().enableMouseInputHandling(false);
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
    public final Component videoSurfaceComponent() {
        return videoSurfaceComponent;
    }

    /**
     * Release the media player component and the associated native media player resources.
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

    @Override
    public final MediaPlayerFactory mediaPlayerFactory() {
        return mediaPlayerFactory;
    }

}
