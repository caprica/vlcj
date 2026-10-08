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

package uk.co.caprica.vlcj.player.base.time;

import org.jspecify.annotations.Nullable;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * Implementation of media player watch time handler that smoothly interpolates playback time with jitter correction.
 */
public abstract class InterpolatedWatchTimeHandler extends WatchTimeCallbackHandler {

    private static final long FALLBACK_PARK_US = VLC_CLOCK_FREQ / 100;

    /**
     * Current time point.
     * <p>
     * Not caching and reusing instances to ensure thread-safety.
     */
    private final AtomicReference<@Nullable TimePoint> currentTimePoint = new AtomicReference<>();

    /**
     * Notification reporting update interval, microseconds.
     */
    private final long targetUpdateIntervalUs;

    /**
     * Dispatch thread running/stop flag.
     */
    private volatile boolean running = false;

    /**
     * Background thread that sends smooth time updates.
     */
    @Nullable
    private Thread dispatcherThread;

    /**
     * Create a watch time handler that interpolates smoothly.
     *
     * @param mediaPlayerInstance native media player instance to watch time for
     * @param targetUpdateIntervalUs target interval for reporting time updates, in <strong>microseconds</strong>
     */
    protected InterpolatedWatchTimeHandler(libvlc_media_player_t mediaPlayerInstance, long targetUpdateIntervalUs) {
        super(mediaPlayerInstance);
        this.targetUpdateIntervalUs = targetUpdateIntervalUs;
    }

    @Override
    protected final void onUpdate(TimePoint timePoint) {
        currentTimePoint.set(timePoint);
    }

    @Override
    protected final void onPause(long systemDate) {
        // A future optimisation may pause the thread processing while paused, but this requires some interaction with
        // update and a pause-tracking state and may have subtle edge cases difficult to deal with robustly

        // When the media stops (finishes) onPause is called with a systemDate of 0, it is not desirable to notify that
        // zero time and position since a UI clock component might temporarily glitch/flicker to 0:00, so these are
        // silently ignored - subsequently updates will fire with the duration of the media and a position of 1.0
        if (isValidTime(systemDate)) {
            TimePoint timePoint = currentTimePoint.getAndSet(null);
            if (timePoint != null) {
                InterpolateResult pausedAt = interpolate(timePoint, systemDate);
                if (pausedAt != null) {
                    notifyUpdate(pausedAt.timestamp, pausedAt.position);
                }
            }
        }
    }

    @Override
    protected final void onSeek(TimePoint timePoint) {
        currentTimePoint.set(timePoint);
        InterpolateResult res = interpolate(timePoint, systemNowUs());
        if (res != null) {
            // Notify immediately on seek
            notifyUpdate(res.timestamp, res.position);
        }
    }

    /**
     * Start watching time.
     *
     * @param minimumPeriodBetweenUpdates minimum time between native media player timer updates, <strong>microseconds</strong>
     * @return <code>true</code> if the watch successfully started; <code>false</code> otherwise
     */
    public final boolean startWatching(long minimumPeriodBetweenUpdates) {
        running = true;
        dispatcherThread = new Thread(this::dispatchLoop, "vlcj-watch-time");
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();

        return start(minimumPeriodBetweenUpdates);
    }

    /**
     * Stop watching media player time.
     */
    public final void stopWatching() {
        running = false;
        if (dispatcherThread != null) {
            dispatcherThread.interrupt();
        }

        stop();
    }

    private void dispatchLoop() {
        while (running) {
            TimePoint timePoint = currentTimePoint.get();
            if (timePoint == null) {
                LockSupport.parkNanos(FALLBACK_PARK_US * MICROS_TO_NANOS);
                continue;
            }

            long nowUs = systemNowUs();
            InterpolateResult interpolated = interpolate(timePoint, nowUs);
            if (interpolated != null) {
                notifyUpdate(interpolated.timestamp, interpolated.position);

                long nextDateUs = getNextDate(timePoint, nowUs, interpolated.timestamp, targetUpdateIntervalUs);
                long sleepNs = (nextDateUs - systemNowUs()) * MICROS_TO_NANOS;

                if (sleepNs > 0) {
                    LockSupport.parkNanos(sleepNs);
                }
            } else {
                LockSupport.parkNanos(FALLBACK_PARK_US * MICROS_TO_NANOS);
            }
        }
    }

    protected abstract void notifyUpdate(long timestampUs, double position);
}
