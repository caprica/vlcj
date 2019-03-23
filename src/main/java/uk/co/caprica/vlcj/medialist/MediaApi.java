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

package uk.co.caprica.vlcj.medialist;

import uk.co.caprica.vlcj.binding.NativeString;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaFactory;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.callback.CallbackMedia;

import java.util.ArrayList;
import java.util.List;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_get_mrl;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_add_media;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_count;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_insert_media;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_is_readonly;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_item_at_index;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_lock;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_remove_index;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_list_unlock;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_release;

/**
 * Behaviour pertaining to the media items in the list.
 */
public final class MediaApi extends BaseApi {

    MediaApi(MediaList mediaList) {
        super(mediaList);
    }

    /**
     * Add a new item to the list for a media resource locator.
     * <p>
     * The list must not be read-only.
     *
     * @param mrl media resource locator
     * @param options options to add to the media
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean add(String mrl, String... options) {
        return add(MediaFactory.newMediaRef(libvlcInstance, mrl, options));
    }

    /**
     * Add a new item to the list for native callback media.
     * <p>
     * The list must not be read-only.
     *
     * @param callbackMedia callback media component
     * @param options options to add to the media
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean add(CallbackMedia callbackMedia, String... options) {
        return add(MediaFactory.newMediaRef(libvlcInstance, callbackMedia, options));
    }

    /**
     * Add a new item to the list for a {@link MediaRef}.
     * <p>
     * The list must not be read-only.
     *
     * @param mediaRef media reference
     * @param options options to add to the media
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean add(MediaRef mediaRef, String... options) {
        return add(MediaFactory.newMediaRef(libvlcInstance, mediaRef, options));
    }

    private boolean add(MediaRef mediaRef) {
        if (mediaRef != null && !isReadOnly()) {
            lock();
            try {
                return libvlc_media_list_add_media(mediaListInstance, mediaRef.mediaInstance()) == 0;
            }
            finally {
                unlock();
                mediaRef.release();
            }
        } else {
            return false;
        }
    }

    /**
     * Insert an item into the list for a media resource locator.
     * <p>
     * The list must not be read-only.
     *
     * @param index index at which to insert the item
     * @param mrl media resource locator
     * @param options options to add to the media
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean insert(int index, String mrl, String... options) {
        return insert(index, MediaFactory.newMediaRef(libvlcInstance, mrl, options));
    }

    /**
     * Insert an item into the last for native callback media.
     * <p>
     * The list must not be read-only.
     *
     * @param index index at which to insert the item
     * @param callbackMedia callback media component
     * @param options options to add to the media
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean insert(int index, CallbackMedia callbackMedia, String... options) {
        return insert(index, MediaFactory.newMediaRef(libvlcInstance, callbackMedia, options));
    }

    /**
     * Insert an item into the list for a {@link MediaRef}.
     * <p>
     * The list must not be read-only.
     *
     * @param index index at which to insert the item
     * @param mediaRef media reference
     * @param options options to add to the media
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean insert(int index, MediaRef mediaRef, String... options) {
        return insert(index, MediaFactory.newMediaRef(libvlcInstance, mediaRef, options));
    }

    private boolean insert(int index, MediaRef mediaRef) {
        if (mediaRef != null && !isReadOnly()) {
            lock();
            try {
                return libvlc_media_list_insert_media(mediaListInstance, mediaRef.mediaInstance(), index) == 0;
            }
            finally {
                unlock();
                mediaRef.release();
            }
        } else {
            return false;
        }
    }

    /**
     * Remove an item from the list.
     *
     * @param index index of the item to remove
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean remove(int index) {
        if (!isReadOnly()) {
            lock();
            try {
                return libvlc_media_list_remove_index(mediaListInstance, index) == 0;
            }
            finally {
                unlock();
            }
        } else {
            return false;
        }
    }

    /**
     * Clear the entire list.
     *
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean clear() {
        if (!isReadOnly()) {
            lock();
            try {
                int result;
                do {
                    result = libvlc_media_list_remove_index(mediaListInstance, 0);
                } while (result == 0);
                return true;
            }
            finally {
                unlock();
            }
        } else {
            return false;
        }
    }

    /**
     * Get the number of items currently in the list.
     *
     * @return item count
     */
    public int count() {
        lock();
        try {
            return libvlc_media_list_count(mediaListInstance);
        }
        finally {
            unlock();
        }
    }

    /**
     * Get a list of media resource locators for the items in the list.
     *
     * @return media resource locators
     */
    public List<String> mrls() {
        lock();
        try {
            int count = libvlc_media_list_count(mediaListInstance);
            List<String> result = new ArrayList<String>(count);
            for (int i = 0; i < count; i++) {
                libvlc_media_t item = libvlc_media_list_item_at_index(mediaListInstance, i);
                result.add(NativeString.copyNativeString(libvlc_media_get_mrl(item)));
                libvlc_media_release(item);
            }
            return result;
        }
        finally {
            unlock();
        }
    }

    /**
     * Get the media resource locator for a particular item in the list.
     *
     * @param index item index
     * @return media resource locator
     */
    public String mrl(int index) {
        lock();
        try {
            libvlc_media_t media = libvlc_media_list_item_at_index(mediaListInstance, index);
            if (media != null) {
                try {
                    return NativeString.copyNativeString(libvlc_media_get_mrl(media));
                }
                finally {
                    libvlc_media_release(media);
                }
            } else {
                return null;
            }

        }
        finally {
            unlock();
        }
    }

    /**
     * Get a new {@link MediaRef} for an item in the list.
     * <p>
     * The caller must release the returned {@link MediaRef} when it no longer has any use for it.
     *
     * @param index item index
     * @return media reference
     */
    public MediaRef newMediaRef(int index) {
        lock();
        try {
            libvlc_media_t media = libvlc_media_list_item_at_index(mediaListInstance, index);
            if (media != null) {
                return new MediaRef(libvlcInstance, media);
            } else {
                return null;
            }
        }
        finally {
            unlock();
        }
    }

    /**
     * Get a new {@link Media} for an item in the list.
     * <p>
     * The caller must release the returned {@link Media} when it no longer has any use for it.
     *
     * @param index item index
     * @return media
     */
    public Media newMedia(int index) {
        lock();
        try {
            libvlc_media_t media = libvlc_media_list_item_at_index(mediaListInstance, index);
            if (media != null) {
                return new Media(libvlcInstance, media);
            } else {
                return null;
            }
        }
        finally {
            unlock();
        }
    }

    /**
     * Is the list read-only?
     *
     * @return <code>true</code> if the list is read-only; <code>false</code> if it is not
     */
    public boolean isReadOnly() {
        return libvlc_media_list_is_readonly(mediaListInstance) != 0;
    }

    /**
     * Create a new {@link MediaList} from this list.
     * <p>
     * The caller must release the returned {@link MediaList} when it no longer has any use for it.
     *
     * @return media list
     */
    public MediaList newMediaList() {
        return new MediaList(libvlcInstance, mediaListInstance);
    }

    /**
     * Create a new {@link MediaListRef} from this list.
     * <p>
     * The caller must release the returned {@link MediaListRef} when it no longer has any use for it.
     *
     * @return media list reference
     */
    public MediaListRef newMediaListRef() {
        return new MediaListRef(libvlcInstance, mediaListInstance);
    }

    private void lock() {
        libvlc_media_list_lock(mediaListInstance);
    }

    private void unlock() {
        libvlc_media_list_unlock(mediaListInstance);
    }

}
