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

import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.media_freed;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventListener;

/**
 * Encapsulation of a media freed event.
 */
final class MediaFreedEvent extends MediaEvent {

    private final libvlc_media_t md;

    /**
     * Create a media event.
     *
     * @param libvlcInstance native library instance
     * @param media component the event relates to
     * @param event native event
     */
    MediaFreedEvent(libvlc_instance_t libvlcInstance, Media media, libvlc_event_t event) {
        super(libvlcInstance, media);
        this.md = ((media_freed) event.u.getTypedValue(media_freed.class)).md;
    }

    @Override
    public void notify(MediaEventListener listener) {
        listener.mediaFreed(component, temporaryMediaRef(md));
    }

}
