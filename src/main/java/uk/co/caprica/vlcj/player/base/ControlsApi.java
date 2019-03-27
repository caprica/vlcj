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

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_next_frame;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_pause;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_play;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_set_pause;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_set_position;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_set_rate;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_set_time;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_stop;

/**
 * Behaviour pertaining to media player controls.
 */
public final class ControlsApi extends BaseApi {

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
     */
    public void play() {
        mediaPlayer.onBeforePlay();
        libvlc_media_player_play(mediaPlayerInstance);
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
        return new MediaPlayerLatch(mediaPlayer).play();
    }

    /**
     * Stop play-back.
     * <p>
     * A subsequent play will play-back from the start.
     */
    public void stop() {
        libvlc_media_player_stop(mediaPlayerInstance);
    }

    /**
     * Pause/resume.
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
    public boolean skipPosition(float delta) {
        float current = mediaPlayer.status().position();
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
    public boolean skipPosition(float delta, boolean fast) {
        float current = mediaPlayer.status().position();
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
    public boolean setPosition(float position) {
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
     * Jump to a specific position.
     * <p>
     * If the requested position is less than zero, it is normalised to zero.
     *
     * @param position position value, a percentage (e.g. 0.15 is 15%)
     * @param fast <code>true</code> for fast seeking; <code>false</code> for precise seeking
     * @return <code>true</code> if successful; <code>false</code> otherwise
     */
    public boolean setPosition(float position, boolean fast) {
        return libvlc_media_player_set_position(mediaPlayerInstance, Math.max(position, 0), fast ? 1 : 0) == 0;
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
