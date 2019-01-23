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

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.eventmanager.EventNotification;
import uk.co.caprica.vlcj.eventmanager.NativeEventManager;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListEventListener;
import uk.co.caprica.vlcj.player.events.media.MediaEventFactory;
import uk.co.caprica.vlcj.player.events.media.MediaEventListener;
import uk.co.caprica.vlcj.player.events.medialist.MediaListEventFactory;

final class MediaListNativeEventManager extends NativeEventManager<MediaList, MediaListEventListener> {

    MediaListNativeEventManager(LibVlc libvlc, MediaList eventObject) {
        super(libvlc, eventObject, libvlc_event_e.libvlc_MediaListItemAdded, libvlc_event_e.libvlc_MediaListEndReached, "media-list-events");
    }

    @Override
    protected libvlc_event_manager_t onGetEventManager(LibVlc libvlc, MediaList eventObject) {
        return libvlc.libvlc_media_list_event_manager(eventObject.mediaListInstance());
    }

    @Override
    protected EventNotification<MediaListEventListener> onCreateEvent(LibVlc libvlc, libvlc_event_t event, MediaList eventObject) {
        return MediaListEventFactory.createEvent(eventObject, event);
    }

}
