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

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.check.EnvironmentCheckerFactory;
import uk.co.caprica.vlcj.player.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.FullScreenStrategy;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.VideoMetaData;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.windows.WindowsCanvas;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;

import com.sun.jna.Native;

/**
 * Simple test harness creates an AWT Window and plays a video.
 * <p>
 * This is <strong>very</strong> basic but should give you an idea of how to build
 * a media player.
 */
public class TestPlayer {
  
  private Frame mainFrame;
  private Canvas videoSurface;
  private JPanel controlsPanel;
  
  private MediaPlayerFactory mediaPlayerFactory;

  private MediaPlayer mediaPlayer;
  
  public static void main(final String[] args) throws Exception {
    // Experimental
    Native.setProtected(true);
    
    new EnvironmentCheckerFactory().newEnvironmentChecker().checkEnvironment();
    
    System.out.println("  version: " + LibVlc.INSTANCE.libvlc_get_version());
    System.out.println(" compiler: " + LibVlc.INSTANCE.libvlc_get_compiler());
    System.out.println("changeset: " + LibVlc.INSTANCE.libvlc_get_changeset());
    
    setLookAndFeel();
    
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new TestPlayer(args);
      }
    });
  }
  
  private static void setLookAndFeel() throws Exception {
    String lookAndFeelClassName = null;
    LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
    for(LookAndFeelInfo lookAndFeel : lookAndFeelInfos) {
      if("Nimbus".equals(lookAndFeel.getName())) {
        lookAndFeelClassName = lookAndFeel.getClassName();
      }
    }
    if(lookAndFeelClassName == null) {
      lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
    }
    UIManager.setLookAndFeel(lookAndFeelClassName);
  }
   
  public TestPlayer(String[] args) {
    Runtime.getRuntime().addShutdownHook(new TestPlayerShutdownHook());

    if(RuntimeUtil.isWindows()) {
      videoSurface = new WindowsCanvas();
    }
    else {
      videoSurface = new Canvas();
    }
    
	videoSurface.setBackground(Color.black);
	videoSurface.setSize(800, 600); // Only for initial layout

	TestPlayerMouseListener mouseListener = new TestPlayerMouseListener();
	videoSurface.addMouseListener(mouseListener);
    videoSurface.addMouseMotionListener(mouseListener);
	
	List<String> vlcArgs = new ArrayList<String>();

	// Add some other arguments here, or take them from the command-line
	
	// Special case to help out users on Windows...
	if(RuntimeUtil.isWindows()) {
	  vlcArgs.add("--plugin-path=" + WindowsRuntimeUtil.getVlcInstallDir() + "\\plugins");
	}

    mainFrame = new Frame("VLCJ Test Player for VLC 1.1.x");

    FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(mainFrame);
	
    mediaPlayerFactory = new MediaPlayerFactory(vlcArgs.toArray(new String[vlcArgs.size()]));
    
	mediaPlayer = mediaPlayerFactory.newMediaPlayer(fullScreenStrategy);

	// Use any first command-line argument to set a logo
	if(args.length > 0) {
	  String logoFile = args[0];
	  
	  String[] standardOptions = {"video-filter=logo", "logo-file=" + logoFile, "logo-opacity=25"};
	  mediaPlayer.setStandardMediaOptions(standardOptions);
	}
	
	controlsPanel = new PlayerControlsPanel(mediaPlayer);
	
    mainFrame.setLayout(new BorderLayout());
    mainFrame.setBackground(Color.black);
    mainFrame.add(videoSurface, BorderLayout.CENTER);
    mainFrame.add(controlsPanel, BorderLayout.SOUTH);
    mainFrame.pack();
    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent evt) {
        System.exit(0);
      }
    });
    
    // Global AWT key handler, you're better off using Swing's InputMap and 
    // ActionMap with a JFrame - that would solve all sorts of focus issues too
    Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
      @Override
      public void eventDispatched(AWTEvent event) {
        if(event instanceof KeyEvent) {
          KeyEvent keyEvent = (KeyEvent)event;
          if(keyEvent.getID() == KeyEvent.KEY_PRESSED) {
            if(keyEvent.getKeyCode() == KeyEvent.VK_F12) {
              controlsPanel.setVisible(!controlsPanel.isVisible());
              mainFrame.invalidate();
              mainFrame.validate();
            }
          }
        }
      }
    }, AWTEvent.KEY_EVENT_MASK);
    
    mainFrame.setVisible(true);
    
	mediaPlayer.addMediaPlayerEventListener(new TestPlayerMediaPlayerEventListener());
	mediaPlayer.setVideoSurface(videoSurface);
    
	// This might be useful
//	enableMousePointer(false);
  }
  
  private final class TestPlayerMediaPlayerEventListener extends MediaPlayerEventAdapter {
    @Override
    public void finished(MediaPlayer mediaPlayer) {
      System.out.println("Finished");
    }

    @Override
    public void paused(MediaPlayer mediaPlayer) {
      System.out.println("Paused");
    }

    @Override
    public void playing(MediaPlayer mediaPlayer) {
      System.out.println("Playing");
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
      System.out.println("Stopped");
    }

    @Override
    public void metaDataAvailable(MediaPlayer mediaPlayer, VideoMetaData videoMetaData) {
      System.out.println("Meta Data Available");
      System.out.println(videoMetaData);
      
      videoSurface.setSize(videoMetaData.getVideoDimension());
      mainFrame.pack();
      
      // You can set a logo like this if you like...
//      mediaPlayer.setLogoFile("./etc/vlcj-logo.png");
//      mediaPlayer.setLogoOpacity(50);
//      mediaPlayer.setLogoLocation(10, 10);
//      mediaPlayer.enableLogo(true);

      // Demo the marquee      
      mediaPlayer.setMarqueeText("Thank you for using VLCJ");
      mediaPlayer.setMarqueeSize(30);
      mediaPlayer.setMarqueeOpacity(95);
      mediaPlayer.setMarqueeColour(new Color(0xf0a0c0));
      mediaPlayer.setMarqueeTimeout(5000);
      mediaPlayer.setMarqueeLocation(50, 100);
      mediaPlayer.enableMarquee(true);
    }
  }
  
  /**
   * 
   * 
   * @param enable
   */
  private void enableMousePointer(boolean enable) {
    if(enable) {
      videoSurface.setCursor(null);
    }
    else {
      Image blankImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
      videoSurface.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(blankImage, new Point(0, 0), ""));
    }
  }
  
  private final class TestPlayerMouseListener extends MouseAdapter {
    @Override
    public void mouseMoved(MouseEvent e) {
//      System.out.println("MOVE: " + e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
      System.out.println("PRESS: " + e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      System.out.println("RELEASE: " + e);
    }
  }
  
  private final class TestPlayerShutdownHook extends Thread {
    @Override
    public void run() {
      if(mediaPlayerFactory != null) {
        mediaPlayerFactory.release();
      }
    }
  }
}
