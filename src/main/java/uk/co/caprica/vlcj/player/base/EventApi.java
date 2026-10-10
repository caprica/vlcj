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

package uk.co.caprica.vlcj.player.base;

import org.jspecify.annotations.Nullable;
import uk.co.caprica.vlcj.player.base.events.MediaPlayerEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_clock;

/**
 * Behaviour pertaining to media player events.
 */
public final class EventApi extends BaseApi {

    @Nullable
    private ScheduledFuture<?> timerTask;

    EventApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);

        // Add event handlers used for internal implementation (ordering here is important)
        addMediaPlayerEventListener(new RepeatPlayEventHandler());
        addMediaPlayerEventListener(new MediaPlayerReadyEventHandler());
    }

    /**
     * Add a component to be notified of media player events.
     *
     * @param listener component to notify
     */
    public void addMediaPlayerEventListener(MediaPlayerEventListener listener) {
        mediaPlayer.callbackHandler.addEventListener(listener);
    }

    /**
     * Remove a component that was previously interested in notifications of media player events.
     *
     * @param listener component to stop notifying
     */
    public void removeMediaPlayerEventListener(MediaPlayerEventListener listener) {
        mediaPlayer.callbackHandler.removeEventListener(listener);
    }

    void raiseEvent(MediaPlayerEvent event) {
        mediaPlayer.callbackHandler.raiseEvent(event);
    }
}
