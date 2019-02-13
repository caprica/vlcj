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

import uk.co.caprica.vlcj.media.MediaRef;

/**
 * Specification for a component that is interested in receiving event notifications from
 * a media list.
 */
public interface MediaListEventListener {

    /**
     * A new media item will be added to the list.
     *  @param mediaList list
     * @param item media instance that will be added
     * @param index index in the list at which the media instance will be added
     */
    void mediaListWillAddItem(MediaList mediaList, MediaRef item, int index);

    /**
     * A new media item was added to the list.
     *  @param mediaList list
     * @param item media instance that was added
     * @param index index in the list at which the media instance was added
     */
    void mediaListItemAdded(MediaList mediaList, MediaRef item, int index);

    /**
     * A new media item will be deleted from the list.
     *  @param mediaList list
     * @param item media instance that will be deleted
     * @param index index in the list at which the media instance will be deleted
     */
    void mediaListWillDeleteItem(MediaList mediaList, MediaRef item, int index);

    /**
     * A new media item was deleted from the list.
     *  @param mediaList list
     * @param item media instance that was deleted
     * @param index index in the list at which the media instance was deleted
     */
    void mediaListItemDeleted(MediaList mediaList, MediaRef item, int index);

    /**
     * The end of the media list was reached.
     * FIXME i don't think this actually fires
     *
     * @param mediaList list
     */
    void mediaListEndReached(MediaList mediaList);
}
