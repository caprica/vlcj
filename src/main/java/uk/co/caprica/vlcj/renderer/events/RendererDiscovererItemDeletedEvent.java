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

package uk.co.caprica.vlcj.renderer.events;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.renderer_discoverer_item_deleted;
import uk.co.caprica.vlcj.renderer.RendererDiscovererEventListener;
import uk.co.caprica.vlcj.renderer.RendererItem;
import uk.co.caprica.vlcj.renderer.RendererDiscoverer;

final class RendererDiscovererItemDeletedEvent extends RendererDiscovererEvent {

    private final RendererItem item;

    RendererDiscovererItemDeletedEvent(LibVlc libvlc, RendererDiscoverer rendererDiscoverer, libvlc_event_t event) {
        super(libvlc, rendererDiscoverer);
        this.item = new RendererItem(libvlc, ((renderer_discoverer_item_deleted) event.u.getTypedValue(renderer_discoverer_item_deleted.class)).item);
    }

    @Override
    public void notify(RendererDiscovererEventListener listener) {
        listener.rendererDiscovererItemDeleted(rendererDiscoverer, item);
    }

}
