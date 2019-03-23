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

package uk.co.caprica.vlcj.media.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.media.Media;

/**
 * A factory that creates a media list event instance for a native media event.
 */
public final class MediaEventFactory {

    /**
     * Create an event.
     *
     * @param libvlcInstance native library instance
     * @param media component the event relates to
     * @param event native event
     * @return media event, or <code>null</code> if the native event type is not known
     */
    public static MediaEvent createEvent(libvlc_instance_t libvlcInstance, Media media, libvlc_event_t event) {
        switch(libvlc_event_e.event(event.type)) {
            case libvlc_MediaMetaChanged           : return new MediaMetaChangedEvent       (libvlcInstance, media, event);
            case libvlc_MediaSubItemAdded          : return new MediaSubItemAddedEvent      (libvlcInstance, media, event);
            case libvlc_MediaDurationChanged       : return new MediaDurationChangedEvent   (libvlcInstance, media, event);
            case libvlc_MediaParsedChanged         : return new MediaParsedChangedEvent     (libvlcInstance, media, event);
            case libvlc_MediaFreed                 : return new MediaFreedEvent             (libvlcInstance, media, event);
            case libvlc_MediaStateChanged          : return new MediaStateChangedEvent      (libvlcInstance, media, event);
            case libvlc_MediaSubItemTreeAdded      : return new MediaSubItemTreeAddedEvent  (libvlcInstance, media, event);
            case libvlc_MediaThumbnailGenerated    : return new MediaThumbnailGeneratedEvent(libvlcInstance, media, event);

            default                                : return null;
        }
    }

    private MediaEventFactory() {
    }

}
