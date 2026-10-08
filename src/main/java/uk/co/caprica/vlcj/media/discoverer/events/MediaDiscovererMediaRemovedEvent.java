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

package uk.co.caprica.vlcj.media.discoverer.events;

import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.discoverer.MediaDiscoverer;
import uk.co.caprica.vlcj.media.discoverer.MediaDiscovererEventListener;

/**
 * Native event used when an item was removed from the media discoverer.
 */
final class MediaDiscovererMediaRemovedEvent extends MediaDiscovererEvent {

    private final MediaRef media;

    MediaDiscovererMediaRemovedEvent(MediaDiscoverer mediaDiscoverer, MediaRef media) {
        super(mediaDiscoverer);
        this.media = media;
    }

    @Override
    public void notify(MediaDiscovererEventListener listener) {
        listener.mediaRemoved(mediaDiscoverer, media);
    }
}
