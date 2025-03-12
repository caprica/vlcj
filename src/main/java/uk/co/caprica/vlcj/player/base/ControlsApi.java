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

import java.util.concurrent.atomic.AtomicBoolean;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_jump_time;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_next_frame;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_pause;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_play;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_reset_abloop;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_set_abloop_position;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_set_abloop_time;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_set_pause;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_set_position;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_set_rate;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_set_time;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_stop_async;

/**
 * Behaviour pertaining to media player controls.
 */
public final class ControlsApi extends BaseApi {

    /**
     * Flag whether or not stop was invoked.
     * <p>
     * Flag is cleared after the stopped event has been processed.
     *
     * @see MediaPlayerFinishedEventHandler
     */
    private final AtomicBoolean stopRequested = new AtomicBoolean(false);

    /**
     * Flag whether or not to automatically replay media after the media has finished playing.
     */
    private boolean repeat;

    ControlsApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Begin play-back.
     * <p>
     * If called when the play-back is paused, the play-back will resume from the current position.
     *
     * @return <code>true</code> if the media was played; <code>false</code> if not
     */
    public boolean play() {
        mediaPlayer.onBeforePlay();
        return libvlc_media_player_play(mediaPlayerInstance) == 0;
    }

    /**
     * Begin play-back and wait for the media to start playing or for an error to occur.
     * <p>
     * If called when the play-back is paused, the play-back will resume from the current position.
     * <p>
     * This call will <strong>block</strong> until the media starts or errors.
     *
     * @return <code>true</code> if the media started playing, <code>false</code> on error
     */
    public boolean start() {
        return new MediaPlayerPlayLatch(mediaPlayer).play();
    }

    /**
     * Stop play-back.
     * <p>
     * A subsequent play will play-back from the start.
     * <p>
     * <strong>Stopping is now an asynchronous operation natively.</strong>
     * <p>
     * This method waits for notification from the event manager that the media player stopped, i.e. it operates
     * synchronously, blocking until stopped.
     *
     * @return <code>true</code> if the media player was stopped; false if not
     * @see #stopAsync()
     * @see #isStopRequested()
     */
    public boolean stop() {
        stopRequested.set(true);
        return new MediaPlayerStopLatch(mediaPlayer).stop();
    }

    /**
     * Stop play-back.
     * <p>
     * A subsequent play will play-back from the start.
     * <p>
     * <strong>Stopping is now an asynchronous operation.</strong>
     *
     * @return <code>true</code> if the media player is being stopped; false if not
     * @see #stop()
     * @see #isStopRequested()
     */
    public boolean stopAsync() {
        stopRequested.set(true);
        return libvlc_media_player_stop_async(mediaPlayerInstance) == 0;
    }

    /**
     * Get whether or not a stop has previously been requested, and not yet processed, i.e. the media player is pending
     * stop.
     *
     * @return <code>true</code> if a stop was requested; false if not
     * @see #stop()
     * @see #stopAsync()
     */
    public boolean isStopRequested() {
        return stopRequested.get();
    }

    /**
     * Clear the stop requested flag.
     * <p>
     * Applications ordinarily have no business calling this.
     */
    public void clearStopRequested() {
        stopRequested.set(false);
    }

    /**
     * Pause/resume.
     * <p>
     * <strong>Pausing is now an asynchronous operation.</strong>
     *
     * @param pause true to pause, false to play/resume
     */
    public void setPause(boolean pause) {
        libvlc_media_player_set_pause(mediaPlayerInstance, pause ? 1 : 0);
    }

    /**
     * Pause play-back.
     * <p>
     * If the play-back is currently paused it will begin playing.
     * <p>
     * <strong>Pausing is now an asynchronous operation.</strong>
     */
    public void pause() {
        libvlc_media_player_pause(mediaPlayerInstance);
    }

    /**
     * Advance one frame.
     */
    public void nextFrame() {
        libvlc_media_player_next_frame(mediaPlayerInstance);
    }

