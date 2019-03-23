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

// Was 2500 lines

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_renderer_item_t;
import uk.co.caprica.vlcj.player.renderer.RendererItem;
import uk.co.caprica.vlcj.support.eventmanager.TaskExecutor;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_new;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_set_renderer;

/**
 * Base media player implementation.
 * <p>
 * This can be used for those media players that do not require a video surface embedded into a user interface, for
 * example audio-only players, or less likely media players that will use a native window created by LibVLC.
 */
public class MediaPlayer {

    /**
     * Libvlc instance.
     */
    protected final libvlc_instance_t libvlcInstance;

    /**
     * Native media player instance.
     */
    private final libvlc_media_player_t mediaPlayerInstance;

    /**
     * Single-threaded service to execute tasks that need to be off-loaded from a native callback thread.
     * <p>
     * See {@link #submit(Runnable)}.
     */
    private final TaskExecutor executor = new TaskExecutor();

    /**
     * Optional alternate renderer.
     */
    private RendererItem renderer;

    /**
     * Arbitrary object associated with this media list player.
     */
    private Object userData;

    private final AudioApi      audioApi;
    private final ChapterApi    chapterApi;
    private final ControlsApi   controlsApi;
    private final EventApi      eventApi;
    private final LogoApi       logoApi;
    private final MarqueeApi    marqueeApi;
    private final MediaApi      mediaApi;
    private final MenuApi       menuApi;
    private final RoleApi       roleApi;
    private final SnapshotApi   snapshotApi;
    private final StatusApi     statusApi;
    private final SubitemApi    subitemApi;
    private final SubpictureApi subpictureApi;
    private final TeletextApi   teletextApi;
    private final TitleApi      titleApi;
    private final VideoApi      videoApi;

    /**
     * Create a new media player.
     *
     * @param instance libvlc instance
     */
    public MediaPlayer(libvlc_instance_t instance) {
        this.libvlcInstance = instance;

        this.mediaPlayerInstance = newNativeMediaPlayer();

        audioApi      = new AudioApi     (this);
        chapterApi    = new ChapterApi   (this);
        controlsApi   = new ControlsApi  (this);
        eventApi      = new EventApi     (this);
        logoApi       = new LogoApi      (this);
        marqueeApi    = new MarqueeApi   (this);
        mediaApi      = new MediaApi     (this);
        menuApi       = new MenuApi      (this);
        roleApi       = new RoleApi      (this);
        snapshotApi   = new SnapshotApi  (this);
        statusApi     = new StatusApi    (this);
        subitemApi    = new SubitemApi   (this);
        subpictureApi = new SubpictureApi(this);
        teletextApi   = new TeletextApi  (this);
        titleApi      = new TitleApi     (this);
        videoApi      = new VideoApi     (this);
    }

    private libvlc_media_player_t newNativeMediaPlayer() {
        libvlc_media_player_t result = libvlc_media_player_new(libvlcInstance);
        if (result != null) {
            return result;
        } else {
            throw new RuntimeException("Failed to get a new native media player instance");
        }
    }

    public final AudioApi audio() {
        return audioApi;
    }

    public final ChapterApi chapters() {
        return chapterApi;
    }

    public final ControlsApi controls() {
        return controlsApi;
    }

    public final EventApi events() {
        return eventApi;
    }

    public final LogoApi logo() {
        return logoApi;
    }

    public final MarqueeApi marquee() {
        return marqueeApi;
    }

    public final MediaApi media() {
        return mediaApi;
    }

    public final MenuApi menu() {
        return menuApi;
    }

    public final RoleApi role() {
        return roleApi;
    }

    public final SnapshotApi snapshots() {
        return snapshotApi;
    }

    public final StatusApi status() {
        return statusApi;
    }

    public final SubitemApi subitems() {
        return subitemApi;
    }

    public final SubpictureApi subpictures() {
        return subpictureApi;
    }

    public final TeletextApi teletext() {
        return teletextApi;
    }

    public final TitleApi titles() {
        return titleApi;
    }

    public final VideoApi video() {
        return videoApi;
    }

    /**
     * Set an alternate media renderer.
     * <p>
     * If the supplied renderer item is not <code>null</code> this component will invoke {@link RendererItem#hold()}.
     * <p>
     * If a renderer was previously set, RendererItem{@link RendererItem#release()} will be invoked.
     * <p>
     * A client application therefore need not, although it may, concern itself with hold/release.
     * <p>
     * If the new renderer could not be set it will be properly released (i.e. the hold acquired in this method will not
     * be kept).
     *
     * @param rendererItem media renderer, or <code>null</code> to render as normal
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public final boolean setRenderer(RendererItem rendererItem) {
        if (rendererItem != null) {
            if (!rendererItem.hold()) {
                return false;
            }
        }
        libvlc_renderer_item_t rendererItemInstance = rendererItem != null ? rendererItem.rendererItemInstance() : null;
        boolean result = libvlc_media_player_set_renderer(mediaPlayerInstance, rendererItemInstance) == 0;
        if (result) {
            if (this.renderer != null) {
                this.renderer.release();
            }
            this.renderer = rendererItem;
        } else {
            if (rendererItem != null) {
                rendererItem.release();
            }
        }
        return result;
    }

    /**
     * Get the user data associated with the media player.
     *
     * @return user data
     */
    public final Object userData() {
        return userData;
    }

    /**
     * Set user data to associate with the media player.
     *
     * @param userData user data
     */
    public final void userData(Object userData) {
        this.userData = userData;
    }

    /**
     * Submit a task for asynchronous execution.
     * <p>
     * This is useful in particular for event handling code as native events are generated on a native event callback
     * thread and it is not allowed to call back into LibVLC from this callback thread. If you do, either the call will
     * be ineffective, strange behaviour will happen, or a fatal JVM crash may occur.
     * <p>
     * To mitigate this, those tasks can be offloaded from the native thread, serialised and executed using this method.
     *
     * @param r task to execute
     */
    public final void submit(Runnable r) {
        executor.submit(r);
    }

    /**
     * Release the media player, freeing all associated (including native) resources.
     */
    public final void release() {
        executor.release();

        onBeforeRelease();

        audioApi     .release();
        chapterApi   .release();
        controlsApi  .release();
        eventApi     .release();
        logoApi      .release();
        marqueeApi   .release();
        mediaApi     .release();
        menuApi      .release();
        roleApi      .release();
        snapshotApi  .release();
        statusApi    .release();
        subitemApi   .release();
        subpictureApi.release();
        teletextApi  .release();
        titleApi     .release();
        videoApi     .release();

        libvlc_media_player_release(mediaPlayerInstance);

        onAfterRelease();
    }

    /**
     * Provided to enable sub-classes to do something just before the video is started.
     */
    protected void onBeforePlay() {
        // Base implementation does nothing
    }

    /**
     * Provided to enable sub-classes to implement their own clean-up immediately before the media player resources will
     * be freed.
     */
    protected void onBeforeRelease() {
        // Base implementation does nothing
    }

    /**
     * Provided to enable sub-classes to implement their own clean-up immediately after the media player resources have
     * been freed.
     */
    protected void onAfterRelease() {
        // Base implementation does nothing
    }

    /**
     * Provide access to the native media player instance.
     * <p>
     * This is exposed on the interface as an implementation side-effect, ordinary applications are not expected to use
     * this.
     *
     * @return media player instance
     */
    public final libvlc_media_player_t mediaPlayerInstance() {
        return mediaPlayerInstance;
    }

}
