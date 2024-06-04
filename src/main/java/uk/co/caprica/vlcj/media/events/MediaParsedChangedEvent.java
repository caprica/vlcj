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
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.media_parsed_changed;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventListener;
import uk.co.caprica.vlcj.media.MediaParsedStatus;

/**
 * Encapsulation of a media parsed changed event.
 */
final class MediaParsedChangedEvent extends MediaEvent {

    private final int newStatus;

    /**
     * Create a media event.
     *
     * @param libvlcInstance native library instance
     * @param media component the event relates to
     * @param event native event
     */
    MediaParsedChangedEvent(libvlc_instance_t libvlcInstance, Media media, libvlc_event_t event) {
        super(libvlcInstance, media);
        this.newStatus = ((media_parsed_changed) event.u.getTypedValue(media_parsed_changed.class)).new_status;
    }

    @Override
    public void notify(MediaEventListener listener) {
        listener.mediaParsedChanged(component, MediaParsedStatus.mediaParsedStatus(newStatus));
    }

}
