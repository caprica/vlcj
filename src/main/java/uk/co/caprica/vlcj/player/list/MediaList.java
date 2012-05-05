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
 * Copyright 2009, 2010, 2011, 2012 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.logger.Logger;

/**
 * A media list (i.e. a play-list).
 * <p>
 * To do anything more advanced than the functionality provided by this class, the underlying native
 * media list instance is accessible via {@link #mediaListInstance}.
 */
public class MediaList {

    /**
     * Native interface.
     */
    private LibVlc libvlc;

    /**
     * Native library instance.
     */
    private libvlc_instance_t instance;

    /**
     * Play-list instance.
     */
    private libvlc_media_list_t mediaListInstance;

    /**
     * Set to true when the media list has been released.
     */
    private final AtomicBoolean released = new AtomicBoolean();

    /**
     * Standard media options to be applied to each media item that is played.
     */
    private String[] standardMediaOptions;

    /**
     * Map of native media instances.
     * <p>
     * This is used so that an application may associate a native media instance with the MRL that
     * was used to create that instance.
     * <p>
     * It is possible simply to ask the native media instance what it's MRL is, but the reported MRL
     * (while functionally equivalent to the original MRL) may actually be different due to URL
     * encoding (the original MRL may not have been URL-encoded).
     */
    private final Map<libvlc_media_t, String> mediaListMap = new HashMap<libvlc_media_t, String>();

    /**
     * Create a new media list.
     * 
     * @param libvlc native interface
     * @param instance native library instance
     */
    public MediaList(LibVlc libvlc, libvlc_instance_t instance) {
        this.libvlc = libvlc;
        this.instance = instance;

        createInstance();
    }

    /**
     * Create a new media list.
     * 
     * @param libvlc native interface
     * @param instance native library instance
     * @param mediaListInstance native media list instance
     */
    public MediaList(LibVlc libvlc, libvlc_instance_t instance, libvlc_media_list_t mediaListInstance) {
        this.libvlc = libvlc;
        this.instance = instance;

        this.mediaListInstance = mediaListInstance;
    }

    /**
     * Set standard media options for all media items subsequently played.
     * <p>
     * This will <strong>not</strong> affect any currently playing media item.
     * 
     * @param standardMediaOptions options to apply to all subsequently played media items
     */
    public void setStandardMediaOptions(String... standardMediaOptions) {
        this.standardMediaOptions = standardMediaOptions;
    }

    /**
     * Add a media item, with options, to the play-list.
     * 
     * @param mrl media resource locator
     * @param mediaOptions zero or more media item options
     */
    public void addMedia(String mrl, String... mediaOptions) {
        Logger.debug("addMedia(mrl={},mediaOptions={})", mrl, mediaOptions);
        try {
            lock();
            // Create a new native media descriptor
            libvlc_media_t mediaDescriptor = newMediaDescriptor(mrl, mediaOptions);
            // Store the new native media descriptor in the MRL map
            mediaListMap.put(mediaDescriptor, mrl);
            // Insert the media descriptor into the media list
            libvlc.libvlc_media_list_add_media(mediaListInstance, mediaDescriptor);
            releaseMediaDescriptor(mediaDescriptor);
        }
        finally {
            unlock();
        }
    }

    /**
     * Insert a media item, with options, to the play-list.
     * 
     * @param index position at which to insert the media item (counting from zero)
     * @param mrl media resource locator
     * @param mediaOptions zero or more media item options
     */
    public void insertMedia(int index, String mrl, String... mediaOptions) {
        Logger.debug("insertMedia(index={},mrl={},mediaOptions={})", index, mrl, mediaOptions);
        try {
            lock();
            // Create a new native media descriptor
            libvlc_media_t mediaDescriptor = newMediaDescriptor(mrl, mediaOptions);
            // Store the new native media descriptor in the MRL map
            mediaListMap.put(mediaDescriptor, mrl);
            // Insert the media descriptor into the media list
            libvlc.libvlc_media_list_insert_media(mediaListInstance, mediaDescriptor, index);
            releaseMediaDescriptor(mediaDescriptor);
        }
        finally {
            unlock();
        }
    }

    /**
     * Remove a media item from the play-list.
     * 
     * @param index item to remove (counting from zero)
     */
    public void removeMedia(int index) {
        Logger.debug("removeMedia(index={})", index);
        try {
            lock();
            libvlc_media_t oldMediaInstance = libvlc.libvlc_media_list_item_at_index(mediaListInstance, index);
            if(oldMediaInstance != null) {
                // Remove the old native media descriptor from the MRL map
                mediaListMap.remove(oldMediaInstance);
                // Remove the media descriptor from the media list
                libvlc.libvlc_media_list_remove_index(mediaListInstance, index);
                // Release the native media instance
                libvlc.libvlc_media_release(oldMediaInstance);
            }
        }
        finally {
            unlock();
        }
    }

