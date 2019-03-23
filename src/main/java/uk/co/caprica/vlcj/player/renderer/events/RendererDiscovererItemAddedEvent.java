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

import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.renderer_discoverer_item_added;
import uk.co.caprica.vlcj.player.renderer.RendererDiscoverer;
import uk.co.caprica.vlcj.player.renderer.RendererDiscovererEventListener;
import uk.co.caprica.vlcj.player.renderer.RendererItem;

/**
 * Native event used when a new item was added to the renderer discoverer.
 */
final class RendererDiscovererItemAddedEvent extends RendererDiscovererEvent {

    private RendererItem item;

    RendererDiscovererItemAddedEvent(RendererDiscoverer rendererDiscoverer, libvlc_event_t event) {
        super(rendererDiscoverer);
        this.item = new RendererItem(((renderer_discoverer_item_added) event.u.getTypedValue(renderer_discoverer_item_added.class)).item);
    }

    @Override
    public void notify(RendererDiscovererEventListener listener) {
        listener.rendererDiscovererItemAdded(rendererDiscoverer, item);
    }

}
