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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.discoverer;

import java.util.concurrent.atomic.AtomicBoolean;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_discoverer_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.NativeString;

/**
 * Encapsulation of a native media discoverer component.
 * <p>
 * This is a minimal implementation providing a list of media <em>on-demand</em> - it
 * does not implement any events and will not automatically update if and when new
 * media is discovered.
 */
public class MediaDiscoverer {

    /**
     * Native interface.
     */
    private final LibVlc libvlc;

    /**
     * Native library instance.
     */
    private final libvlc_instance_t instance;

    /**
     * Discoverer name.
     * <p>
     * For example "audio", "video".
     */
    private final String name;

    /**
     * Native media discoverer instance.
     */
    private libvlc_media_discoverer_t mediaDiscovererInstance;

    /**
     * Set to true when the media discoverer has been released.
     */
    private final AtomicBoolean released = new AtomicBoolean();

    /**
     * Create a media discoverer.
     *
     * @param libvlc native interface
     * @param instance native library instance
     * @param name discoverer name
     */
    public MediaDiscoverer(LibVlc libvlc, libvlc_instance_t instance, String name) {
        this.libvlc = libvlc;
        this.instance = instance;
        this.name = name;
        createInstance();
    }

    /**
     * Get the name of this discoverer.
     *
     * @return name
     */
    public final String getName() {
        return name;
    }

    /**
     * Get the localised name of this discoverer.
     *
     * @return name
     */
    public final String getLocalisedName() {
        return NativeString.getNativeString(libvlc, libvlc.libvlc_media_discoverer_localized_name(mediaDiscovererInstance));
    }

    /**
     * Get the media list containing the discovered services/media.
     * <p>
     * This media list will likely contain nested sub-items, so the list should
     * be processed recursively to discover media items and their MRLs.
     *
     * @return media list
     */
    public final MediaList getMediaList() {
        libvlc_media_list_t mediaListInstance = libvlc.libvlc_media_discoverer_media_list(mediaDiscovererInstance);
        MediaList mediaList = new MediaList(libvlc, instance, mediaListInstance);
        libvlc.libvlc_media_list_release(mediaListInstance);
        return mediaList;
    }

    /**
     * Clean up media list resources.
     */
    public final void release() {
        Logger.debug("release()");
        if(released.compareAndSet(false, true)) {
            destroyInstance();
        }
    }

    /**
     * Create and initialise a new media discoverer instance.
     */
    private void createInstance() {
        Logger.debug("createInstance()");

        mediaDiscovererInstance = libvlc.libvlc_media_discoverer_new_from_name(instance, name);
        Logger.debug("mediaDiscovererInstance={}", mediaDiscovererInstance);

        if(mediaDiscovererInstance == null) {
            throw new IllegalArgumentException("No media discoverer for '" + name + "' is available on this platform");
        }
    }

    /**
     * Clean up and free the media list instance.
     */
    private void destroyInstance() {
        Logger.debug("destroyInstance()");

        if(mediaDiscovererInstance != null) {
            Logger.debug("Release media discoverer...");
            libvlc.libvlc_media_discoverer_release(mediaDiscovererInstance);
            Logger.debug("Media discoverer released.");
        }
    }
}
