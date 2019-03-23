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

/**
 * Behaviour pertaining to renderer discoverer events.
 */
public final class EventApi extends BaseApi {

    private final RendererDiscovererNativeEventManager eventManager;

    EventApi(RendererDiscoverer rendererDiscoverer) {
        super(rendererDiscoverer);

        this.eventManager = new RendererDiscovererNativeEventManager(rendererDiscoverer);

        // Add event handlers used for internal implementation
        addRendererDiscovererEventListener(new RendererItemListEventHandler());
    }

    /**
     * Add a component to be notified of renderer discoverer events.
     *
     * @param listener component to notify
     */
    public void addRendererDiscovererEventListener(RendererDiscovererEventListener listener) {
        eventManager.addEventListener(listener);
    }

    /**
     * Remove a component that was previously interested in notifications of renderer discoverer events.
     *
     * @param listener component to stop notifying
     */
    public void removeRendererDiscovererEventListener(RendererDiscovererEventListener listener) {
        eventManager.removeEventListener(listener);
    }

    @Override
    protected void release() {
        eventManager.release();
    }

}
