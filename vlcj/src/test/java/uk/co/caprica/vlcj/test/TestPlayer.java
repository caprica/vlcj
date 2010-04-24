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

package uk.co.caprica.vlcj.test;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.check.EnvironmentChecker;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.VideoMetaData;
import uk.co.caprica.vlcj.player.linux.LinuxMediaPlayer;

/**
 * Simple test harness creates an AWT Window and plays a video.
 * <p>
 * This is <strong>very</strong> basic but should give you an idea of how to build
 * a media player.
 */
public class TestPlayer {
  
  private static final String[] ARGS = {};
  
  private Frame mainFrame;
  
  private Canvas videoSurface;
  
  /**
   * If you must use a broken operating system, you will need to do something
   * like this instead of the above.
   */
  // private static final String[] ARGS = {"--plugin-path=C:\\Program Files\\VideoLAN\\VLC\\plugins"};
  
  public static void main(String[] args) throws Exception {
    new EnvironmentChecker().checkEnvironment();
    
    System.out.println("  version: " + LibVlc.INSTANCE.libvlc_get_version());
    System.out.println(" compiler: " + LibVlc.INSTANCE.libvlc_get_compiler());
    System.out.println("changeset: " + LibVlc.INSTANCE.libvlc_get_changeset());
    
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new TestPlayer();
      }
    });
  }
   
  public TestPlayer() {
	videoSurface = new Canvas();
	videoSurface.setBackground(Color.black);

	MediaPlayer mediaPlayer = new LinuxMediaPlayer(ARGS);
	  
	mainFrame = new Frame("VLCJ Test Player for VLC 1.1.x");
	mainFrame.setLayout(new BorderLayout());
	mainFrame.setBackground(Color.black);
	mainFrame.add(videoSurface, BorderLayout.CENTER);
	mainFrame.add(new PlayerControlsPanel(mediaPlayer), BorderLayout.SOUTH);
	mainFrame.setBounds(100, 100, 600, 400);
	mainFrame.addWindowListener(new WindowAdapter() {
	  public void windowClosing(WindowEvent evt) {
		System.exit(0);
	  }
	});
    mainFrame.setVisible(true);
	
	mediaPlayer.addMediaPlayerEventListener(new TestPlayerMediaPlayerEventListener());
	mediaPlayer.setVideoSurface(videoSurface);
    mediaPlayer.playMedia("/some/movie/here.avi");
  }
  
  private final class TestPlayerMediaPlayerEventListener extends MediaPlayerEventAdapter {
    public void finished(MediaPlayer mediaPlayer) {
      System.out.println("Finished");
      System.exit(0);
    }

    public void paused(MediaPlayer mediaPlayer) {
      System.out.println("Paused");
    }

    public void playing(MediaPlayer mediaPlayer) {
      System.out.println("Playing");
    }

    public void stopped(MediaPlayer mediaPlayer) {
      System.out.println("Stopped");
    }

    @Override
    public void metaDataAvailable(MediaPlayer mediaPlayer, VideoMetaData videoMetaData) {
      System.out.println("Meta Data Available");
      System.out.println(videoMetaData);
      
      videoSurface.setSize(videoMetaData.getVideoDimension());
      mainFrame.pack();
      
      // Auto-set the first sub-title just for purposes of demonstration
      if(videoMetaData.getSpuCount() > 0) {
        mediaPlayer.setSpu(1);
      }
      
//      System.out.println("SPU SPU SPU: " + mediaPlayer.getSpuCount());
    }
  }
}
