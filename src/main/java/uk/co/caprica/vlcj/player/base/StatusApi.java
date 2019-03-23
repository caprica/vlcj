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

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_can_pause;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_get_length;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_get_position;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_get_rate;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_get_state;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_get_time;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_has_vout;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_is_playing;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_is_seekable;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_program_scrambled;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_will_play;

/**
 * Behaviour pertaining to the status of the media player.
 * <p>
 * It is recommended instead to rely on events to track the media player status.
 */
public final class StatusApi extends BaseApi {

    StatusApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Is the current media playable?
     *
     * @return <code>true</code> if the current media is playable, otherwise <code>false</code>
     */
    public boolean isPlayable() {
        return libvlc_media_player_will_play(mediaPlayerInstance) == 1;
    }

    /**
     * Is the media player playing?
     *
     * @return <code>true</code> if the media player is playing, otherwise <code>false</code>
     */
    public boolean isPlaying() {
        return libvlc_media_player_is_playing(mediaPlayerInstance) == 1;
    }

    /**
     * Is the current media seekable?
     *
     * @return <code>true</code> if the current media is seekable, otherwise <code>false</code>
     */
    public boolean isSeekable() {
        return libvlc_media_player_is_seekable(mediaPlayerInstance) == 1;
    }

    /**
     * Can the current media be paused?
     *
     * @return <code>true</code> if the current media can be paused, otherwise <code>false</code>
     */
    public boolean canPause() {
        return libvlc_media_player_can_pause(mediaPlayerInstance) == 1;
    }

    /**
     * Is the current program scrambled?
     *
     * @return <code>true</code> if the current program is scrambled, otherwise <code>false</code>
     */
    public boolean programScrambled() {
        return libvlc_media_player_program_scrambled(mediaPlayerInstance) == 1;
    }

    /**
     * Get the length of the current media item.
     *
     * @return length, in milliseconds
     */
    public long length() {
        return libvlc_media_player_get_length(mediaPlayerInstance);
    }

    /**
     * Get the current play-back time.
     *
     * @return current time, expressed as a number of milliseconds
     */
    public long time() {
        return libvlc_media_player_get_time(mediaPlayerInstance);
    }

    /**
     * Get the current play-back position.
     *
     * @return current position, expressed as a percentage (e.g. 0.15 is returned for 15% complete)
     */
    public float position() {
        return libvlc_media_player_get_position(mediaPlayerInstance);
    }

    /**
     * Get the current video play rate.
     *
     * @return rate, where 1.0 is normal speed, 0.5 is half speed, 2.0 is double speed and so on
     */
    public float rate() {
        return libvlc_media_player_get_rate(mediaPlayerInstance);
    }

    /**
     * Get the number of video outputs for the media player.
     *
     * @return number of video outputs, may be zero
     */
    public int videoOutputs() {
        return libvlc_media_player_has_vout(mediaPlayerInstance);
    }

    /**
     * Get the media player current state.
     * <p>
     * It is recommended to listen to events instead of using this.
     *
     * @return state current media player state
     */
    public State state() {
        return State.state(libvlc_media_player_get_state(mediaPlayerInstance));
    }

}
