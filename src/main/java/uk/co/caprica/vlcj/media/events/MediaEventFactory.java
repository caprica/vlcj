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

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.media.Media;

/**
 * A factory that creates a media list event instance for a native media event.
 */
public final class MediaEventFactory {

    /**
     * Create an event.
     *
     * @param event native event
     * @return media event, or <code>null</code> if the native event type is not known
     */
    public static MediaEvent createEvent(LibVlc libvlc, Media media, libvlc_event_t event) {
        switch(libvlc_event_e.event(event.type)) {
            case libvlc_MediaMetaChanged           : return new MediaMetaChangedEvent       (libvlc, media, event);
            case libvlc_MediaSubItemAdded          : return new MediaSubItemAddedEvent      (libvlc, media, event);
            case libvlc_MediaDurationChanged       : return new MediaDurationChangedEvent   (libvlc, media, event);
            case libvlc_MediaParsedChanged         : return new MediaParsedChangedEvent     (libvlc, media, event);
            case libvlc_MediaFreed                 : return new MediaFreedEvent             (libvlc, media);
            case libvlc_MediaStateChanged          : return new MediaStateChangedEvent      (libvlc, media, event);
            case libvlc_MediaSubItemTreeAdded      : return new MediaSubItemTreeAddedEvent  (libvlc, media, event);
            case libvlc_MediaThumbnailGenerated    : return new MediaThumbnailGeneratedEvent(libvlc, media, event);

            default                                : return null;
        }
    }

    private MediaEventFactory() {
    }

}
