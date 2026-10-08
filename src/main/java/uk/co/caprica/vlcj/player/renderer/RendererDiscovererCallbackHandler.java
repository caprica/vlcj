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

package uk.co.caprica.vlcj.player.renderer;

import uk.co.caprica.vlcj.binding.internal.libvlc_renderer_discoverer_cbs;
import uk.co.caprica.vlcj.player.renderer.events.RendererDiscovererEventFactory;
import uk.co.caprica.vlcj.support.callback.NativeCallbackHandler;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_renderer_item_release;

/**
 * Creates and manages the libvlc_renderer_discoverer_cbs callback struct for a RendererDiscoverer.
 */
public final class RendererDiscovererCallbackHandler extends NativeCallbackHandler<RendererDiscoverer, libvlc_renderer_discoverer_cbs, RendererDiscovererEventListener> {

    public RendererDiscovererCallbackHandler(RendererDiscoverer source) {
        super(source);
    }

    protected libvlc_renderer_discoverer_cbs initCallbacks() {
        libvlc_renderer_discoverer_cbs cbs = new libvlc_renderer_discoverer_cbs();
        cbs.version = 0;

        cbs.on_item_added = (opaque, item) -> {
            raiseEvent(RendererDiscovererEventFactory.createItemAddedEvent(source(), new RendererItem(item)));
        };

        cbs.on_item_removed = (opaque, item) -> {
            raiseEvent(RendererDiscovererEventFactory.createItemRemovedEvent(source(), new RendererItem(item)));
            libvlc_renderer_item_release(item);
        };

        cbs.write();
        return cbs;
    }
}