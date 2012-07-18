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

package uk.co.caprica.vlcj.medialist;

import java.util.Collections;
import java.util.List;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

/**
 * Encapsulation of an item in a {@link MediaList}.
 * <p>
 * An item comprises a name, a media resource locator (MRL) that may be passed
 * to a media player instance to play it, and possibly a sub-item list.
 * <p>
 * The sub-item list should be an empty list rather than <code>null</code> if
 * there are no items.
 */
public class MediaListItem {

    /**
     * Native media instance.
     */
    private final libvlc_media_t mediaInstance;
    
    /**
     * Name/description of the item.
     */
    private final String name;
    
    /**
     * MRL of the item.
     */
    private final String mrl;
    
    /**
     * List of sub-items.
     */
    private final List<MediaListItem> subItems;
    
    /**
     * Create a media list item.
     *
     * @param mediaInstance native media instance 
     * @param name name/description
     * @param mrl MRL
     * @param subItems
     */
    public MediaListItem(libvlc_media_t mediaInstance, String name, String mrl, List<MediaListItem> subItems) {
        this.name = name;
        this.mrl = mrl;
        this.subItems = subItems;
        this.mediaInstance = mediaInstance;
    }
    
    /**
     * Get the name/description of this item.
     * 
     * @return name/description
     */
    public final String name() {
        return name;
    }
    
    /**
     * Get the MRL of this item.
     * 
     * @return MRL
     */
    public final String mrl() {
        return mrl;
    }

    /**
     * Get the native media instance.
     * 
     * @return native media instance
     */
    public final libvlc_media_t mediaInstance() {
        return mediaInstance;
    }
    
    /**
     * Get the sub-item list.
     * 
     * @return sub-items
     */
    public final List<MediaListItem> subItems() {
        return Collections.unmodifiableList(subItems);
    }
    
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("name=").append(name).append(',');
        sb.append("mrl=").append(mrl).append(',');
        sb.append("subItems=").append(subItems).append(']');
        return sb.toString();
    }
}
