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

package uk.co.caprica.vlcj.player.list.events;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.eventmanager.EventNotification;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;

/**
 * Specification for a media list player event.
 */
abstract class MediaListPlayerEvent implements EventNotification<MediaListPlayerEventListener> {

    /**
     * Native library.
     */
    protected final LibVlc libvlc;

    /**
     * Native library instance.
     */
    protected final libvlc_instance_t libvlcInstance;

    /**
     * The media list player the event relates to.
     */
    protected final MediaListPlayer mediaListPlayer;

    /**
     * Create a media player event.
     *
     * @param libvlc native library
     * @param libvlcInstance native library instance
     * @param mediaListPlayer media player that the event relates to
     */
    protected MediaListPlayerEvent(LibVlc libvlc, libvlc_instance_t libvlcInstance, MediaListPlayer mediaListPlayer) {
        this.libvlc = libvlc;
        this.libvlcInstance = libvlcInstance;
        this.mediaListPlayer = mediaListPlayer;
    }

    // FIXME doc no need to release this, since it's transient
    protected final MediaRef temporaryMediaRef(libvlc_media_t mediaInstance) {
        return new MediaRef(libvlc, libvlcInstance, mediaInstance);
    }

}
