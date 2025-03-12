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

import uk.co.caprica.vlcj.media.MediaEventListener;
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

    private final MediaPlayerNativeEventManager eventManager;

    private final PlaybackTimeHandler playbackTimeHandler = new PlaybackTimeHandler();

    private final List<MediaPlayerTimerListener> timerListenerList = new CopyOnWriteArrayList<>();

    private final ScheduledExecutorService timerService = Executors.newSingleThreadScheduledExecutor();

    private final Runnable playbackTimerTask = () -> {
        if (playbackTimeHandler.playing) {
            long playbackStarted = playbackTimeHandler.referenceTime;
            long playbackTime = playbackStarted != 0L ? libvlc_clock() - playbackStarted : 0L;
            for (MediaPlayerTimerListener listener : timerListenerList) {
                listener.tick(mediaPlayer, playbackTime);
            }
        }
    };

    private long timerRate = 1000;

    private TimeUnit timerUnit = TimeUnit.MILLISECONDS;

    private ScheduledFuture<?> timerTask;

    EventApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);

        eventManager = new MediaPlayerNativeEventManager(libvlcInstance, mediaPlayer);

        // Add event handlers used for internal implementation (ordering here is important)
        addMediaPlayerEventListener(new RepeatPlayEventHandler());
        addMediaPlayerEventListener(new MediaPlayerReadyEventHandler());

        // This is a special handler that is annotated to run after all others
        addMediaPlayerEventListener(new MediaPlayerFinishedEventHandler());

        // Handler for custom timer events
        addMediaPlayerEventListener(playbackTimeHandler);
    }

    /**
     * Add a component to be notified of media player events.
     *
     * @param listener component to notify
     */
    public void addMediaPlayerEventListener(MediaPlayerEventListener listener) {
        eventManager.addEventListener(listener);
    }

    /**
     * Remove a component that was previously interested in notifications of media player events.
     *
     * @param listener component to stop notifying
     */
    public void removeMediaPlayerEventListener(MediaPlayerEventListener listener) {
        eventManager.removeEventListener(listener);
    }

    /**
     * Add a component to be notified of media events.
     * <p>
     * As the current media changes, this listener will automatically be removed from the previous media and added to
     * the new.
     *
     * @param listener component to notify
     */
    public void addMediaEventListener(MediaEventListener listener) {
        mediaPlayer.media().addPersistentMediaEventListener(listener);
    }

    /**
     * Remove a component that was previously interested in notifications of media events.
     *
     * @param listener component to stop notifying
     */
    public void removeMediaEventListener(MediaEventListener listener) {
        mediaPlayer.media().removePersistentMediaEventListener(listener);
    }

    /**
     * Set the rate (period) at which to receive the custom media player timer event
     * <p>
     * This timer is completely separate from the {@link MediaPlayerEventListener#timeChanged(MediaPlayer, long)}
     * events.
     * <p>
     * By default this timer, if used, will send events once per second.
     * <p>
     * The timer tick events specify a number of <em>nanoseconds</em> since media playback first started.
     * <p>
     * If this method is going to be used to set the timer rate then it <em>must</em> be set <em>before</em> adding any
     * timer listeners.
     *
     * @see #addTimerListener(MediaPlayerTimerListener)
     *
     * @param rate rate, in time-units, for the custom timer notifications
     * @param unit time-unit for the rate
     */
    public void setTimerRate(long rate, TimeUnit unit) {
        this.timerRate = rate;
        this.timerUnit = unit;
    }

    /**
     * Add a component to receive regular timer notifications.
     * <p>
     * This timer is completely separate from the {@link MediaPlayerEventListener#timeChanged(MediaPlayer, long)}
     * events.
     * <p>
     * In principle this should provide a smoother media elapsed playback time timer.
     * <p>
     * This timer has nanosecond precision (although the availability of a nanosecond timer may depend on the run-time
     * operating system).
     * <p>
     * These timer events will <em>not</em> be sent if the media is paused/stopped.
     *
     * @see #setTimerRate(long, TimeUnit)
     *
     * @param listener component to receive timer notifications
     */
    public void addTimerListener(MediaPlayerTimerListener listener) {
        timerListenerList.add(listener);
        if (timerTask == null) {
            timerTask = timerService.scheduleAtFixedRate(playbackTimerTask, 0L, timerRate, timerUnit);
        }
    }

    /**
     * Remove a component previously added to receive regular timer notifications.
     *
     * @param listener component to no longer receive timer notifications
     */
    public void removeTimerListener(MediaPlayerTimerListener listener) {
        timerListenerList.remove(listener);
        if (timerListenerList.isEmpty()) {
            timerTask.cancel(true);
            timerTask = null;
        }
    }

    void raiseEvent(MediaPlayerEvent event) {
        eventManager.raiseEvent(event);
    }

    @Override
    protected void release() {
        eventManager.release();

        timerListenerList.clear();
        if (timerTask != null) {
            timerTask.cancel(true);
        }
        timerService.shutdown();
    }

    private static class PlaybackTimeHandler extends MediaPlayerEventAdapter {

        private volatile long referenceTime;

        private volatile boolean playing;

        @Override
        public void playing(MediaPlayer mediaPlayer) {
            referenceTime = libvlc_clock();
            playing = true;
        }

        @Override
        public void paused(MediaPlayer mediaPlayer) {
            playing = false;
        }

        @Override
        public void stopped(MediaPlayer mediaPlayer) {
            referenceTime = 0;
            playing = false;
        }

        @Override
        public void stopping(MediaPlayer mediaPlayer) {
            referenceTime = 0;
            playing = false;
        }

        @Override
        public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
            referenceTime = libvlc_clock() - (newTime * 1000);
        }

        @Override
        public void error(MediaPlayer mediaPlayer) {
            referenceTime = 0;
            playing = false;
        }
    }
}
