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

package uk.co.caprica.vlcj.player.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Behaviour pertaining to the list of discovered renderer items.
 */
public final class ListApi extends BaseApi {

    private final List<RendererItem> rendererItems = new CopyOnWriteArrayList<RendererItem>();

    ListApi(RendererDiscoverer rendererDiscoverer) {
        super(rendererDiscoverer);
    }

    /**
     * Get the current list of renderer items.
     *
     * @return
     */
    public List<RendererItem> rendererItems() {
        return new ArrayList<RendererItem>(rendererItems);
    }

    /**
     * Check if a particular renderer item is still available.
     * <p>
     * Renderer items can come and go as they are discovered or disconnected (or otherwise become unavailable)
     *
     * @param containsItem
     * @return
     */
    public boolean contains(RendererItem containsItem) {
        for (RendererItem rendererItem : rendererItems) {
            if (rendererItem.rendererItemInstance().equals(containsItem.rendererItemInstance())) {
                return true;
            }
        }
        return false;
    }

    void itemAdded(RendererItem itemAdded) {
        rendererItems.add(itemAdded);
    }

    void itemDeleted(RendererItem itemDeleted) {
        for (RendererItem rendererItem : rendererItems) {
            if (rendererItem.rendererItemInstance().equals(itemDeleted.rendererItemInstance())) {
                rendererItems.remove(rendererItem);
                break;
            }
        }
    }

}
