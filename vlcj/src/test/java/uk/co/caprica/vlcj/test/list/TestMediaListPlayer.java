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

package uk.co.caprica.vlcj.test.list;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaList;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;

/**
 * An example of how to use the media list player.
 */
public class TestMediaListPlayer {

  public static void main(String[] args) throws Exception {
    Canvas videoSurface = new Canvas();
    videoSurface.setBackground(Color.black);
    
    JPanel cp = new JPanel();
    cp.setLayout(new BorderLayout());
    cp.add(videoSurface, BorderLayout.CENTER);
    
    JFrame f = new JFrame("VLCJ Media List Player");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setContentPane(cp);
    f.setSize(800, 600);
    f.setVisible(true);

    String[] vlcArgs = {};
    
    MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(vlcArgs);

    // Create a media list player to manage the play-list 
    MediaListPlayer mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();
    
    mediaListPlayer.addMediaListPlayerEventListener(new MediaListPlayerEventListener() {
      @Override
      public void nextItem(MediaListPlayer mediaListPlayer) {
        System.out.println("nextItem()");
      }
    });
    
    // Create a play-list and add some media items
    MediaList mediaList = mediaPlayerFactory.newMediaList();
    
    // Add your media items here...
    mediaList.addMedia("/home/movies/one.mp4");
    mediaList.addMedia("/home/movies/two.mp4");
    mediaList.addMedia("/home/movies/three.mp4");
    
    // Attach the play-list to the media list player
    mediaListPlayer.setMediaList(mediaList);
    
    // Loop the play-list when finished
    mediaListPlayer.setMode(MediaListPlayerMode.LOOP);

    // Create an media player to play the items in the play-list
    EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newMediaPlayer(null);
    mediaPlayer.setVideoSurface(videoSurface);
    
    // Attach the media player to the media list player
    mediaListPlayer.setMediaPlayer(mediaPlayer);
    
    // Unfortunately we have to invoke play() here to ensure the video surface 
    // gets set - this will NOT actually start playing anything (this API might
    // change)
    mediaPlayer.play();

    // Now start playing the play-list - this will cause the associated media 
    // player instance to start
    mediaListPlayer.play();

    // Wait forever (until the main window is closed)
    Thread.currentThread().join();
  }
}