    /**
     * Clear the list.
     */
    public void clear() {
        Logger.debug("clear()");
        try {
            lock();
            for(int i = libvlc.libvlc_media_list_count(mediaListInstance)-1; i >= 0; i--) {
                libvlc.libvlc_media_list_remove_index(mediaListInstance, i);
            }
            mediaListMap.clear();
        }
        finally {
            unlock();
        }
    }

    /**
     * Get the number of items currently in the list.
     * 
     * @return item count
     */
    public int size() {
        Logger.debug("size()");
        try {
            lock();
            int size = libvlc.libvlc_media_list_count(mediaListInstance);
            return size;
        }
        finally {
            unlock();
        }
    }

    /**
     * Test if the play-list is read-only.
     * 
     * @return <code>true</code> if the play-list is currently read-only, otherwise
     *         <code>false</code>
     */
    public boolean isReadOnly() {
        Logger.debug("isReadOnly()");
        boolean readOnly = libvlc.libvlc_media_list_is_readonly(mediaListInstance) == 0;
        return readOnly;
    }

    /**
     * Get the media resource locators for all of the items in the list.
     * 
     * @return list of media resource locators
     */
    public List<String> mrls() {
        Logger.debug("mrls()");
        try {
            lock();
            int count = libvlc.libvlc_media_list_count(mediaListInstance);
            List<String> result = new ArrayList<String>(count);
            for(int i = 0; i < count; i ++ ) {
                libvlc_media_t mediaInstance = libvlc.libvlc_media_list_item_at_index(mediaListInstance, i);
                if(mediaInstance != null) {
                    result.add(mrl(mediaInstance));
                    libvlc.libvlc_media_release(mediaInstance);
                }
            }
            return result;
        }
        finally {
            unlock();
        }
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
     * Create and initialise a new media list instance.
     */
    private void createInstance() {
        Logger.debug("createInstance()");
        mediaListInstance = libvlc.libvlc_media_list_new(instance);
    }

    /**
     * Clean up and free the media list instance.
     */
    private void destroyInstance() {
        Logger.debug("destroyInstance()");
        if(mediaListInstance != null) {
            libvlc.libvlc_media_list_release(mediaListInstance);
        }
    }

    /**
     * Acquire the media list lock.
     */
    private void lock() {
        Logger.debug("lock()");
        libvlc.libvlc_media_list_lock(mediaListInstance);
    }

    /**
     * Release the media list lock.
     */
    private void unlock() {
        Logger.debug("unlock()");
        libvlc.libvlc_media_list_unlock(mediaListInstance);
    }

    /**
     * Create a new native media instance.
     * 
     * @param media media resource locator
     * @param mediaOptions zero or more media options
     * @return native media instance
     */
    private libvlc_media_t newMediaDescriptor(String media, String... mediaOptions) {
        Logger.debug("newMediaDescriptor(media={},mediaOptions={})", media, Arrays.toString(mediaOptions));
        libvlc_media_t mediaDescriptor = libvlc.libvlc_media_new_path(instance, media);
        Logger.debug("mediaDescriptor={}", mediaDescriptor);
        if(standardMediaOptions != null) {
            for(String standardMediaOption : standardMediaOptions) {
                Logger.debug("standardMediaOption={}", standardMediaOption);
                libvlc.libvlc_media_add_option(mediaDescriptor, standardMediaOption);
            }
        }
        if(mediaOptions != null) {
            for(String mediaOption : mediaOptions) {
                Logger.debug("mediaOption={}", mediaOption);
                libvlc.libvlc_media_add_option(mediaDescriptor, mediaOption);
            }
        }
        return mediaDescriptor;
    }

    /**
     * Release a native media instance.
     * 
     * @param mediaDescripor native media instance
     */
    private void releaseMediaDescriptor(libvlc_media_t mediaDescriptor) {
        Logger.debug("releaseMediaDescriptor(mediaDescriptor={})", mediaDescriptor);
        libvlc.libvlc_media_release(mediaDescriptor);
    }

    /**
     * Get the media resource locator for the given native media instance.
     * <p>
     * The media instance must have been added to the list - it must not be any
     * automatically/scripted added sub-item.
     * 
     * @param mediaInstance native media instance
     * @return media resource locator
     */
    public String mrl(libvlc_media_t mediaInstance) {
        return mediaListMap.get(mediaInstance);
    }

    /**
     * Get the native media list instance handle.
     * 
     * @return native media list handle
     */
    libvlc_media_list_t mediaListInstance() {
        return mediaListInstance;
    }
}
