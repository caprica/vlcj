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

import uk.co.caprica.vlcj.binding.internal.*;
import uk.co.caprica.vlcj.callbackmedia.CallbackMedia;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.binding.NativeString;
import uk.co.caprica.vlcj.media.MediaFactory;
import uk.co.caprica.vlcj.media.MediaRef;

import java.util.ArrayList;
import java.util.List;

// FIXME consider rename, is this really MediaService? (would match the Media component i suppose)

public class ItemService extends BaseService {

    ItemService(MediaList mediaList) {
        super(mediaList);
    }

    public boolean add(String mrl, String... options) {
        return add(MediaFactory.newMediaRef(libvlc, libvlcInstance, mrl, options));
    }

    public boolean add(CallbackMedia callbackMedia, String... options) {
        return add(MediaFactory.newMediaRef(libvlc, libvlcInstance, callbackMedia, options));
    }

    public boolean add(MediaRef mediaRef, String... options) {
        return add(MediaFactory.newMediaRef(libvlc, libvlcInstance, mediaRef, options));
    }

    private boolean add(MediaRef mediaRef) {
        if (mediaRef != null && !isReadOnly()) {
            lock();
            try {
                return libvlc.libvlc_media_list_add_media(mediaListInstance, mediaRef.mediaInstance()) == 0;
            }
            finally {
                unlock();
                mediaRef.release();
            }
        } else {
            return false;
        }
    }

    public boolean insert(int index, String mrl, String... options) {
        return insert(index, MediaFactory.newMediaRef(libvlc, libvlcInstance, mrl, options));
    }

    public boolean insert(int index, CallbackMedia callbackMedia, String... options) {
        return insert(index, MediaFactory.newMediaRef(libvlc, libvlcInstance, callbackMedia, options));
    }

    public boolean insert(int index, MediaRef mediaRef, String... options) {
        return insert(index, MediaFactory.newMediaRef(libvlc, libvlcInstance, mediaRef, options));
    }

    private boolean insert(int index, MediaRef mediaRef) {
        if (mediaRef != null && !isReadOnly()) {
            lock();
            try {
                return libvlc.libvlc_media_list_insert_media(mediaListInstance, mediaRef.mediaInstance(), index) == 0;
            }
            finally {
                unlock();
                mediaRef.release();
            }
        } else {
            return false;
        }
    }

    public boolean remove(int index) {
        if (!isReadOnly()) {
            lock();
            try {
                return libvlc.libvlc_media_list_remove_index(mediaListInstance, index) == 0;
            }
            finally {
                unlock();
            }
        } else {
            return false;
        }
    }

    public boolean clear() {
        if (!isReadOnly()) {
            lock();
            try {
                int result;
                do {
                    result = libvlc.libvlc_media_list_remove_index(mediaListInstance, 0);
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
            return libvlc.libvlc_media_list_count(mediaListInstance);
        }
        finally {
            unlock();
        }
    }

    public List<String> mrls() {
        lock();
        try {
            int count = libvlc.libvlc_media_list_count(mediaListInstance);
            List<String> result = new ArrayList<String>(count);
            for (int i = 0; i < count; i++) {
                libvlc_media_t item = libvlc.libvlc_media_list_item_at_index(mediaListInstance, i);
                result.add(NativeString.copyNativeString(libvlc.libvlc_media_get_mrl(item)));
                libvlc.libvlc_media_release(item);
            }
            return result;
        }
        finally {
            unlock();
        }
    }

    public String mrl(int index) {
        lock();
        try {
            libvlc_media_t media = libvlc.libvlc_media_list_item_at_index(mediaListInstance, index);
            if (media != null) {
                try {
                    return NativeString.copyNativeString(libvlc.libvlc_media_get_mrl(media));
                }
                finally {
                    libvlc.libvlc_media_release(media);
                }
            } else {
                return null;
            }

        }
        finally {
            unlock();
        }
    }

    // client must release MediaRef
    public MediaRef newMediaRef(int index) {
        lock();
        try {
            libvlc_media_t media = libvlc.libvlc_media_list_item_at_index(mediaListInstance, index);
            if (media != null) {
                return new MediaRef(libvlc, libvlcInstance, media);
            } else {
                return null;
            }
        }
        finally {
            unlock();
        }
    }

    // client must release Media
    public Media newMedia(int index) {
        lock();
        try {
            libvlc_media_t media = libvlc.libvlc_media_list_item_at_index(mediaListInstance, index);
            if (media != null) {
                return new Media(libvlc, libvlcInstance, media);
            } else {
                return null;
            }
        }
        finally {
            unlock();
        }
    }

    public boolean isReadOnly() {
        return libvlc.libvlc_media_list_is_readonly(mediaListInstance) != 0;
    }

    private void lock() {
        libvlc.libvlc_media_list_lock(mediaListInstance);
    }

    private void unlock() {
        libvlc.libvlc_media_list_unlock(mediaListInstance);
    }

}
