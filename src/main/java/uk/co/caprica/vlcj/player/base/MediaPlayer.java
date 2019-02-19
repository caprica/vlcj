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

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_renderer_item_t;
import uk.co.caprica.vlcj.player.renderer.RendererItem;
import uk.co.caprica.vlcj.support.eventmanager.TaskExecutor;

/**
 * Base media player implementation.
 * <p>
 * This can be used for those media players that do not require a video surface embedded into a user interface, for
 * example audio-only players, or less likely media players that will use a native window created by LibVLC.
 */
public class MediaPlayer {

    /**
     * Native library interface.
     */
    protected final LibVlc libvlc;

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
     * Arbitrary object associated with this media list player.
     */
    private Object userData;

    private final AudioService      audioService;
    private final ChapterService    chapterService;
    private final ControlsService   controlsService;
    private final EventService      eventService;
    private final LogoService       logoService;
    private final MarqueeService    marqueeService;
    private final MediaService      mediaService;
    private final MenuService       menuService;
    private final RoleService       roleService;
    private final SnapshotService   snapshotService;
    private final StatusService     statusService;
    private final SubitemService    subitemService;
    private final SubpictureService subpictureService;
    private final TeletextService   teletextService;
    private final TitleService      titleService;
    private final VideoService      videoService;

    /**
     * Create a new media player.
     *
     * @param libvlc native library interface
     * @param instance libvlc instance
     */
    public MediaPlayer(LibVlc libvlc, libvlc_instance_t instance) {
        this.libvlc         = libvlc;
        this.libvlcInstance = instance;

        this.mediaPlayerInstance = newNativeMediaPlayer();

        audioService      = new AudioService     (this);
        chapterService    = new ChapterService   (this);
        controlsService   = new ControlsService  (this);
        eventService      = new EventService     (this);
        logoService       = new LogoService      (this);
        marqueeService    = new MarqueeService   (this);
        mediaService      = new MediaService     (this);
        menuService       = new MenuService      (this);
        roleService       = new RoleService      (this);
        snapshotService   = new SnapshotService  (this);
        statusService     = new StatusService    (this);
        subitemService    = new SubitemService   (this);
        subpictureService = new SubpictureService(this);
        teletextService   = new TeletextService  (this);
        titleService      = new TitleService     (this);
        videoService      = new VideoService     (this);
    }

    private libvlc_media_player_t newNativeMediaPlayer() {
        libvlc_media_player_t result = libvlc.libvlc_media_player_new(libvlcInstance);
        if (result != null) {
            return result;
        } else {
            throw new RuntimeException("Failed to get a new native media player instance");
        }
    }

    public final AudioService audio() {
        return audioService;
    }

    public final ChapterService chapters() {
        return chapterService;
    }

    public final ControlsService controls() {
        return controlsService;
    }

    public final EventService events() {
        return eventService;
    }

    public final LogoService logo() {
        return logoService;
    }

    public final MarqueeService marquee() {
        return marqueeService;
    }

    public final MediaService media() {
        return mediaService;
    }

    public final MenuService menu() {
        return menuService;
    }

    public final RoleService role() {
        return roleService;
    }

    public final SnapshotService snapshots() {
        return snapshotService;
    }

    public final StatusService status() {
        return statusService;
    }

    public final SubitemService subitems() {
        return subitemService;
    }

    public final SubpictureService subpictures() {
        return subpictureService;
    }

    public final TeletextService teletext() {
        return teletextService;
    }

    public final TitleService titles() {
        return titleService;
    }

    public final VideoService video() {
        return videoService;
    }

    /**
     * Set an alternate media renderer.
     * <p>
     * This must be set before playback starts.
     *
     * @param rendererItem media renderer, or <code>null</code> to render as normal
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public final boolean setRenderer(RendererItem rendererItem) {
        libvlc_renderer_item_t rendererItemInstance = rendererItem != null ? rendererItem.rendererItemInstance() : null;
        return libvlc.libvlc_media_player_set_renderer(mediaPlayerInstance, rendererItemInstance) == 0;
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

        audioService     .release();
        chapterService   .release();
        controlsService  .release();
        eventService     .release();
        logoService      .release();
        marqueeService   .release();
        mediaService     .release();
        menuService      .release();
        roleService      .release();
        snapshotService  .release();
        statusService    .release();
        subitemService   .release();
        subpictureService.release();
        teletextService  .release();
        titleService     .release();
        videoService     .release();

        libvlc.libvlc_media_player_release(mediaPlayerInstance);

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
