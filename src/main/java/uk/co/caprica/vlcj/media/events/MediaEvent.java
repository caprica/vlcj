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
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.eventmanager.EventNotification;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventListener;
import uk.co.caprica.vlcj.media.MediaRef;

abstract class MediaEvent implements EventNotification<MediaEventListener> {

    protected final LibVlc libvlc;

    protected final libvlc_instance_t libvlcInstance;

    /**
     * The media list the event relates to.
     */
    protected final Media media;

    /**
     * Create a media list event.
     *
     * @param media media that the event relates to
     */
    protected MediaEvent(LibVlc libvlc, libvlc_instance_t libvlcInstance, Media media) {
        this.libvlc = libvlc;
        this.libvlcInstance = libvlcInstance;
        this.media = media;
    }

    /**
     * Create a temporary media reference to wrap the native media handle.
     * <p>
     * Returning this temporary reference does <em>not</em> cause the native media instance to be retained - there is no
     * need to do so since the reference only exists for the lifetime of the native callback, and since it is not
     * retained, there is no need to release it either.
     * <p>
     * If a client application needs to keep the {@link MediaRef} it must use either {@link MediaRef#newMediaRef()} or
     * {@link MediaRef#newMedia()}.
     *
     * @param mediaInstance native media instance
     * @return temporary media reference, which must <em>not</em> be released
     */
    protected final MediaRef temporaryMediaRef(libvlc_media_t mediaInstance) {
        return new MediaRef(libvlc, libvlcInstance, mediaInstance);
    }

}
