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
import uk.co.caprica.vlcj.player.base.time.InterpolatedWatchTimeHandler;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Behaviour pertaining to the media player timer.
 * <p>
 * Note that all time units are expressed as <strong>microseconds</strong> (us).
 */
public final class TimeApi extends BaseApi {

    /**
     * 60 notification updates per second.
     */
    public static final long FAST_UPDATE_INTERVAL = InterpolatedWatchTimeHandler.interval(60);

    /**
     * 30 notification updates per second.
     */
    public static final long MEDIUM_UPDATE_INTERVAL = InterpolatedWatchTimeHandler.interval(30);

    /**
     * One notification update per second.
     */
    public static final long SLOW_UPDATE_INTERVAL = InterpolatedWatchTimeHandler.interval(1);

    public static final long FINE_MINIMUM_UPDATE_PERIOD = 250_000L;
    public static final long NORMAL_MINIMUM_UPDATE_PERIOD = 500_000L;
    public static final long COARSE_MINIMUM_UPDATE_PERIOD = 1_000_000L;

    private final List<WatchTimeListener> eventListenerList = new CopyOnWriteArrayList<WatchTimeListener>();

    @Nullable
    private InterpolatedWatchTimeHandler watchTimeHandler;

    TimeApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Start watching for media player time/position updates.
     * <p>
     * The targetReportingInterval parameter is used to set how frequently new time updates will be emitted, and is used
     * typically by a user interface clock (or slider if using position) component. A lower interval will result in
     * smoother updates/animations.
     * <p>
     * See:
     *   <li>{@link #FAST_UPDATE_INTERVAL}
     *   <li>{@link #MEDIUM_UPDATE_INTERVAL}
     *   <li>{@link #SLOW_UPDATE_INTERVAL}
     * <p>
     * The miniumUpdateInterval is used to set the minimum time that the native media player will wait before sending a
     * timer event, providing a new anchor time for next interpolated time calculation. If this value is too low, the
     * media player will flood timer callbacks continuously. Since the time/position is being smoothly interpolated, it
     * only needs infrequent native events to correct any small amount of drift from the calculated value.
     * <p>
     * See:
     *   <li>{@link #FINE_MINIMUM_UPDATE_PERIOD}
     *   <li>{@link #NORMAL_MINIMUM_UPDATE_PERIOD}
     *   <li>{@link #COARSE_MINIMUM_UPDATE_PERIOD}
     * <p>
     *
     * @param targetReportingInterval target interval for reporting time updates, in <strong>microseconds</strong>
     * @param miniumUpdatePeriod minimum time between native media player timer updates, <strong>microseconds</strong>
     * @return <code>true</code> if the watch started successfully; otherwise <code>false</code>
     */
    public boolean startWatching(long targetReportingInterval, long miniumUpdatePeriod) {
        if (watchTimeHandler != null) {
            throw new IllegalStateException("Already watching");
        }

        watchTimeHandler = new InterpolatedWatchTimeHandler(mediaPlayerInstance, targetReportingInterval) {
            @Override
            protected void notifyUpdate(long timestampUs, double position) {
                for (WatchTimeListener listener : eventListenerList) {
                    listener.watchTimeUpdate(mediaPlayer, timestampUs, position);
                }
            }
        };

        boolean started = watchTimeHandler.startWatching(miniumUpdatePeriod);
        if (!started) {
            stopWatching();
        }

        return started;
    }

    /**
     * Start watching for media player time/position updates with sensible default values.
     * <p>
     * For a full explanation of the interval parameter, see {@link #startWatching(long, long)}.
     *
     * @param targetReportingInterval target interval for reporting time updates, in <strong>microseconds</strong>
     * @return <code>true</code> if the watch started successfully; otherwise <code>false</code>
     */
    public boolean startWatching(long targetReportingInterval) {
        return startWatching(targetReportingInterval, NORMAL_MINIMUM_UPDATE_PERIOD);
    }

    /**
     * Start watching for media player time/position updates with sensible default values.
     *
     * @return <code>true</code> if the watch started successfully; otherwise <code>false</code>
     */
    public boolean startWatching() {
        return startWatching(MEDIUM_UPDATE_INTERVAL, NORMAL_MINIMUM_UPDATE_PERIOD);
    }

    /**
     * Stop watching media player time/position updates.
     */
    public void stopWatching() {
        Objects.requireNonNull(watchTimeHandler, "Watch time handler must not be null");
        watchTimeHandler.stopWatching();
    }

    public void addWatchTimeListener(WatchTimeListener listener) {
        eventListenerList.add(listener);
    }

    public void removeWatchTimeListener(WatchTimeListener listener) {
        eventListenerList.remove(listener);
    }

    @Override
    protected void release() {
        eventListenerList.clear();

        stopWatching();
    }
}
