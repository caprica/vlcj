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

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.factory.LibVlcInstance;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.support.eventmanager.TaskExecutor;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_new;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_release;

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
    private final ProgramApi    programApi;
    private final RecordApi    recordApi;
    private final RendererApi   rendererApi;
    private final RoleApi       roleApi;
    private final SnapshotApi   snapshotApi;
    private final StatusApi     statusApi;
    private final SubitemApi    subitemApi;
    private final SubpictureApi subpictureApi;
    private final TeletextApi   teletextApi;
    private final TimeApi       timeApi;
    private final TitleApi      titleApi;
    private final TrackApi      trackApi;
    private final VideoApi      videoApi;

    /**
     * Create a new media player.
     *
     * @param instance libvlc instance
     */
    public MediaPlayer(libvlc_instance_t instance) {
        this(instance, newNativeMediaPlayer(instance));
    }

    /**
     * Create a new media player.
     * <p>
     * This constructor for internal use only.
     *
     * @param instance libvlc instance
     * @param mediaPlayerInstance native media player instance
     */
    public MediaPlayer(libvlc_instance_t instance, libvlc_media_player_t mediaPlayerInstance) {
        this.libvlcInstance = instance;
        this.mediaPlayerInstance = mediaPlayerInstance;

        audioApi      = new AudioApi     (this);
        chapterApi    = new ChapterApi   (this);
        controlsApi   = new ControlsApi  (this);
        eventApi      = new EventApi     (this);
        logoApi       = new LogoApi      (this);
        marqueeApi    = new MarqueeApi   (this);
        mediaApi      = new MediaApi     (this);
        menuApi       = new MenuApi      (this);
        programApi    = new ProgramApi   (this);
        recordApi     = new RecordApi    (this);
        rendererApi   = new RendererApi  (this);
        roleApi       = new RoleApi      (this);
        snapshotApi   = new SnapshotApi  (this);
        statusApi     = new StatusApi    (this);
        subitemApi    = new SubitemApi   (this);
        subpictureApi = new SubpictureApi(this);
        teletextApi   = new TeletextApi  (this);
        timeApi       = new TimeApi      (this);
        titleApi      = new TitleApi     (this);
        trackApi      = new TrackApi     (this);
        videoApi      = new VideoApi     (this);
    }

    /**
     * Create a new media player.
     * <p>
     * This constructor is intended for use by user applications that use media player sub-classing.
     * <p>
     * See {@link MediaPlayerFactory#getLibVlcInstance()}.
     *
     * @param instance opaque libvlc native library instance handle
     */
    public MediaPlayer(LibVlcInstance instance) {
        this(instance.get());
    }

    private static libvlc_media_player_t newNativeMediaPlayer(libvlc_instance_t instance) {
        libvlc_media_player_t result = libvlc_media_player_new(instance);
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

    public final ProgramApi programs() {
        return programApi;
    }

    public final RecordApi record() {
        return recordApi;
    }

    public final RendererApi renderer() {
        return rendererApi;
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

    public final TimeApi time() {
        return timeApi;
    }

    public final TitleApi titles() {
        return titleApi;
    }

    public final TrackApi tracks() {
        return trackApi;
    }

    public final VideoApi video() {
        return videoApi;
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
        programApi   .release();
        recordApi    .release();
        rendererApi  .release();
        roleApi      .release();
        snapshotApi  .release();
        statusApi    .release();
        subitemApi   .release();
        subpictureApi.release();
        teletextApi  .release();
        timeApi      .release();
        titleApi     .release();
        trackApi     .release();
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
