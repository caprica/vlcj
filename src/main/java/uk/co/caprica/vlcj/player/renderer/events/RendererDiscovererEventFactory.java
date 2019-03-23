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

package uk.co.caprica.vlcj.player.renderer.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.player.renderer.RendererDiscoverer;

/**
 * A factory that creates a media player event instance for a native discoverer event.
 */
public final class RendererDiscovererEventFactory {

    /**
     * Create a new discoverer event for a given native event.
     * <p>
     * Events generally are expected to copy values from the native structure as needed (specifically this applies to
     * non-primitive values like Strings) because once the event handler returns the native memory will be gone. Without
     * copying such structure values pointers will become invalid.
     *
     * @param rendererDiscoverer component the event relates to
     * @param event native event
     * @return media player event, or <code>null</code> if the native event type is not enabled or otherwise could not be handled
     */
    public static RendererDiscovererEvent createEvent(RendererDiscoverer rendererDiscoverer, libvlc_event_t event) {
        switch(libvlc_event_e.event(event.type)) {
            case libvlc_RendererDiscovererItemAdded  : return new RendererDiscovererItemAddedEvent  (rendererDiscoverer, event);
            case libvlc_RendererDiscovererItemDeleted: return new RendererDiscovererItemDeletedEvent(rendererDiscoverer, event);

            default                                  : return null;
        }
    }

    private RendererDiscovererEventFactory() {
    }

}
