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

package uk.co.caprica.vlcj.test.multi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.MediaPlayerLatch;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;

/**
 * Example multi-instance player.
 */
public class TestMultiPlayer {

  private String[] medias = {
    "wibble1.mp4",
    "wibble2.mp4"
    // Your MRL's go here
  };
    
  private int rows = 1;
  private int cols = 4;
  
  private Frame mainFrame;
  
  private List<PlayerInstance> players = new ArrayList<PlayerInstance>();

  private MediaPlayerFactory factory;
  
  public static void main(String[] args) {
    System.out.println(LibVlc.INSTANCE.libvlc_get_version());
    System.out.println(LibVlc.INSTANCE.libvlc_get_changeset());
    
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new TestMultiPlayer().start();
      }
    });
  }
  
  public TestMultiPlayer() {
    JPanel contentPane = new JPanel();
    contentPane.setBackground(Color.black);
    contentPane.setLayout(new GridLayout(rows, cols, 16, 16));
    contentPane.setBorder(new EmptyBorder(16, 16, 16, 16));
    
    mainFrame = new Frame("VLCJ Test Multi Player for VLC 1.1.x");
    
    mainFrame.setLayout(new BorderLayout());
    mainFrame.setBackground(Color.black);
    mainFrame.add(contentPane, BorderLayout.CENTER);
    mainFrame.setBounds(100, 100, 1600, 300);
    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent evt) {
        for(PlayerInstance pi : players) {
          pi.mediaPlayer().release();
        }
        factory.release();
        System.exit(0);
      }
    });

    mainFrame.addKeyListener(new KeyAdapter() {

      @Override
      public void keyPressed(KeyEvent e) {
        for(int i = 0; i < players.size(); i++) {
          players.get(i).mediaPlayer().pause();
        }
      }
    });

//    String[] args = {"--no-xlib"};
//    String[] args = {"--plugin-path=/home/linux/vlc/lib"};
    String[] args = {};
    
    factory = new MediaPlayerFactory(args);
    
    FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(mainFrame);
    
    for(int i = 0; i < medias.length; i++) {
      EmbeddedMediaPlayer player = factory.newMediaPlayer(fullScreenStrategy);
      PlayerInstance playerInstance = new PlayerInstance(player);
      players.add(playerInstance);
      
      JPanel playerPanel = new JPanel();
      playerPanel.setLayout(new BorderLayout());
      playerPanel.setBorder(new LineBorder(Color.white, 2));
      playerPanel.add(playerInstance.videoSurface());
      
      contentPane.add(playerPanel);
    }
    
    mainFrame.setVisible(true);
  }
  
  private void start() {
    for(int i = 0; i < medias.length; i++) {
      players.get(i).mediaPlayer().setVideoSurface(players.get(i).videoSurface());
      players.get(i).mediaPlayer().prepareMedia(medias[i]);
    }

    // There is a race condition somewhere when invoking libvlc_media_player_play()
    // multiple times in quick succession that causes a hard-failure and a fatal 
    // VM crash.
    //
    // This is _not_ about _concurrently_ calling play multiple times, but the
    // native play function call must be off-loading something to a separate
    // thread and returning - then a subsequent call to play somehow interferes
    // with that or fails because of that.
    //
    // When libvlc_media_player_play() is called, the video playback is kicked 
    // off asynchronously - so the API call will return before the video has
    // started playing. If we invoke play and then wait (making this effectively
    // a synchronous call) for the player to start playing, the hard VM crash
    // does not occur
    for(int i = 0; i < medias.length; i++) {
      EmbeddedMediaPlayer mediaPlayer = players.get(i).mediaPlayer();
      
      MediaPlayerLatch l = new MediaPlayerLatch(mediaPlayer);
      l.setTimeout(3, TimeUnit.SECONDS);
      l.play();
    }
  }
}
