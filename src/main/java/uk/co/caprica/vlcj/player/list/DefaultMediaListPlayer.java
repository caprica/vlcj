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

package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_player_t;
import uk.co.caprica.vlcj.eventmanager.TaskExecutor;

import java.util.concurrent.TimeUnit;

/**
 * Implementation of a media list player.
 * <p>
 * The native media list player will automatically deal properly with media that has subitems (like YouTube movies), so
 * simply adding an ordinary MRL/URL is all that is needed for such media.
 */
public class DefaultMediaListPlayer implements MediaListPlayer {

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
    private libvlc_media_list_player_t mediaListPlayerInstance;

    /**
     * Single-threaded service to execute tasks that need to be off-loaded from a native callback thread.
     * <p>
     * See {@link #submit(Runnable)}.
     */
    private final TaskExecutor executor = new TaskExecutor();

    private final ControlsService    controlsService;
    private final EventService       eventService;
    private final ListService        listService;
    private final MediaPlayerService mediaPlayerService;
    private final StatusService      statusService;
    private final UserDataService    userDataService;

    /**
     * Create a new media list player.
     *
     * @param libvlc native library interface
     * @param libvlcInstance libvlc instance
     */
    public DefaultMediaListPlayer(LibVlc libvlc, libvlc_instance_t libvlcInstance) {
        this.libvlc         = libvlc;
        this.libvlcInstance = libvlcInstance;

        // Add event handlers for internal implementation - the order in which these are added (and therefore the order
        // in which they execute) is important in some cases (as per the individual class Javadoc)
        this.mediaListPlayerInstance = newNativeMediaListPlayer();

        this.controlsService    = new ControlsService   (this);
        this.eventService       = new EventService      (this);
        this.listService        = new ListService       (this);
        this.mediaPlayerService = new MediaPlayerService(this);
        this.statusService      = new StatusService     (this);
        this.userDataService    = new UserDataService   (this);
    }

    private libvlc_media_list_player_t newNativeMediaListPlayer() {
        libvlc_media_list_player_t result = libvlc.libvlc_media_list_player_new(libvlcInstance);
        if (result != null) {
            return result;
        } else {
            throw new RuntimeException("Failed to get a new native media list player instance");
        }
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
    public ListService list() {
        return listService;
    }

    @Override
    public MediaPlayerService mediaPlayer() {
        return mediaPlayerService;
    }

    @Override
    public StatusService status() {
        return statusService;
    }

    @Override
    public UserDataService userData() {
        return userDataService;
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
    @Override
    public final void submit(Runnable r) {
        executor.submit(r);
    }

    @Override
    public final void release() {
        executor.release();

        onBeforeRelease();

        controlsService   .release();
        eventService      .release();
        listService       .release();
        mediaPlayerService.release();
        statusService     .release();
        userDataService   .release();

        libvlc.libvlc_media_list_player_release(mediaListPlayerInstance);

        onAfterRelease();
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

    @Override
    public final libvlc_media_list_player_t mediaListPlayerInstance() {
        return mediaListPlayerInstance;
    } // FIXME check it has to be public and on the interface, ideally no...

}
