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

package uk.co.caprica.vlcj.medialist.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.medialist.MediaList;

/**
 * A factory that creates a media list event instance for a native media list event.
 */
public final class MediaListEventFactory {

    /**
     * Create an event.
     *
     * @param libvlcInstance native library instance
     * @param mediaList component the event relates to
     * @param event native event
     * @return media list event, or <code>null</code> if the native event type is not known
     */
    public static MediaListEvent createEvent(libvlc_instance_t libvlcInstance, MediaList mediaList, libvlc_event_t event) {
        switch(libvlc_event_e.event(event.type)) {
            case libvlc_MediaListWillAddItem   : return new MediaListWillAddItemEvent   (libvlcInstance, mediaList, event);
            case libvlc_MediaListItemAdded     : return new MediaListItemAddedEvent     (libvlcInstance, mediaList, event);
            case libvlc_MediaListWillDeleteItem: return new MediaListWillDeleteItemEvent(libvlcInstance, mediaList, event);
            case libvlc_MediaListItemDeleted   : return new MediaListItemDeletedEvent   (libvlcInstance, mediaList, event);
            case libvlc_MediaListEndReached    : return new MediaListEndReachedEvent    (libvlcInstance, mediaList       );

            default                            : return null;
        }
    }

    private MediaListEventFactory() {
    }

}
