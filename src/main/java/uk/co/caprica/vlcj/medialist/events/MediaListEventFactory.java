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

package uk.co.caprica.vlcj.medialist.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.media_list_item_added;
import uk.co.caprica.vlcj.binding.internal.media_list_item_deleted;
import uk.co.caprica.vlcj.binding.internal.media_list_will_add_item;
import uk.co.caprica.vlcj.binding.internal.media_list_will_delete_item;
import uk.co.caprica.vlcj.medialist.MediaList;

/**
 * A factory that creates a media list event instance for a native media list event.
 */
public class MediaListEventFactory {

    /**
     * Media list to which the event relates.
     */
    private final MediaList mediaList;

    /**
     * Create a new factory.
     *
     * @param mediaList media list to create events for
     */
    public MediaListEventFactory(MediaList mediaList) {
        this.mediaList = mediaList;
    }

    /**
     * Create an event.
     *
     * @param event native event
     * @return media list event, or <code>null</code> if the native event type is not enabled or otherwise could not be handled
     */
    public MediaListEvent createEvent(libvlc_event_t event) {
        // Create an event suitable for the native event type...
        MediaListEvent result = null;
        switch(libvlc_event_e.event(event.type)) {
            case libvlc_MediaListWillAddItem:
                media_list_will_add_item addItemEvent = ((media_list_will_add_item)event.u.getTypedValue(media_list_will_add_item.class));
                result = new MediaListWillAddItemEvent(mediaList, addItemEvent.item, addItemEvent.index);
                break;

            case libvlc_MediaListItemAdded:
                media_list_item_added itemAddedEvent = ((media_list_item_added)event.u.getTypedValue(media_list_item_added.class));
                result = new MediaListItemAddedEvent(mediaList, itemAddedEvent.item, itemAddedEvent.index);
                break;

            case libvlc_MediaListWillDeleteItem:
                media_list_will_delete_item deleteItemEvent = ((media_list_will_delete_item)event.u.getTypedValue(media_list_will_delete_item.class));
                result = new MediaListWillDeleteItemEvent(mediaList, deleteItemEvent.item, deleteItemEvent.index);
                break;

            case libvlc_MediaListItemDeleted:
                media_list_item_deleted itemDeletedEvent = ((media_list_item_deleted)event.u.getTypedValue(media_list_item_deleted.class));
                result = new MediaListItemDeletedEvent(mediaList, itemDeletedEvent.item, itemDeletedEvent.index);
                break;
        }
        return result;
    }
}
