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
 * Implementation of an {@link AudioCallback}.
 * <p>
 * Provides default (empty) implementations of each callback method.
 * <p>
 * An application can simply override the callback methods it is interested in.
 */
public class AudioCallbackAdapter implements AudioCallback {

    @Override
    public void play(MediaPlayer mediaPlayer, Pointer samples, int sampleCount, long pts) {
    }

    @Override
    public void pause(MediaPlayer mediaPlayer, long pts) {
    }

    @Override
    public void resume(MediaPlayer mediaPlayer, long pts) {
    }

    @Override
    public void flush(MediaPlayer mediaPlayer, long pts) {
    }

    @Override
    public void drain(MediaPlayer mediaPlayer) {
    }

    @Override
    public void setVolume(float volume, boolean mute) {
    }

}
