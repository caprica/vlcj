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

package uk.co.caprica.vlcj.player.list;

/**
 * Behaviour pertaining to media list player events.
 */
public final class EventApi extends BaseApi {

    private final MediaListPlayerNativeEventManager eventManager;

    EventApi(MediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);

        this.eventManager = new MediaListPlayerNativeEventManager(libvlcInstance, mediaListPlayer);
    }

    /**
     * Add a component to be notified of media player events.
     *
     * @param listener component to notify
     */
    public void addMediaListPlayerEventListener(MediaListPlayerEventListener listener) {
        eventManager.addEventListener(listener);
    }

    /**
     * Remove a component that was previously interested in notifications of media player events.
     *
     * @param listener component to stop notifying
     */
    public void removeMediaListPlayerEventListener(MediaListPlayerEventListener listener) {
        eventManager.removeEventListener(listener);
    }

    @Override
    protected void release() {
        eventManager.release();
    }

}
