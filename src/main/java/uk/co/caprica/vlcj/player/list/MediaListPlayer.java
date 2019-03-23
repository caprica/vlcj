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

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_player_t;
import uk.co.caprica.vlcj.support.eventmanager.TaskExecutor;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_new;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_player_release;

/**
 * Implementation of a media list player.
 * <p>
 * A media list player can be used with an embedded media player (without this a native video window will be opened when
 * video is played).
 * <p>
 * The native media list player will automatically deal properly with media that has subitems (like YouTube movies), so
 * simply adding an ordinary MRL/URL is all that is needed for such media.
 */
public final class MediaListPlayer {

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

    /**
     * Arbitrary object associated with this media list player.
     */
    private Object userData;

    private final ControlsApi    controlsApi;
    private final EventApi       eventApi;
    private final ListApi        listApi;
    private final MediaPlayerApi mediaPlayerApi;
    private final StatusApi      statusApi;

    /**
     * Create a new media list player.
     *
     * @param libvlcInstance libvlc instance
     */
    public MediaListPlayer(libvlc_instance_t libvlcInstance) {
        this.libvlcInstance = libvlcInstance;

        this.mediaListPlayerInstance = newNativeMediaListPlayer();

        this.controlsApi    = new ControlsApi   (this);
        this.eventApi       = new EventApi      (this);
        this.listApi        = new ListApi       (this);
        this.mediaPlayerApi = new MediaPlayerApi(this);
        this.statusApi      = new StatusApi     (this);
    }

    private libvlc_media_list_player_t newNativeMediaListPlayer() {
        libvlc_media_list_player_t result = libvlc_media_list_player_new(libvlcInstance);
        if (result != null) {
            return result;
        } else {
            throw new RuntimeException("Failed to get a new native media list player instance");
        }
    }

    public ControlsApi controls() {
        return controlsApi;
    }

    public EventApi events() {
        return eventApi;
    }

    public ListApi list() {
        return listApi;
    }

    public MediaPlayerApi mediaPlayer() {
        return mediaPlayerApi;
    }

    public StatusApi status() {
        return statusApi;
    }

    /**
     * Get the user data associated with the media player.
     *
     * @return user data
     */
    public Object userData() {
        return userData;
    }

    /**
     * Set user data to associate with the media player.
     *
     * @param userData user data
     */
    public void userData(Object userData) {
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
    public void submit(Runnable r) {
        executor.submit(r);
    }

    /**
     * Release the media player, freeing all associated (including native) resources.
     */
    public void release() {
        executor.release();

        onBeforeRelease();

        controlsApi   .release();
        eventApi      .release();
        listApi       .release();
        mediaPlayerApi.release();
        statusApi     .release();

        libvlc_media_list_player_release(mediaListPlayerInstance);

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

    /**
     * Provide access to the native media player instance.
     * <p>
     * This is exposed on the interface as an implementation side-effect, ordinary applications are not expected to use
     * this.
     *
     * @return media player instance
     */
    public libvlc_media_list_player_t mediaListPlayerInstance() {
        return mediaListPlayerInstance;
    }

}
