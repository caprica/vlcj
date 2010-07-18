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

package uk.co.caprica.vlcj.test.numpty;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

import org.apache.log4j.BasicConfigurator;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;

/**
 * Minimal media player application.
 * <p>
 * Specify a single media MRL as a command-line argument.
 * <p>
 * VLCJ depends on log4j, so ensure you have a log4j.jar file in your class-path.
 */
public class NumptyPlayer {
  
  private Frame mainFrame;
  private Canvas videoSurface;
  
  private MediaPlayerFactory mediaPlayerFactory;

  private MediaPlayer mediaPlayer;
  
  public static void main(final String[] args) throws Exception {
    BasicConfigurator.configure();
    
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new NumptyPlayer(args);
      }
    });
  }
   
  public NumptyPlayer(final String[] args) {
    if(args.length != 1) {
      System.out.println("Specify a single media URL");
      System.exit(1);
    }
    
    videoSurface = new Canvas();
    
    // This burns so many people on Windows that I decided to leave it in...
    String vlcArgs = null;
    if(RuntimeUtil.isWindows()) {
      vlcArgs = "--plugin-path=" + WindowsRuntimeUtil.getVlcInstallDir() + "\\plugins";
    }

    mediaPlayerFactory = new MediaPlayerFactory(vlcArgs != null ? new String[] {vlcArgs} : new String[]{});
    
    mediaPlayer = mediaPlayerFactory.newMediaPlayer(null);

    mainFrame = new Frame("VLCJ Numpty Player");
    mainFrame.setLayout(new BorderLayout());
    mainFrame.add(videoSurface, BorderLayout.CENTER);
    mainFrame.setSize(800, 600);
    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent evt) {
        mediaPlayer.release();
        mediaPlayer = null;
        mediaPlayerFactory.release();
        System.exit(0);
      }
    });
    
    mainFrame.setVisible(true);

    mediaPlayer.setVideoSurface(videoSurface);

    // There is a race condition in native libraries when starting up, so
    // do not play immediately - empirically the most reliable way to avoid 
    // a native library crash is to sleep for a bit, then start the player 
    // on the EDT. Not ideal, if you find a better way let me know.
    try {
      Thread.sleep(1000);
    }
    catch (InterruptedException e) {
    }
    
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        mediaPlayer.playMedia(args[0]);
      }
    });
  }
}
