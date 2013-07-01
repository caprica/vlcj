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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.directaudio;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_drain_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_flush_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_pause_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_play_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_resume_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.DefaultMediaPlayer;

import com.sun.jna.Pointer;

/**
 * Media player implementation that provides direct access to the audio buffer data.
 * <p>
 * Client applications specify the decoded audio buffer format, the sample rate (in Hz) and the
 * number of audio channels.
 * <p>
 * The standard format for the decoded audio is "S16N".
 * <p>
 * Client applications respond to native callbacks by providing an implementation of
 * {@link AudioCallback} or {@link AudioCallbackAdapter}.
 * <p>
 * When using the direct audio player then obviously the audio is not played - if a client
 * application needs the audio to be heard then the client application must play the audio samples
 * itself (e.g. via JavaSound or some other library).
 * <p>
 * The main callback is "play", whereby a number of decoded audio samples are presented - the
 * number of audio samples does <em>not</em> equate to the size of the audio buffer, the "block
 * size" for a sample must also be considered.
 */
public class DefaultDirectAudioPlayer extends DefaultMediaPlayer implements DirectAudioPlayer {

    /**
     * Play callback.
     * <p>
     * A hard reference to the callback must be kept otherwise the callback will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_audio_play_cb playCallback;

    /**
     * Pause callback.
     * <p>
     * A hard reference to the callback must be kept otherwise the callback will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_audio_pause_cb pauseCallback;

    /**
     * Resume callback.
     * <p>
     * A hard reference to the callback must be kept otherwise the callback will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_audio_resume_cb resumeCallback;

    /**
     * Flush callback.
     * <p>
     * A hard reference to the callback must be kept otherwise the callback will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_audio_flush_cb flushCallback;

    /**
     * Drain callback.
     * <p>
     * A hard reference to the callback must be kept otherwise the callback will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_audio_drain_cb drainCallback;

    /**
     * Audio callback receives native callback events.
     */
    private final AudioCallback audioCallback;

    /**
     * Create a direct audio player.
     *
     * @param libvlc native library instance
     * @param instance libvlc instance
     * @param format decoded buffer format
     * @param rate decoded buffer sample rate
     * @param channels decoded buffer channel count
     * @param audioCallback audio callback
     */
    public DefaultDirectAudioPlayer(LibVlc libvlc, libvlc_instance_t instance, String format, int rate, int channels, AudioCallback audioCallback) {
        super(libvlc, instance);
        this.audioCallback = audioCallback;
        this.playCallback = new PlayCallback();
        this.pauseCallback = new PauseCallback();
        this.resumeCallback = new ResumeCallback();
        this.flushCallback = new FlushCallback();
        this.drainCallback = new DrainCallback();
        libvlc.libvlc_audio_set_format(mediaPlayerInstance(), format, rate, channels);
        libvlc.libvlc_audio_set_callbacks(mediaPlayerInstance(), playCallback, pauseCallback, resumeCallback, flushCallback, drainCallback, null);
    }

    /**
     * Implementation of a callback invoked by the native library to play a series of samples.
     */
    private final class PlayCallback implements libvlc_audio_play_cb {

        @Override
        public void play(Pointer data, Pointer samples, int count, long pts) {
            Logger.trace("play(count={},pts={})", count, pts);
            audioCallback.play(DefaultDirectAudioPlayer.this, samples, count, pts);
        }
    }

    /**
     * Implementation of a callback invoked by the native library when audio is paused.
     */
    private final class PauseCallback implements libvlc_audio_pause_cb {

        @Override
        public void pause(Pointer data, long pts) {
            Logger.debug("pause(pts={})", pts);
            audioCallback.pause(DefaultDirectAudioPlayer.this, pts);
        }
    }

    /**
     * Implementation of a callback invoked by the native library when audio is resumed.
     */
    private final class ResumeCallback implements libvlc_audio_resume_cb {

        @Override
        public void resume(Pointer data, long pts) {
            Logger.debug("resume(pts={})", pts);
            audioCallback.resume(DefaultDirectAudioPlayer.this, pts);
        }
    }

    /**
     * Implementation of a callback invoked by the native library when audio is flushed.
     */
    private final class FlushCallback implements libvlc_audio_flush_cb {

        @Override
        public void flush(Pointer data, long pts) {
            Logger.debug("flush(pts={})", pts);
            audioCallback.flush(DefaultDirectAudioPlayer.this, pts);
        }
    }

    /**
     * Implementation of a callback invoked by the native library when audio is drained.
     */
    private final class DrainCallback implements libvlc_audio_drain_cb {

        @Override
        public void drain(Pointer data) {
            Logger.debug("drain()");
            audioCallback.drain(DefaultDirectAudioPlayer.this);
        }
    }
}
