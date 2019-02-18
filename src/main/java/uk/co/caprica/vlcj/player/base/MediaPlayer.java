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

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.renderer.RendererItem;

/**
 * Specification for a media player component.
 *
 * @see EmbeddedMediaPlayerComponent
 */
public interface MediaPlayer {

    /**
     * Behaviour pertaining to media player audio.
     *
     * @return audio behaviour
     */
    AudioService audio();

    /**
     * Behaviour pertaining to chapters.
     *
     * @return chapter behaviour
     */
    ChapterService chapters();

    /**
     * Behaviour pertaining to media player controls.
     *
     * @return controls behaviour
     */
    ControlsService controls();

    /**
     * Behaviour pertaining to media player events.
     *
     * @return event behaviour.
     */
    EventService events();

    /**
     * Behaviour pertaining to the logo.
     *
     * @return logo behaviour
     */
    LogoService logo();

    /**
     * Behaviour pertaining to the current media.
     *
     * @return media behaviour
     */
    MediaService media();

    /**
     * Behaviour pertaining to the marquee.
     *
     * @return marquee behaviour
     */
    MarqueeService marquee();

    /**
     * Behaviour pertaining to the menu.
     *
     * @return menu behaviour
     */
    MenuService menu();

    /**
     * Behaviour pertaining to the media player role.
     *
     * @return role behaviour
     */
    RoleService role();

    /**
     * Behaviour pertaining to video snapshots.
     *
     * @return snapshot behaviour
     */
    SnapshotService snapshots();

    /**
     * Behaviour pertaining to the status of the media player.
     *
     * @return status behaviour
     */
    StatusService status();

    /**
     * Behaviour pertaining to subitems.
     *
     * @return subitems behaviour
     */
    SubitemService subitems();

    /**
     * Behaviour pertaining to subpictures.
     *
     * @return subpicture behaviour
     */
    SubpictureService subpictures();

    /**
     * Behaviour pertaining to teletext.
     *
     * @return teletext behaviour
     */
    TeletextService teletext();

    /**
     * Behaviour pertaining to titles.
     *
     * @return titles behaviour
     */
    TitleService titles();

    /**
     * Behaviour pertaining to media player video.
     *
     * @return video behaviour
     */
    VideoService video();

    /**
     * Set an alternate media renderer.
     * <p>
     * This must be set before playback starts.
     *
     * @param rendererItem media renderer
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    boolean setRenderer(RendererItem rendererItem);

    /**
     * Get the user data associated with the media player.
     *
     * @return user data
     */
    Object userData();

    /**
     * Set user data to associate with the media player.
     *
     * @param userData user data
     */
    void userData(Object userData);

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
    void submit(Runnable r);

    /**
     * Release the media player, freeing all associated (including native) resources.
     */
    void release();

    /**
     * Provide access to the native media player instance.
     * <p>
     * This is exposed on the interface as an implementation side-effect, ordinary applications are not expected to use
     * this.
     *
     * @return media player instance
     */
    libvlc_media_player_t mediaPlayerInstance();

}
