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
import uk.co.caprica.vlcj.support.eventmanager.TaskExecutor;

// FIXME
//  still need to think about what Interfaces i actually need (e.g. vlcj-pro impact is currently unknown)

// FIXME could almost be renamed BaseMediaPlayer instead of Default

// all the services should be renamed more simply like VideoSurface -> Video, SnapshotService -> Snapshots and so on

/**
 * Base media player implementation.
 */
public class DefaultMediaPlayer implements MediaPlayer {

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

    private final AudioService      audioService;
    private final ChapterService    chapterService;
    private final ControlsService   controlsService;
    private final EventService      eventService;
    private final LogoService       logoService;
    private final MarqueeService    marqueeService;
    private final MediaService      mediaService;
    private final MenuService       menuService;
    private final RoleService       roleService;
    private final SlaveService      slaveService;
    private final SnapshotService   snapshotService;
    private final StatusService     statusService;
    private final SubitemService    subitemService;
    private final SubpictureService subpictureService;
    private final TeletextService   teletextService;
    private final TitleService      titleService;
    private final UserDataService   userDataService;
    private final VideoService      videoService;

    /**
     * Create a new media player.
     *
     * @param libvlc native library interface
     * @param instance libvlc instance
     */
    public DefaultMediaPlayer(LibVlc libvlc, libvlc_instance_t instance) {
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
        slaveService      = new SlaveService     (this);
        snapshotService   = new SnapshotService  (this);
        statusService     = new StatusService    (this);
        subitemService    = new SubitemService(this);
        subpictureService = new SubpictureService(this);
        teletextService   = new TeletextService  (this);
        titleService      = new TitleService     (this);
        userDataService   = new UserDataService  (this);
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

    @Override
    public final AudioService audio() {
        return audioService;
    }

    @Override
    public final ChapterService chapters() {
        return chapterService;
    }

    @Override
    public final ControlsService controls() {
        return controlsService;
    }

    @Override
    public final EventService events() {
        return eventService;
    }

    @Override
    public final LogoService logo() {
        return logoService;
    }

    @Override
    public final MarqueeService marquee() {
        return marqueeService;
    }

    @Override
    public final MediaService media() {
        return mediaService;
    }

    @Override
    public final MenuService menu() {
        return menuService;
    }

    @Override
    public final RoleService role() {
        return roleService;
    }

    @Override
    public final SlaveService slave() {
        return slaveService;
    }

    @Override
    public final SnapshotService snapshots() {
        return snapshotService;
    }

    @Override
    public final StatusService status() {
        return statusService;
    }

    @Override
    public final SubitemService subitems() {
        return subitemService;
    }

    @Override
    public final SubpictureService subpictures() {
        return subpictureService;
    }

    @Override
    public final TeletextService teletext() {
        return teletextService;
    }

    @Override
    public final TitleService titles() {
        return titleService;
    }

    @Override
    public final VideoService video() {
        return videoService;
    }

    @Override
    public final void submit(Runnable r) {
        executor.submit(r);
    }

    @Override
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
        slaveService     .release();
        snapshotService  .release();
        statusService    .release();
        subitemService   .release();
        subpictureService.release();
        teletextService  .release();
        titleService     .release();
        userDataService  .release();
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

    // this is the only thing i'm not quite happy with right now, ideally just protected final, but let's see it's not so bad
    @Override
    public final libvlc_media_player_t mediaPlayerInstance() {
        return mediaPlayerInstance;
    } // FIXME this is used unfortunately by the other media players... not sure what to do - why is it not just protected? some things like VideoSurfaceAdapter need it, could try and refactor that
    //    ideally this would just be a protected final class field

}
