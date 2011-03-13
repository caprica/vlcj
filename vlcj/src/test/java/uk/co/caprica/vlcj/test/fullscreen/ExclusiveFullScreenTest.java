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

package uk.co.caprica.vlcj.test.fullscreen;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple full-screen test.
 * <p>
 * This test ignores the FullScreenStrategy implementation and instead directly
 * uses the GraphicsDevice to set the application frame to be the full-screen
 * window.
 * <p>
 * This may be useful on Windows platforms where there are many issues 
 * preventing proper operation of going in to and out of full-screen mode on
 * the fly. 
 */
public class ExclusiveFullScreenTest extends VlcjTest {

  public static void main(final String[] args) {
    if(args.length != 1) {
      System.err.println("Specify a single MRL");
      System.exit(1);
    }

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new ExclusiveFullScreenTest(args);
      }
    });
  }
  
  public ExclusiveFullScreenTest(String[] args) {
    Canvas c = new Canvas();
    c.setBackground(Color.black);

    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.add(c, BorderLayout.CENTER);
    
    JFrame f = new JFrame();
    f.setContentPane(p);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(800, 600);

    MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new DefaultFullScreenStrategy(f));
    mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));

    // Go directly to full-screen exclusive mode, do not use the media player
    // full screen strategy to do it. If you have multiple screens then you
    // need to provide a way to choose the desired screen device here
    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(f);

    mediaPlayer.startMedia(args[0]);
  }
}

