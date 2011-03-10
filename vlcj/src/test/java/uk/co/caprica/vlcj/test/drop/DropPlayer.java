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

package uk.co.caprica.vlcj.test.drop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.LineBorder;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

/**
 * Simple demo application that opens up an undecorated window onto which MRLs 
 * can be dropped.
 * <p>
 * For example, use an internet radio station directory web page to search for
 * a station - this will usually result in a link to an M3U file. Drag the M3U
 * link out of the browser and drop it on the application window and the radio
 * station will start playing.
 * <p>
 * The window itself is always-on-top and can be moved by clicking and dragging
 * in the window client area. 
 */
public class DropPlayer {

  private final DataFlavor uriListFlavor;
  
  private final MediaPlayerFactory mediaPlayerFactory;
  private final MediaPlayer mediaPlayer;
  private final JFrame frame;
  private final JPanel contentPane;
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          new DropPlayer().start();
        }
        catch(Exception e) {
          e.printStackTrace();
          System.exit(1);
        }
      }
    });
  }

  public DropPlayer() throws Exception {
    uriListFlavor = new DataFlavor("text/uri-list;class=java.lang.String");
    
    mediaPlayerFactory = new MediaPlayerFactory();
    mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
    mediaPlayer.setPlaySubItems(true); // <--- very important!

    contentPane = new JPanel();
    contentPane.setBackground(Color.black);
    contentPane.setBorder(new LineBorder(new Color(190, 190, 190)));
    contentPane.setLayout(new BorderLayout());
    contentPane.add(new ImagePane(ImagePane.Mode.FIT, getClass().getResource("drop.png"), 0.3f), BorderLayout.CENTER);
    contentPane.setTransferHandler(new MyTransferHandler());
    
    MyMouseAdapter mouseAdapter = new MyMouseAdapter();
    contentPane.addMouseListener(mouseAdapter);
    contentPane.addMouseMotionListener(mouseAdapter);
    
    frame = new JFrame("vlcj");
    frame.setContentPane(contentPane);
    frame.setAlwaysOnTop(true);
    frame.setSize(120, 78);
    frame.setUndecorated(true);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        mediaPlayer.release();
        mediaPlayerFactory.release();
      }
    });
  }
  
  private void start() {
    frame.setVisible(true);
  }
  
  /**
   * Transfer handler implementation to handle the dropped MRL.
   */
  @SuppressWarnings("serial")
  private class MyTransferHandler extends TransferHandler {
    @Override
    public boolean canImport(TransferSupport support) {
      return getDataFlavor(support) != null;
    }
    
    @Override
    public boolean importData(TransferSupport support) {
      DataFlavor flavor = getDataFlavor(support);
      if(flavor != null) {
        try {
          Object transferData = support.getTransferable().getTransferData(flavor);
          if(transferData instanceof String) { 
            System.out.println("DROPPED: " + transferData);
            String value = (String)transferData;
            String[] uris = value.split("\\r\\n");
            if(uris.length > 0) {
              // Play the first MRL that was dropped (the others are discarded)
              String uri = uris[0];
              mediaPlayer.playMedia(uri);
            }
            return true;
          }
        }
        catch(Exception e) {
          e.printStackTrace();
        }
      }
      return false;
    }
    
    private DataFlavor getDataFlavor(TransferSupport support) {
      if(support.isDataFlavorSupported(uriListFlavor)) {
        return uriListFlavor;
      }
      // TODO something else for Windows?
      return null;
    }
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
      Point mouseDragPoint = e.getLocationOnScreen();
      int x = mouseDownScreenPoint.x + (mouseDragPoint.x - mouseDownScreenPoint.x) - mouseDownPoint.x;
      int y = mouseDownScreenPoint.y + (mouseDragPoint.y - mouseDownScreenPoint.y) - mouseDownPoint.y;
      frame.setLocation(x, y);
    }
  }
}
