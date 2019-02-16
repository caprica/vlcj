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

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.*;
import uk.co.caprica.vlcj.player.base.callback.AudioCallback;

/**
 * Encapsulation of native audio callbacks.
 * <p>
 * This component acts as a bridge between the native callbacks and an implementation of an {@link AudioCallback}
 * component used to process the audio samples in some way (like playing them).
 * <p>
 * Once callbacks are enabled for a media player, they can <em>not</em> be disabled.
 */
final class AudioCallbacks {

    private final libvlc_audio_play_cb playCallback = new PlayCallback();
    private final libvlc_audio_pause_cb pauseCallback = new PauseCallback();
    private final libvlc_audio_resume_cb resumeCallback = new ResumeCallback();
    private final libvlc_audio_flush_cb flushCallback = new FlushCallback();
    private final libvlc_audio_drain_cb drainCallback = new DrainCallback();

    private final LibVlc libvlc;

    private final MediaPlayer mediaPlayer;

    private AudioCallback audioCallback;

    AudioCallbacks(LibVlc libvlc, MediaPlayer mediaPlayer) {
        this.libvlc = libvlc;
        this.mediaPlayer = mediaPlayer;
    }

    void callback(String format, int rate, int channels, AudioCallback audioCallback) {
        this.audioCallback = audioCallback;
        enableCallbacks(format, rate, channels);
    }

    private void enableCallbacks(String format, int rate, int channels) {
        libvlc.libvlc_audio_set_format(mediaPlayer.mediaPlayerInstance(), format, rate, channels);
        libvlc.libvlc_audio_set_callbacks(mediaPlayer.mediaPlayerInstance(), playCallback, pauseCallback, resumeCallback, flushCallback, drainCallback, null);
    }

    /**
     * Implementation of a callback invoked by the native library to play a series of samples.
     */
    private final class PlayCallback implements libvlc_audio_play_cb {

        @Override
        public void play(Pointer data, Pointer samples, int count, long pts) {
            audioCallback.play(mediaPlayer, samples, count, pts);
        }
    }

    /**
     * Implementation of a callback invoked by the native library when audio is paused.
     */
    private final class PauseCallback implements libvlc_audio_pause_cb {

        @Override
        public void pause(Pointer data, long pts) {
            audioCallback.pause(mediaPlayer, pts);
        }
    }

    /**
     * Implementation of a callback invoked by the native library when audio is resumed.
     */
    private final class ResumeCallback implements libvlc_audio_resume_cb {

        @Override
        public void resume(Pointer data, long pts) {
            audioCallback.resume(mediaPlayer, pts);
        }
    }

    /**
     * Implementation of a callback invoked by the native library when audio is flushed.
     */
    private final class FlushCallback implements libvlc_audio_flush_cb {

        @Override
        public void flush(Pointer data, long pts) {
            audioCallback.flush(mediaPlayer, pts);
        }
    }

    /**
     * Implementation of a callback invoked by the native library when audio is drained.
     */
    private final class DrainCallback implements libvlc_audio_drain_cb {

        @Override
        public void drain(Pointer data) {
            audioCallback.drain(mediaPlayer);
        }
    }

}
