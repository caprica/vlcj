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

package uk.co.caprica.vlcj.player.base.callback;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * Specification for an audio callback.
 *
 * @see AudioCallbackAdapter
 */
public interface AudioCallback {

    /**
     * Play samples.
     *
     * @param mediaPlayer media player
     * @param samples native sample data
     * @param sampleCount number of samples
     * @param pts presentation time stamp
     */
    void play(MediaPlayer mediaPlayer, Pointer samples, int sampleCount, long pts);

    /**
     * Audio was paused.
     *
     * @param mediaPlayer media player
     * @param pts presentation time stamp
     */
    void pause(MediaPlayer mediaPlayer, long pts);

    /**
     * Audio was resumed.
     *
     * @param mediaPlayer media player
     * @param pts presentation time stamp
     */
    void resume(MediaPlayer mediaPlayer, long pts);

    /**
     * Audio buffer was flushed.
     *
     * @param mediaPlayer media player
     * @param pts presentation time stamp
     */
    void flush(MediaPlayer mediaPlayer, long pts);

    /**
     * Audio buffer was drained.
     *
     * @param mediaPlayer media player
     */
    void drain(MediaPlayer mediaPlayer);

    /**
     * Adjust audio volume.
     *
     * @param volume volume
     * @param mute <code>true</code> if audio should be muted; <code>false</code> for un-muted
     */
    void setVolume(float volume, boolean mute);

}
