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

package uk.co.caprica.vlcj.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ListService extends BaseService {

    private final List<RendererItem> rendererItems = new CopyOnWriteArrayList<RendererItem>();

    ListService(RendererDiscoverer rendererDiscoverer) {
        super(rendererDiscoverer);
    }

    public List<RendererItem> rendererItems() {
        return new ArrayList<RendererItem>(rendererItems);
    }

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