    /**
     * Skip forward or backward by a period of time.
     * <p>
     * To skip backwards specify a negative delta.
     * <p>
     * Precise seeking is used.
     *
     * @param delta time period, in milliseconds
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean skipTime(long delta) {
        long current = mediaPlayer.status().time();
        if (current != -1) {
            return setTime(current + delta, false);
        } else {
            return false;
        }
    }

    /**
     * Skip forward or backward by a change in position.
     * <p>
     * To skip backwards specify a negative delta.
     * <p>
     * Precise seeking is used.
     *
     * @param delta amount to skip
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean skipPosition(double delta) {
        double current = mediaPlayer.status().position();
        if (current != -1) {
            return setPosition(current + delta, false);
        } else {
            return false;
        }
    }

    /**
     * Skip forward or backward by a period of time.
     * <p>
     * To skip backwards specify a negative delta.
     *
     * @param delta time period, in milliseconds
     * @param fast <code>true</code> for fast seeking; <code>false</code> for precise seeking
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean skipTime(long delta, boolean fast) {
        long current = mediaPlayer.status().time();
        if (current != -1) {
            return setTime(current + delta, fast);
        } else {
            return false;
        }
    }

    /**
     * Skip forward or backward by a change in position.
     * <p>
     * To skip backwards specify a negative delta.
     *
     * @param delta amount to skip
     * @param fast <code>true</code> for fast seeking; <code>false</code> for precise seeking
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean skipPosition(double delta, boolean fast) {
        double current = mediaPlayer.status().position();
        if (current != -1) {
            return setPosition(current + delta, fast);
        } else {
            return false;
        }
    }

    /**
     * Jump to a specific moment.
     * <p>
     * If the requested time is less than zero, it is normalised to zero.
     * <p>
     * Precise seeking is used.
     *
     * @param time time since the beginning, in milliseconds
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean setTime(long time) {
        return setTime(time, false);
    }

    /**
     * Jump to a specific position.
     * <p>
     * If the requested position is less than zero, it is normalised to zero.
     * <p>
     * Precise seeking is used.
     *
     * @param position position value, a percentage (e.g. 0.15 is 15%)
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean setPosition(double position) {
        return setPosition(position, false);
    }

    /**
     * Jump to a specific moment.
     * <p>
     * If the requested time is less than zero, it is normalised to zero.
     *
     * @param time time since the beginning, in milliseconds
     * @param fast <code>true</code> for fast seeking; <code>false</code> for precise seeking
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean setTime(long time, boolean fast) {
        return libvlc_media_player_set_time(mediaPlayerInstance, Math.max(time, 0), fast ? 1 : 0) == 0;
    }

    /**
     * Jump to a new time.
     * <p>
     * This will trigger a precise and relative seek from the current time.
     * <p>
     * If the requested time is less than zero, it is normalised to zero.
     *
     * @param time time, in milliseconds
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean jumpTime(long time) {
        return libvlc_media_player_jump_time(mediaPlayerInstance, Math.max(time, 0)) == 0;
    }

    /**
     * Jump to a specific position.
     * <p>
     * If the requested position is less than zero, it is normalised to zero.
     *
     * @param position position value, a percentage (e.g. 0.15 is 15%)
     * @param fast <code>true</code> for fast seeking; <code>false</code> for precise seeking
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean setPosition(double position, boolean fast) {
        return libvlc_media_player_set_position(mediaPlayerInstance, Math.max(position, 0), fast ? 1 : 0) == 0;
    }

    /**
     * Set an AB loop by time.
     * <p>
     * Media will loop from the A time to the B time indefinitely, until the media stops/finishes, or the loop is
     * explicitly reset via {@link #resetABLoop()}.
     * <p>
     * There must be an active input before setting the AB loop, i.e. essentially the media must be playing.
     * <p>
     * If the given times are very near the end of the media, the media may actually stop without looping.
     * <p>
     * If the loop is set successfully, the current playback time will jump to the start of the loop.
     *
     * @param aTime start time for the loop
     * @param bTime end time for the loop
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean setABLoopTime(long aTime, long bTime) {
        return libvlc_media_player_set_abloop_time(mediaPlayerInstance, aTime, bTime) == 0;
    }

    /**
     * Set an AB loop by position.
     * <p>
     * Media will loop from the A time to the B time indefinitely, until the media stops/finishes, or the loop is
     * explicitly reset via {@link #resetABLoop()}.
     * <p>
     * There must be an active input before setting the AB loop, i.e. essentially the media must be playing.
     * <p>
     * If the given positions are very near the end of the media, the media may actually stop without looping.
     * <p>
     * If the loop is set successfully, the current playback position will jump to the start of the loop.
     *
     * @param aPosition start position for the loop
     * @param bPosition end position for the loop
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean setABLoopPosition(double aPosition, double bPosition) {
        return libvlc_media_player_set_abloop_position(mediaPlayerInstance, aPosition, bPosition) == 0;
    }

    /**
     * Reset/remove a previously set AB loop.
     * <p>
     * This will not stop media playback.
     *
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean resetABLoop() {
        return libvlc_media_player_reset_abloop(mediaPlayerInstance) == 0;
    }

    /**
     * Set the video play rate.
     * <p>
     * Some media protocols are not able to change the rate.
     * <p>
     * Note that a successful return does not guarantee the rate was changed (depending on media protocol).
     *
     * @param rate rate, where 1.0 is normal speed, 0.5 is half speed, 2.0 is double speed and so on
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean setRate(float rate) {
        return libvlc_media_player_set_rate(mediaPlayerInstance, rate) != -1;
    }

    /**
     * Set whether or not the media player should automatically repeat playing the media when it has
     * finished playing.
     * <p>
     * There is <em>no</em> guarantee of seamless play-back when using this method - see instead
     * {@link uk.co.caprica.vlcj.player.list.MediaListPlayer MediaListPlayer}.
     *
     * @param repeat <code>true</code> to automatically replay the media, otherwise <code>false</code>
     */
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * Get whether or not the media player will automatically repeat playing the media when it has
     * finished playing.
     *
     * @return <code>true</code> if the media will be automatically replayed, otherwise <code>false</code>
     */
    public boolean getRepeat() {
        return repeat;
    }
}
