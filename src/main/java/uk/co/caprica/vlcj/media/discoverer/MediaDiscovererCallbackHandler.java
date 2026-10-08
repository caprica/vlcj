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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media.discoverer;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_discoverer_cbs;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.discoverer.events.MediaDiscovererEventFactory;
import uk.co.caprica.vlcj.support.callback.NativeCallbackHandler;

final class MediaDiscovererCallbackHandler extends NativeCallbackHandler<MediaDiscoverer, libvlc_media_discoverer_cbs, MediaDiscovererEventListener> {

    MediaDiscovererCallbackHandler(MediaDiscoverer source) {
        super(source);
    }

    protected libvlc_media_discoverer_cbs initCallbacks() {
        libvlc_media_discoverer_cbs cbs = new libvlc_media_discoverer_cbs();
        cbs.version = 0;

        cbs.on_media_added = (opaque, parent, media) -> {
            MediaRef mediaRef = new MediaRef(media);
            try {
                raiseEvent(MediaDiscovererEventFactory.createMediaAddedEvent(source(), mediaRef));
            } finally {
                mediaRef.release();
            }
        };

        cbs.on_media_removed = (opaque, media) -> {
            MediaRef mediaRef = new MediaRef(media);
            try {
                raiseEvent(MediaDiscovererEventFactory.createMediaRemovedEvent(source(), mediaRef));
            } finally {
                mediaRef.release();
            }
        };

        cbs.write();
        return cbs;
    }
}
