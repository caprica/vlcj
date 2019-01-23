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

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.events.standard.MediaPlayerEvent;

public final class EventService extends BaseService {

    private final MediaPlayerNativeEventManager eventManager;

    EventService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);

        eventManager = new MediaPlayerNativeEventManager(libvlc, mediaPlayer);

        // Add event handlers used for internal implementation
        addMediaPlayerEventListener(new RepeatPlayEventHandler      ());
        addMediaPlayerEventListener(new MediaPlayerReadyEventHandler());
    }

    public void addMediaPlayerEventListener(MediaPlayerEventListener listener) {
        eventManager.addEventListener(listener);
    }

    public void removeMediaPlayerEventListener(MediaPlayerEventListener listener) {
        eventManager.removeEventListener(listener);
    }

    void raiseEvent(MediaPlayerEvent event) {
        eventManager.raiseEvent(event);
    }

    @Override
    protected void release() {
        eventManager.release();
    }

}
