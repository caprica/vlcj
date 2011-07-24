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

package uk.co.caprica.vlcj.test.undecorated;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Basic media player that displays video in an undecorated frame.
 * <p>
 * The frame can be dragged around the desktop by clicking and dragging anywhere
 * inside the window client area, including clicking and dragging on the video
 * itself.
 */
public class UndecoratedTest extends VlcjTest {

  private MediaPlayerFactory factory;
  private EmbeddedMediaPlayer mediaPlayer;
  private Canvas canvas;
  private CanvasVideoSurface videoSurface;
  private JFrame frame;
  
  /**
   * Application entry point.
   * 
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    if(args.length != 1) {
      System.err.println("Specify a single MRL");
      System.exit(1);
    }
    
    final String mrl = args[0];
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new UndecoratedTest().start(mrl);
      }
    });
  }

  /**
   * Create a media player.
   */
  public UndecoratedTest() {
    factory = new MediaPlayerFactory("--no-video-title-show");
    mediaPlayer = factory.newEmbeddedMediaPlayer();
    canvas = new Canvas();
    canvas.setBackground(Color.black);
    videoSurface = factory.newVideoSurface(canvas);
    mediaPlayer.setVideoSurface(videoSurface);
    
    JPanel cp = new JPanel();
    cp.setLayout(new BorderLayout());
    cp.add(canvas, BorderLayout.CENTER);
    
    MyMouseAdapter mouseAdapter = new MyMouseAdapter();
    canvas.addMouseListener(mouseAdapter);
    canvas.addMouseMotionListener(mouseAdapter);
    
    frame = new JFrame("Undecorated Test");
    frame.setContentPane(cp);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1000, 700);
    frame.setUndecorated(true);
  }
  
  /**
   * Start the media player.
   * 
   * @param mrl media to play
   */
  private void start(String mrl) {
    frame.setVisible(true);
    mediaPlayer.startMedia(mrl);
  }

  /**
   * Mouse adapter implementation to handle dragging the window by clicking and
   * dragging in the window client area.
   */
  private class MyMouseAdapter extends MouseAdapter {

    private Point mouseDownScreenPoint;
    private Point mouseDownPoint;
    
    @Override
    public void mousePressed(MouseEvent e) {
      mouseDownScreenPoint = e.getLocationOnScreen();
      mouseDownPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      mouseDownScreenPoint = mouseDownPoint = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      if(SwingUtilities.isLeftMouseButton(e)) {
        Point mouseDragPoint = e.getLocationOnScreen();
        int x = mouseDownScreenPoint.x + (mouseDragPoint.x - mouseDownScreenPoint.x) - mouseDownPoint.x;
        int y = mouseDownScreenPoint.y + (mouseDragPoint.y - mouseDownScreenPoint.y) - mouseDownPoint.y;
        frame.setLocation(x, y);
      }
    }
  }
}
