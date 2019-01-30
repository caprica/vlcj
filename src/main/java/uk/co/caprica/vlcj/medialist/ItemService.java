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
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.binding.NativeString;

import java.util.ArrayList;
import java.util.List;

// FIXME consider rename
// FIXME really need to understand what's ref-counted when here... when adding / removing items
// FIXME is this really MediaService?

public class ItemService extends BaseService {

    ItemService(MediaList mediaList) {
        super(mediaList);
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

    /**
     * Add a media item, with options, to the play-list.
     *
     * @param mrl media resource locator
     * @param mediaOptions zero or more media item options
     */
    public boolean addMedia(Media media) {
        lock();
        try {
            return libvlc.libvlc_media_list_add_media(mediaListInstance, media.mediaInstance()) == 0;
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
    public boolean insertMedia(int index, Media media) {
        lock();
        try {
            return libvlc.libvlc_media_list_insert_media(mediaListInstance, media.mediaInstance(), index) == 0;
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
    public boolean removeMedia(int index) {
        lock();
        try {
            // Internally LibVLC is already releasing the media instance
            return libvlc.libvlc_media_list_remove_index(mediaListInstance, index) == 0;
        }
        finally {
            unlock();
        }
    }

    // FIXME....... it should return the same Media instance each time i think... that implies some sort of mirror'd list
    public Media getMedia(int index) {
        lock();
        try {
            libvlc_media_t media = libvlc.libvlc_media_list_item_at_index(mediaListInstance, index);
            if (media != null) {
                return new Media(libvlc, media);
            } else {
                return null;
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
        if (!isReadOnly()) {
            lock();
            try {
                int result;
                do {
                    result = libvlc.libvlc_media_list_remove_index(mediaListInstance, 0);
                } while (result == 0);
            }
            finally {
                unlock();
            }
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
