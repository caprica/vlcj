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

package uk.co.caprica.vlcj.medialist;

import java.util.Collections;
import java.util.List;

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
     * @param name name/description
     * @param mrl MRL
     * @param subItems
     */
    public MediaListItem(String name, String mrl, List<MediaListItem> subItems) {
        this.name = name;
        this.mrl = mrl;
        this.subItems = subItems;
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
