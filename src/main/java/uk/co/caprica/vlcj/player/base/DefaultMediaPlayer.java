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
import uk.co.caprica.vlcj.binding.internal.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// FIXME
//  still need to think about what Interfaces i actually need (e.g. vlcj-pro impact is currently unknown)

// FIXME
//  would be nice to totally ditch the loggers from everywhere

// FIXME don't forget, we should do something similar with EmbeddedMediaPlayer and so on

// FIXME could almost be renamed BaseMediaPlayer instead of Default

// FIXME rename instance to libvlcInstance ?

/**
 * Base media player implementation.
 */
public abstract class DefaultMediaPlayer implements MediaPlayer {

    /**
     * Native library interface.
     */
    protected final LibVlc libvlc;

    /**
     * Libvlc instance.
     */
    protected final libvlc_instance_t libvlcInstance;

    /**
     * Single-threaded service to execute tasks that need to be off-loaded from a native callback thread.
     * <p>
     * Native events are generated on a native event callback thread. It is not allowed to call back into LibVLC from
     * this thread, if you do either the call will be ineffective, strange behaviour will happen, or a fatal JVM crash
     * may occur.
     * <p>
     * To mitigate this, tasks can be serialised and executed using this service.
     * <p>
     * See {@link #submit(Runnable)}.
     */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Native media player instance.
     */
    private final libvlc_media_player_t mediaPlayerInstance;

    private final AudioService      audioService;
    private final ChapterService    chapterService;
    private final ControlsService   controlsService;
    private final EventService      eventService;
    private final InfoService       infoService;
    private final LogoService       logoService;
    private final MarqueeService    marqueeService;
    private final MediaService      mediaService;
    private final MenuService       menuService;
    private final ParseService      parseService;
    private final RoleService       roleService;
    private final SlaveService      slaveService;
    private final SnapshotService   snapshotService;
    private final StatsService      statsService;
    private final StatusService     statusService;
    private final SubItemService    subItemService;
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
    protected DefaultMediaPlayer(LibVlc libvlc, libvlc_instance_t instance) {
        this.libvlc   = libvlc;
        this.libvlcInstance = instance;

        this.mediaPlayerInstance = newNativeMediaPlayer();

        audioService      = new AudioService     (this);
        chapterService    = new ChapterService   (this);
        controlsService   = new ControlsService  (this);
        eventService      = new EventService     (this);
        infoService       = new InfoService      (this);
        logoService       = new LogoService      (this);
        marqueeService    = new MarqueeService   (this);
        mediaService      = new MediaService     (this);
        menuService       = new MenuService      (this);
        parseService      = new ParseService     (this);
        roleService       = new RoleService      (this);
        slaveService      = new SlaveService     (this);
        snapshotService   = new SnapshotService  (this);
        statsService      = new StatsService     (this);
        statusService     = new StatusService    (this);
        subItemService    = new SubItemService   (this);
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
    public AudioService audio() {
        return audioService;
    }

    @Override
    public ChapterService chapters() {
        return chapterService;
    }

    @Override
    public ControlsService controls() {
        return controlsService;
    }

    @Override
    public EventService events() {
        return eventService;
    }

    @Override
    public InfoService info() {
        return infoService;
    }

    @Override
    public LogoService logo() {
        return logoService;
    }

    @Override
    public MediaService media() {
        return mediaService;
    }

    @Override
    public MarqueeService marquee() {
        return marqueeService;
    }

    @Override
    public MenuService menu() {
        return menuService;
    }

    @Override
    public ParseService parsing() {
        return parseService;
    }

    @Override
    public RoleService role() {
        return roleService;
    }

    @Override
    public SlaveService slave() {
        return slaveService;
    }

    @Override
    public SnapshotService snapshots() {
        return snapshotService;
    }

    @Override
    public StatsService statistics() {
        return statsService;
    }

    @Override
    public StatusService status() {
        return statusService;
    }

    @Override
    public SubItemService subItems() {
        return subItemService;
    }

    @Override
    public SubpictureService subpictures() {
        return subpictureService;
    }

    @Override
    public TeletextService teletext() {
        return teletextService;
    }

    @Override
    public TitleService titles() {
        return titleService;
    }

    @Override
    public VideoService video() {
        return videoService;
    }

    @Override
    public final void release() {
        shutdownExecutor();

        onBeforeRelease();

        audioService     .release();
        chapterService   .release();
        controlsService  .release();
        eventService     .release();
        infoService      .release();
        logoService      .release();
        marqueeService   .release();
        mediaService     .release();
        menuService      .release();
        parseService     .release();
        roleService      .release();
        slaveService     .release();
        snapshotService  .release();
        statsService     .release();
        statusService    .release();
        subItemService   .release();
        subpictureService.release();
        teletextService  .release();
        titleService     .release();
        userDataService  .release();
        videoService     .release();

        libvlc.libvlc_media_player_release(mediaPlayerInstance);

        onAfterRelease();
    }

    /**
     * Shutdown the task executor service.
     * <p>
     * Care must be taken to prevent fatal JVM crashes during shutdown due to tasks that may be still be waiting in the
     * queue to be executed (e.g. we do not want to destroy the native media player if a task is running that is going
     * to invoke a call on the native media player).
     * <p>
     * So, we first shutdown the executor service then await termination of all tasks. If there are no pending tasks we
     * will terminate immediately as normal. If there are tasks, we wait for a short timeout period before forcing a
     * termination anyway.
     * <p>
     * We should never really be waiting any significant amount of time for the queued tasks to terminate because they
     * will be very few in number, and should execute very quickly anyway. Even the short wait before timeout may be
     * useful to avoid any hard crashes during clean-up.
     */
    private void shutdownExecutor() {
        executor.shutdownNow();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
        }
    }

    /**
     *
     *
     * @param r
     */
    @Override
    public final void submit(Runnable r) {
        executor.submit(r);
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
