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

package uk.co.caprica.vlcj.test.drawable;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapter;
import uk.co.caprica.vlcj.test.VlcjTest;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * Test the behaviour of the various set drawable methods.
 * <p>
 * This application creates a launcher for three frames, each with an embedded
 * media player. Clicking a button launches a frame and plays a video.
 * <p>
 * Any particular test only works if the video is played correctly embedded 
 * inside the frame and no native video window is opened.
 * <p>
 * The three available methods to set the video surface are:
 * <ul>
 *   <li>libvlc_media_player_set_agl</li>
 *   <li>libvlc_media_player_set_nsobject</li>
 *   <li>libvlc_media_player_set_xwindow, this is what is used on Linux</li>
 * </ul>
 * <p>
 * You must pass an MRL for a media file to play as the only command-line
 * argument.
 * <p>
 * This is used primarily to test behaviour on MacOS platforms. If this test
 * does not work (which is likely the case on MacOS), then you simply can not 
 * have a media player embedded in your application. There is no other way
 * provided by libvlc to make this work.
 * <p>
 * The Windows implementation is excluded from this since it works and is 
 * irrelevant.
 */
public class SetDrawableTest extends VlcjTest {

  private MediaPlayerFactory mediaPlayerFactory;
  private EmbeddedMediaPlayer aglMediaPlayer;
  private EmbeddedMediaPlayer nsobjectMediaPlayer;
  private EmbeddedMediaPlayer xwindowMediaPlayer;
  
  private Canvas aglCanvas;
  private Canvas nsobjectCanvas;
  private Canvas xwindowCanvas;
  
  private CanvasVideoSurface aglVideoSurface;
  private CanvasVideoSurface nsobjectVideoSurface;
  private CanvasVideoSurface xwindowVideoSurface;
  
  private JPanel mainFrameContentPane;
  private JButton aglButton;
  private JButton nsobjectButton;
  private JButton xwindowButton;
  
  private JFrame mainFrame;
  private VideoFrame aglFrame;
  private VideoFrame nsobjectFrame;
  private VideoFrame xwindowFrame;
  
  public static void main(final String[] args) {
    if(args.length != 1) {
      System.out.println("Specify a single MRL");
      System.exit(1);
    }
    
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new SetDrawableTest(args);
      }
    });
  }

  @SuppressWarnings("serial")
  public SetDrawableTest(String[] args) {
    final String mrl = args[0]; 
    
    mediaPlayerFactory = new MediaPlayerFactory(new String[] {"--no-video-title-show"});
    aglMediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
    nsobjectMediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
    xwindowMediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
    
    aglCanvas = new Canvas();
    aglCanvas.setBackground(Color.black);
    
    nsobjectCanvas = new Canvas();
    nsobjectCanvas.setBackground(Color.black);

    xwindowCanvas = new Canvas();
    xwindowCanvas.setBackground(Color.black);
    
    aglVideoSurface = new CanvasVideoSurface(aglCanvas, new VideoSurfaceAdapter() {
      @Override
      public void attach(LibVlc libvlc, MediaPlayer mediaPlayer, long componentId) {
        long aglDrawable = Native.getComponentID(aglCanvas);
        libvlc.libvlc_media_player_set_agl(aglMediaPlayer.mediaPlayerInstance(), toInt(aglDrawable));
      }
    });
    
    nsobjectVideoSurface = new CanvasVideoSurface(aglCanvas, new VideoSurfaceAdapter() {
      @Override
      public void attach(LibVlc libvlc, MediaPlayer mediaPlayer, long componentId) {
        Pointer nsObjectDrawable = Native.getComponentPointer(nsobjectCanvas);
        libvlc.libvlc_media_player_set_nsobject(nsobjectMediaPlayer.mediaPlayerInstance(), nsObjectDrawable);
      }
    });

    xwindowVideoSurface = new CanvasVideoSurface(aglCanvas, new VideoSurfaceAdapter() {
      @Override
      public void attach(LibVlc libvlc, MediaPlayer mediaPlayer, long componentId) {
        long xwindowDrawable = Native.getComponentID(xwindowCanvas);
        libvlc.libvlc_media_player_set_xwindow(xwindowMediaPlayer.mediaPlayerInstance(), toInt(xwindowDrawable));
      }
    });
    
    aglMediaPlayer.setVideoSurface(aglVideoSurface);
    nsobjectMediaPlayer.setVideoSurface(nsobjectVideoSurface);
    xwindowMediaPlayer.setVideoSurface(xwindowVideoSurface);
    
    aglFrame = new VideoFrame("AGL", aglMediaPlayer);
    nsobjectFrame = new VideoFrame("NSObject", nsobjectMediaPlayer);
    xwindowFrame = new VideoFrame("XWindow", xwindowMediaPlayer);
    
    aglFrame.getContentPane().setLayout(new BorderLayout());
    aglFrame.getContentPane().add(aglCanvas, BorderLayout.CENTER);
    
    nsobjectFrame.getContentPane().setLayout(new BorderLayout());
    nsobjectFrame.getContentPane().add(nsobjectCanvas, BorderLayout.CENTER);
    
    xwindowFrame.getContentPane().setLayout(new BorderLayout());
    xwindowFrame.getContentPane().add(xwindowCanvas, BorderLayout.CENTER);
    
    aglFrame.setLocation(50, 150);
    nsobjectFrame.setLocation(50, 350);
    xwindowFrame.setLocation(50, 550);
    
    aglButton = new JButton("AGL");
    aglButton.setMnemonic('a');
    aglButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        aglFrame.start(mrl);
      }
    });
    
    nsobjectButton = new JButton("NSObject");
    nsobjectButton.setMnemonic('n');
    nsobjectButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        nsobjectFrame.start(mrl);
      }
    });

    xwindowButton = new JButton("XWindow");
    xwindowButton.setMnemonic('x');
    xwindowButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        xwindowFrame.start(mrl);
      }
    });
    
    mainFrameContentPane = new JPanel();
    mainFrameContentPane.setBorder(new EmptyBorder(16, 16, 16, 16));
    mainFrameContentPane.setLayout(new GridLayout(1, 3, 16, 0));
    mainFrameContentPane.add(aglButton);
    mainFrameContentPane.add(nsobjectButton);
    mainFrameContentPane.add(xwindowButton);
    
    mainFrame = new JFrame("vlcj Mac Test");
    mainFrame.setLocation(50, 50);
    mainFrame.setContentPane(mainFrameContentPane);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.pack();
    mainFrame.setVisible(true);
  }
  
  private class VideoFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;

    private EmbeddedMediaPlayer mediaPlayer;
    
    private VideoFrame(String title, EmbeddedMediaPlayer mediaPlayer) {
      super(title);
      this.mediaPlayer = mediaPlayer;
      setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      setSize(320, 180);
      pack();
    }
    
    private void start(final String mrl) {
      setVisible(true);
      toFront();
      mediaPlayer.playMedia(mrl);
    }
  }
  
  public int toInt(long value) {
    if(value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("long value " + value + " cannot be converted to an int without truncation.");
    }
    else {
      return (int)value;
    }
  }
}
