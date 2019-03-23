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

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.component.AudioListPlayerComponent;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaListPlayerComponent;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.test.VlcjTest;

import static uk.co.caprica.vlcj.player.component.MediaPlayerSpecs.audioPlayerSpec;
import static uk.co.caprica.vlcj.player.component.MediaPlayerSpecs.embeddedMediaPlayerSpec;

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

        AudioPlayerComponent component3 = new AudioPlayerComponent(audioPlayerSpec()
            .withFactory(null))
        {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                super.finished(mediaPlayer);
            }
        };

        AudioListPlayerComponent component4 = new AudioListPlayerComponent(audioPlayerSpec()
            .withFactory(null))
        {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                super.finished(mediaPlayer);
            }
        };

    }

}
