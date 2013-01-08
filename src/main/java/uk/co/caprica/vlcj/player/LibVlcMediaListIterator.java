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

package uk.co.caprica.vlcj.player;

import java.util.Iterator;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

/**
 * A one-time media list iterator.
 * <p>
 * For simplicities sake, and since this is a <em>private implementation
 * class</em>, the iterator may not be re-used and the implementation takes some liberties to ensure
 * that native resources are properly released.
 * <p>
 * Specifically, the native media instance for each sub-item must be explicitly released. The only
 * reliable place to carry this out is in the {@link #hasNext()} method implementation.
 * <p>
 * Consequently a user of this class <strong>must</strong> invoke {@link #hasNext()} after
 * processing each sub item. Since in any event this is the typical usage pattern for an iterator,
 * this is no big deal.
 * <p>
 * The reason that this class is used, despite the issues described above, is that it makes the code
 * in the media player implementation that deals with sub-items a lot simpler and reduces a lot of
 * code duplication when iterating the sub-items.
 */
class LibVlcMediaListIterator implements Iterable<libvlc_media_t>, Iterator<libvlc_media_t> {

    /**
     * Native library interface.
     */
    private final LibVlc libvlc;

    /**
     * Native media list instance.
     */
    private final libvlc_media_list_t mediaList;

    /**
     * Number of items in the media list.
     */
    private final int count;

    /**
     * Current iteration index.
     */
    private int index = -1;

    /**
     * Native instance for the current sub-item.
     */
    private libvlc_media_t current;

    /**
     * Create a new media list iterable iterator.
     *
     * @param libvlc native library instance
     * @param mediaList native media list instance, may be <code>null</code>
     */
    LibVlcMediaListIterator(LibVlc libvlc, libvlc_media_list_t mediaList) {
        this.libvlc = libvlc;
        this.mediaList = mediaList;
        this.count = mediaList != null ? libvlc.libvlc_media_list_count(mediaList) : 0;
    }

    @Override
    public Iterator<libvlc_media_t> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        // First release the current item if there is one...
        if(current != null) {
            libvlc.libvlc_media_release(current);
            current = null;
        }
        return mediaList != null && index + 1 < count;
    }

    @Override
    public libvlc_media_t next() {
        // Get the next item, this native handle must be released later
        current = libvlc.libvlc_media_list_item_at_index(mediaList, ++ index);
        return current;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
