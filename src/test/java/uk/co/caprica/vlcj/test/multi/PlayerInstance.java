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

package uk.co.caprica.vlcj.test.multi;

import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.*;

/**
 * A single player instance and associated video surface.
 */
public class PlayerInstance extends MediaPlayerEventAdapter {

    private final EmbeddedMediaPlayer mediaPlayer;

    private final Canvas videoSurface;

    public PlayerInstance(EmbeddedMediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        this.videoSurface = new Canvas();
        this.videoSurface.setBackground(Color.black);

        mediaPlayer.events().addMediaPlayerEventListener(this);
    }

    public EmbeddedMediaPlayer mediaPlayer() {
        return mediaPlayer;
    }

    public Canvas videoSurface() {
        return videoSurface;
    }

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, MediaRef media) {
        System.out.println("mediaChanged");
    }

    @Override
    public void playing(MediaPlayer mediaPlayer) {
        System.out.println("playing");
    }

    @Override
    public void paused(MediaPlayer mediaPlayer) {
        System.out.println("paused");
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        System.out.println("stopped");
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        System.out.println("finished");
    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
        System.out.println("error");
    }

    @Override
    public void opening(MediaPlayer mediaPlayer) {
        System.out.println("opening");
    }
}
