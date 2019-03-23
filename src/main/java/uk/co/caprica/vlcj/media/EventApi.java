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

package uk.co.caprica.vlcj.media;

/**
 * Behaviour pertaining to media events.
 */
public final class EventApi extends BaseApi {

    private final MediaNativeEventManager eventManager;

    EventApi(Media media) {
        super(media);

        this.eventManager = new MediaNativeEventManager(libvlcInstance, media);
    }

    /**
     * Add a component to be notified of media events.
     *
     * @param listener component to notify
     */
    public void addMediaEventListener(MediaEventListener listener) {
        eventManager.addEventListener(listener);
    }

    /**
     * Remove a component that was previously interested in notifications of media events.
     *
     * @param listener component to stop notifying
     */
    public void removeMediaEventListener(MediaEventListener listener) {
        eventManager.removeEventListener(listener);
    }

    @Override
    protected void release() {
        eventManager.release();
    }

}
