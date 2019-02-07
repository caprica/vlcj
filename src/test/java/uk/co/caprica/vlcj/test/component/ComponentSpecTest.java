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

package uk.co.caprica.vlcj.test.component;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.component.*;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

import static uk.co.caprica.vlcj.component.MediaPlayerSpecs.*;

/**
 * This is a simple test of the spec builder syntax and dynamic sub-classing.
 */
public class ComponentSpecTest extends VlcjTest {

    public static void main(String[] args) {

        EmbeddedMediaPlayerComponent component1 = new EmbeddedMediaPlayerComponent(embeddedMediaPlayerSpec()
            .withFactory(null)
            .withFullScreenStrategy(null))
        {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                super.finished(mediaPlayer);
            }
        };

        EmbeddedMediaListPlayerComponent component2 = new EmbeddedMediaListPlayerComponent(embeddedMediaPlayerSpec()
            .withFactory(null)
            .withFullScreenStrategy(null))
        {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                super.finished(mediaPlayer);
            }
        };

        DirectAudioPlayerComponent component3 = new DirectAudioPlayerComponent(callbackAudioPlayerSpec().
            withFactory(null))
        {
            @Override
            public void play(DirectAudioPlayer mediaPlayer, Pointer samples, int sampleCount, long pts) {
                super.play(mediaPlayer, samples, sampleCount, pts);
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                super.finished(mediaPlayer);
            }
        };

        AudioMediaPlayerComponent component4 = new AudioMediaPlayerComponent(audioPlayerSpec()
            .withFactory(null))
        {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                super.finished(mediaPlayer);
            }
        };

        AudioMediaListPlayerComponent component5 = new AudioMediaListPlayerComponent(audioPlayerSpec()
            .withFactory(null))
        {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                super.finished(mediaPlayer);
            }
        };

    }

}
