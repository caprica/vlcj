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

package uk.co.caprica.vlcj.test.multi;

import java.awt.Canvas;
import java.awt.Color;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

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

        mediaPlayer.addMediaPlayerEventListener(this);
    }

    public EmbeddedMediaPlayer mediaPlayer() {
        return mediaPlayer;
    }

    public Canvas videoSurface() {
        return videoSurface;
    }

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
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
