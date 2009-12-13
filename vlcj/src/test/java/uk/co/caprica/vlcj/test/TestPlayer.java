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
 * Copyright 2009 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.MediaPlayer;
import uk.co.caprica.vlcj.MediaPlayerEventListener;
import uk.co.caprica.vlcj.VideoMetaData;
import uk.co.caprica.vlcj.check.EnvironmentChecker;

/**
 * Simple test harness creates an AWT Window and plays a video.
 * <p>
 * This is <strong>very</strong> basic but should give you an idea of how to build
 * a media player.
 */
public class TestPlayer {

  private static final String[] ARGS = {};
  
  /**
   * If you must use a broken operating system, you will need to do something
   * like this instead of the above.
   */
  // private static final String[] ARGS = {"--plugin-path=C:\\Program Files\\VideoLAN\\VLC\\plugins"};
  
  public static void main(String[] args) throws Exception {
    new EnvironmentChecker().checkEnvironment();
    
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    
	Canvas videoSurface = new Canvas();
	videoSurface.setBackground(Color.black);

	MediaPlayer mediaPlayer = new MediaPlayer(ARGS);
	  
	final Frame f = new Frame("VLCJ Test Player");
	f.setLayout(new BorderLayout());
	f.setBackground(Color.black);
	f.add(videoSurface, BorderLayout.CENTER);
	f.add(new ControlsPanel(mediaPlayer), BorderLayout.SOUTH);
	f.setBounds(100, 100, 600, 400);
	f.addWindowListener(new WindowAdapter() {
	  public void windowClosing(WindowEvent evt) {
		System.exit(0);
	  }
	});
    f.setVisible(true);
	
	mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventListener() {

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
        
        f.setSize(videoMetaData.getVideoDimension());
      }
	});
	mediaPlayer.setVideoSurface(videoSurface);
	mediaPlayer.playMedia("/path/to/some/video/here.mp4");

	Thread.currentThread().join();
  }
  
  private static class ControlsPanel extends JPanel {
    
    private ControlsPanel(final MediaPlayer mediaPlayer) {
      setBackground(Color.black);
      setBorder(new EmptyBorder(8, 8, 8, 8));
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      
      add(Box.createHorizontalGlue());
      
      Action pauseAction = new AbstractAction("Pause") {
        
        @Override
        public void actionPerformed(ActionEvent e) {
          mediaPlayer.pause();
        }
      };
      
      JButton stopButton = new JButton(pauseAction);
      add(stopButton);

      add(Box.createHorizontalStrut(8));
      
      Action playAction = new AbstractAction("Play") {

        @Override
        public void actionPerformed(ActionEvent e) {
          mediaPlayer.play();
        }
        
      };
      
      JButton playButton = new JButton(playAction);
      add(playButton);

      add(Box.createHorizontalGlue());
    }
    
  }
}
