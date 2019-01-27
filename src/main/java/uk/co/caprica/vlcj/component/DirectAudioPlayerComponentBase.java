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

package uk.co.caprica.vlcj.component;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.directaudio.AudioCallback;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

abstract class DirectAudioPlayerComponentBase extends MediaPlayerEventAdapter implements AudioCallback {

    protected static final Spec spec() {
        return new Spec();
    }

    public static final class Spec {

        protected MediaPlayerFactory factory;
        protected String format;
        protected int rate;
        protected int channels;
        protected AudioCallback audioCallback;

        public Spec withFactory(MediaPlayerFactory factory) {
            this.factory = factory;
            return this;
        }

        public Spec withFormat(String format, int rate, int channels) {
            this.format = format;
            this.rate = rate;
            this.channels = channels;
            return this;
        }

        public Spec withAudioCallback(AudioCallback audioCallback) {
            this.audioCallback = audioCallback;
            return this;
        }

        private Spec(){
        }

    }

    protected DirectAudioPlayerComponentBase() {
    }

    // === AudioCallback ========================================================

    @Override
    public void play(DirectAudioPlayer mediaPlayer, Pointer samples, int sampleCount, long pts) {
    }

    @Override
    public void pause(DirectAudioPlayer mediaPlayer, long pts) {
    }

    @Override
    public void resume(DirectAudioPlayer mediaPlayer, long pts) {
    }

    @Override
    public void flush(DirectAudioPlayer mediaPlayer, long pts) {
    }

    @Override
    public void drain(DirectAudioPlayer mediaPlayer) {
    }

}
