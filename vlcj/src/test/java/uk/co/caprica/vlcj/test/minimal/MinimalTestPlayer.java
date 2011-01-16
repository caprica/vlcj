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
 * Copyright 2009, 2010 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.minimal;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * An absolute minimum test player.
 * <p>
 * <strong>It is very important that the main frame is made visible 
 * <em>before</em> the MediaPlayerFactory is created.</strong>
 */
public class MinimalTestPlayer {

  public static void main(String[] args) throws Exception {
    Frame f = new Frame("Test Player");
    f.setSize(800, 600);
    f.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    f.setLayout(new BorderLayout());
    Canvas vs = new Canvas();
    f.add(vs, BorderLayout.CENTER);
    f.setVisible(true);
    
    MediaPlayerFactory factory = new MediaPlayerFactory(new String[] {});
    
    EmbeddedMediaPlayer mediaPlayer = factory.newMediaPlayer(null);
    mediaPlayer.setVideoSurface(vs);
    
    mediaPlayer.playMedia("SomeMovie.mp4");
    Thread.currentThread().join();
  }
}
