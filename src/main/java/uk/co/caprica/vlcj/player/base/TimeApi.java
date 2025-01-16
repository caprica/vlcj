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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.LongByReference;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_time_point_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_watch_time_on_paused;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_watch_time_on_seek;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_watch_time_on_update;
import uk.co.caprica.vlcj.binding.lib.LibVlc;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_clock;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_time_point_get_next_date;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_time_point_interpolate;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_unwatch_time;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_watch_time;

/**
 * Behaviour pertaining to media player timer.
 * <p>
 * Note that all time units are expressed as micro-seconds (us).
 */
public final class TimeApi extends BaseApi {

    /**
     * Native clock frequency constant, from the ./vlc/include/vlc_tick.h header file in the VLC sources.
     */
    private static final long VLC_CLOCK_FREQ = 1000000L;

    /**
     * Constant value used to indicate an invalid timestamp (e.g. if playback is paused).
     */
    private static final long VLC_TICK_INVALID = 0L;

    /**
     * Current listener.
     * <p>
     * The native API supports only a single current listener.
     */
    private WatchTimeListener watchTimeListener;

    TimeApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Watch the media player timer.
     * <p>
     * Only one listener may be registered.
     *
     * @param minimumPeriodBetweenUpdates minimum period between updates, or zero for all updates (nanoseconds)
     * @param data opaque data
     * @param listener listener to receive timer events
     * @return <code>true</code> on success; <code>false</code> o failure (e.g. already have a listener)
     */
    public boolean watchTime(long minimumPeriodBetweenUpdates, Long data, WatchTimeListener listener) {
        this.watchTimeListener = listener;
        return 0 == libvlc_media_player_watch_time(
            mediaPlayerInstance,
            minimumPeriodBetweenUpdates,
            onUpdateCallback,
            onPausedCallback,
            onSeekCallback,
            toPointer(data)
        );
    }

    /**
     * Stop watching the media player timer.
     */
    public void unwatchTime() {
        libvlc_media_player_unwatch_time(mediaPlayerInstance);
        watchTimeListener = null;
    }

    /**
     * Interpolate time/position values for a given time-point.
     *
     * @param timePoint time-point to update, if successful
     * @return <code>true</code> if interpolation was successful; <code>false</code> if not
     */
    public boolean interpolate(TimePoint timePoint) {
        LongByReference out_ts = new LongByReference();
        DoubleByReference out_pos = new DoubleByReference();
        boolean result = libvlc_media_player_time_point_interpolate(toInstance(timePoint), libvlc_clock(), out_ts, out_pos) == 0;
        if (result) {
            timePoint.update(out_ts.getValue(), out_pos.getValue());
        }
        return result;
    }

    /**
     * Return the next interval to receive an update event at the given number of seconds.
     * <p>
     * For example, if the number of seconds is 1, the interval will be the time until the timer reaches the next
     * second - it is not an absolute number of seconds from now.
     *
     * @param seconds desired number of segments for the next update
     * @return interval
     */
    public long nextInterval(int seconds) {
        // Equivalent to VLC_TICK_FROM_SEC(sec)
        return VLC_CLOCK_FREQ * seconds;
    }

    /**
     * Return whether the given tick/time/tiemstamp value is valid or not.
     *
     * @param value value to validate
     * @return <code>true</code> if the given value is valid; <code>false</code> if not
     */
    public boolean isValidTime(long value) {
        return value != VLC_TICK_INVALID;
    }

    /**
     * Return the absolute timestamp of the next interval.
     *
     * @param timePoint time point from {@link WatchTimeListener#watchTimeUpdate(MediaPlayer, TimePoint, Long)}
     * @param interpolatedTimestamp timestamp returned by {@link #interpolate(TimePoint)}
     * @param nextInterval next interval
     * @return absolute timestamp of the next interval
     */
    public long nextAbsoluteTimestamp(TimePoint timePoint, long interpolatedTimestamp, long nextInterval) {
        return libvlc_media_player_time_point_get_next_date(toInstance(timePoint), libvlc_clock(), interpolatedTimestamp, nextInterval);
    }

    /**
     * Return the relative timestamp of the next interval.
     * <p>
     * The return value is relative {@link LibVlc#libvlc_clock()}.
     *
     * @param timePoint time point from {@link WatchTimeListener#watchTimeUpdate(MediaPlayer, TimePoint, Long)}
     * @param interpolatedTimestamp timestamp returned by {@link #interpolate(TimePoint)}
     * @param nextInterval next interval
     * @return relative timestamp of the next interval
     */
    public long nextRelativeTimestamp(TimePoint timePoint, long interpolatedTimestamp, long nextInterval) {
        // Equivalent to libvlc_delay(), which is not a native API method
        return nextAbsoluteTimestamp(timePoint, interpolatedTimestamp, nextInterval) - libvlc_clock();
    }

    @Override
    protected void release() {
        if (watchTimeListener != null) {
            unwatchTime();
        }
    }

    private final libvlc_media_player_watch_time_on_update onUpdateCallback = new libvlc_media_player_watch_time_on_update() {

        @Override
        public void callback(libvlc_media_player_time_point_t value, Pointer data) {
            TimePoint timePoint = new TimePoint(
                value.rate, value.length_us, value.system_date_us, value.ts_us, value.position
            );
            watchTimeListener.watchTimeUpdate(mediaPlayer, timePoint, fromPointer(data));
        }
    };

    private final libvlc_media_player_watch_time_on_paused onPausedCallback = new libvlc_media_player_watch_time_on_paused() {

        @Override
        public void callback(long system_date_us, Pointer data) {
            watchTimeListener.watchTimePaused(mediaPlayer, system_date_us, fromPointer(data));
        }
    };

    private final libvlc_media_player_watch_time_on_seek onSeekCallback = new libvlc_media_player_watch_time_on_seek() {

        @Override
        public void callback(libvlc_media_player_time_point_t value, Pointer data) {
            TimePoint timePoint = new TimePoint(
                value.rate, value.length_us, value.system_date_us, value.ts_us, value.position
            );
            watchTimeListener.watchTimeSeek(mediaPlayer, timePoint, fromPointer(data));
        }
    };

    private static Pointer toPointer(Long value) {
        return value != null ? Pointer.createConstant(value) : null;
    }

    private static Long fromPointer(Pointer pointer) {
        return Pointer.nativeValue(pointer);
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
