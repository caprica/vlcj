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

package uk.co.caprica.vlcj.player.embedded;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * Implementation of a media player that renders video to an video surface embedded in the application user interface.
 * <p>
 * Note that to get mouse and keyboard events delivered via listeners on some platforms (i.e. Windows) you will likely
 * need to invoke {@link InputApi#enableMouseInputHandling(boolean)} <em>and</em>
 * {@link InputApi#enableKeyInputHandling(boolean)}.
 * <p>
 * This implementation supports the use of a heavyweight 'overlay' window that will track the video surface position and
 * size. Such an overlay could be used to paint custom graphics over the top of the video.
 * <p>
 * The overlay window should be non-opaque - support for this depends on the JVM, desktop window manager and graphics
 * device hardware and software.
 * <p>
 * The overlay also has some significant limitations, it is a component that covers the video surface component and will
 * prevent mouse and keyboard events from being processed by the video surface. Workarounds to delegate the mouse and
 * keyboard events to the underlying component may be possible but that is a responsibility of the overlay itself.
 * <p>
 * The overlay will also 'lag' the main application frame when the frame is dragged - the event used to track the frame
 * position does not fire until after the window drag operation has completed (i.e. the mouse pointer is released).
 * <p>
 * A further limitation is that the overlay will not appear when full-screen exclusive mode is used - if an overlay is
 * required in full-screen mode then the full-screen mode must be simulated (by re-sizing the main window, removing
 * decorations and so on).
 * <p>
 * If an overlay is used, then because the window is required to be non-opaque then it will appear in front of
 * <strong>all</strong> other desktop windows, including application dialog windows. For this reason, it may be
 * necessary to disable the overlay while displaying dialog boxes, or when the window is deactivated.
 * <p>
 * The overlay implementation in this class simply keeps a supplied window in sync with the video surface. It is the
 * responsibility of the client application itself to supply an appropriate overlay component.
 */
public class EmbeddedMediaPlayer extends MediaPlayer {

    /**
     * Libvlc instance.
     */
    protected final libvlc_instance_t libvlcInstance;

    private final FullScreenApi   fullScreenApi;
    private final InputApi        inputApi;
    private final OverlayApi      overlayApi;
    private final VideoSurfaceApi videoSurfaceApi;

    /**
     * Create a new media player.
     * <p>
     * Full-screen will not be supported.
     *
     * @param instance libvlc instance
     */
    public EmbeddedMediaPlayer(libvlc_instance_t instance) {
        super(instance);

        this.libvlcInstance = instance;

        this.fullScreenApi   = new FullScreenApi  (this);
        this.inputApi        = new InputApi       (this);
        this.overlayApi      = new OverlayApi     (this);
        this.videoSurfaceApi = new VideoSurfaceApi(this);
    }

    /**
     * Create a new media player.
     * <p>
     * Full-screen will not be supported.
     * <p>
     * This constructor for internal use only.
     *
     * @param instance libvlc instance
     * @param mediaPlayerInstance native media player instance
     */
    public EmbeddedMediaPlayer(libvlc_instance_t instance, libvlc_media_player_t mediaPlayerInstance) {
        super(instance, mediaPlayerInstance);

        this.libvlcInstance = instance;

        this.fullScreenApi   = new FullScreenApi  (this);
        this.inputApi        = new InputApi       (this);
        this.overlayApi      = new OverlayApi     (this);
        this.videoSurfaceApi = new VideoSurfaceApi(this);
    }

    public final FullScreenApi fullScreen() {
        return fullScreenApi;
    }

    public final InputApi input() {
        return inputApi;
    }

    public final OverlayApi overlay() {
        return overlayApi;
    }

    public final VideoSurfaceApi videoSurface() {
        return videoSurfaceApi;
    }

    protected final void onBeforePlay() {
        videoSurface().attachVideoSurface();
    }

    @Override
    protected final void onBeforeRelease() {
        fullScreenApi  .release();
        inputApi       .release();
        overlayApi     .release();
        videoSurfaceApi.release();
    }

}
