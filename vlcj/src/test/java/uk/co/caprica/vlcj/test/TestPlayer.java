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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import uk.co.caprica.vlcj.MediaPlayer;
import uk.co.caprica.vlcj.MediaPlayerEventListener;

/**
 * Simple test harness creates an AWT Window and plays a video.
 */
public class TestPlayer {

  private static final String[] ARGS = {};
  
  public static void main(String[] args) throws Exception {
	Canvas videoSurface = new Canvas();
	videoSurface.setBackground(Color.black);
	  
	Frame f = new Frame("VLCJ Test Player");
	f.setLayout(new BorderLayout());
	f.setBackground(Color.black);
	f.add(videoSurface, BorderLayout.CENTER);
	f.setBounds(100, 100, 600, 400);
	f.addWindowListener(new WindowAdapter() {
	  public void windowClosing(WindowEvent evt) {
		System.exit(0);
	  }
	});
    f.setVisible(true);
	
	MediaPlayer mediaPlayer = new MediaPlayer(ARGS);
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
	});
	mediaPlayer.setVideoSurface(videoSurface);
	mediaPlayer.playMedia("someMovie.iso");
	
	Thread.currentThread().join();
  }
}
