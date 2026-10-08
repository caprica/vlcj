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

import com.sun.jna.Structure;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.LongByReference;
import org.jspecify.annotations.Nullable;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_time_point_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_watch_time_cbs;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_clock;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_time_point_get_next_date;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_time_point_interpolate;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_unwatch_time;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_watch_time;

/**
 * Native callback handler for watch time events.
 * <p>
 * This is a bespoke handler that notifies a single source by template methods instead of multiple event listeners
 * sources.
 * <p>
 * It exists as a separate base class to pull out the native callback bindings from the interpolation calculation code.
 */
abstract class WatchTimeCallbackHandler {

    protected static final long VLC_CLOCK_FREQ = 1_000_000L;

    protected static final long VLC_TICK_INVALID = 0L;

    protected static final long MICROS_TO_NANOS = 1000L;

    private final libvlc_media_player_t mediaPlayerInstance;

    private final libvlc_media_player_watch_time_cbs cbs;

    protected WatchTimeCallbackHandler(libvlc_media_player_t mediaPlayerInstance) {
        this.mediaPlayerInstance = mediaPlayerInstance;
        this.cbs = initCallbacks();
    }

    private libvlc_media_player_watch_time_cbs initCallbacks() {
        libvlc_media_player_watch_time_cbs cbs = new libvlc_media_player_watch_time_cbs();
        cbs.version = 0;

        cbs.on_update =  (data, value) -> {
            TimePoint timePoint = fromInstance(value);
            onUpdate(timePoint);
        };

        cbs.on_paused = (data, system_date_us) -> {
            onPause(system_date_us);
        };

        cbs.on_seek = (data, value) -> {
            // Value is null when seeking is finished, we do not use this currently
            if (value != null) {
                TimePoint timePoint = fromInstance(value);
                onSeek(timePoint);
            }
        };

        cbs.write();
        return cbs;
    }

    public final boolean start(long minimumPeriodBetweenUpdates) {
        return 0 == libvlc_media_player_watch_time(
            mediaPlayerInstance,
            minimumPeriodBetweenUpdates,
            cbs,
            null
        );
    }

    public final void stop() {
        libvlc_media_player_unwatch_time(mediaPlayerInstance);
    }

    protected final @Nullable InterpolateResult interpolate(TimePoint timePoint, long systemNow) {
        LongByReference outputTimestamp = new LongByReference(0);
        DoubleByReference outputPosition = new DoubleByReference(0);
        int result = libvlc_media_player_time_point_interpolate(toInstance(timePoint), systemNow, outputTimestamp, outputPosition);
        if (result == 0) {
            return new InterpolateResult(outputTimestamp.getValue(), outputPosition.getValue());
        }
        return null;
    }

    protected final long getNextDate(TimePoint timePoint, long systemNow, long interpolatedTimestamp, long nextInterval) {
        return libvlc_media_player_time_point_get_next_date(toInstance(timePoint), systemNow, interpolatedTimestamp, nextInterval);
    }

    protected final long systemNowUs() {
        return libvlc_clock();
    }

    protected final boolean isValidTime(long value) {
        return value != VLC_TICK_INVALID;
    }

    protected void onUpdate(TimePoint timePoint) {
    }

    protected void onPause(long systemDate) {
    }

    protected void onSeek(TimePoint timePoint) {
    }

    public static long interval(int fps) {
        return VLC_CLOCK_FREQ / fps;
    }

    private static TimePoint fromInstance(libvlc_media_player_time_point_t value) {
        return new TimePoint(
            value.rate, value.length_us, value.system_date_us, value.ts_us, value.position
        );
    }

    private static libvlc_media_player_time_point_t toInstance(TimePoint timePoint) {
        libvlc_media_player_time_point_t result = Structure.newInstance(libvlc_media_player_time_point_t.class);
        result.setAutoWrite(false);
        result.position = timePoint.position();
        result.rate = timePoint.rate();
        result.ts_us = timePoint.timestamp();
        result.length_us = timePoint.length();
        result.system_date_us = timePoint.systemDate();
        result.write();
        return result;
    }
}
