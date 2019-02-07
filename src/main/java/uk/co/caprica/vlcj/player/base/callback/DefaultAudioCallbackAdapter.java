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
 * Default implementation of an {@link AudioCallbackAdapter}.
 * <p>
 * This implementation gets the native sample data as a <code>byte[]</code> assuming a known fixed block size.
 */
public abstract class DefaultAudioCallbackAdapter extends AudioCallbackAdapter {

    /**
     * Fixed block size for each sample.
     */
    protected final int blockSize;

    /**
     * Create an audio callback.
     *
     * @param blockSize block size for each sample
     */
    public DefaultAudioCallbackAdapter(int blockSize) {
        this.blockSize = blockSize;
    }

    @Override
    public final void play(MediaPlayer mediaPlayer, Pointer samples, int sampleCount, long pts) {
        onPlay(mediaPlayer, samples.getByteArray(0, sampleCount * blockSize), sampleCount, pts);
    }

    /**
     * Template method to receive the decoded samples.
     *
     * @param mediaPlayer media player
     * @param data sample data
     * @param sampleCount number of samples
     * @param pts presentation time stamp
     */
    protected abstract void onPlay(MediaPlayer mediaPlayer, byte[] data, int sampleCount, long pts);

}
