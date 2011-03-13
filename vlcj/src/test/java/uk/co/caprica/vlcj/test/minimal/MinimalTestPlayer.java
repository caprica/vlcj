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
 * Copyright 2009, 2010, 2011 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.minimal;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * An absolute minimum test player.
 */
public class MinimalTestPlayer extends VlcjTest {

  public static void main(String[] args) throws Exception {
    if(args.length != 1) {
      System.out.println("Specify an MRL to play");
      System.exit(1);
    }

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
    
    MediaPlayerFactory factory = new MediaPlayerFactory();
    
    EmbeddedMediaPlayer mediaPlayer = factory.newEmbeddedMediaPlayer();
    mediaPlayer.setVideoSurface(factory.newVideoSurface(vs));

    mediaPlayer.setRepeat(true);
    mediaPlayer.playMedia(args[0]);
    Thread.currentThread().join();
  }
}
