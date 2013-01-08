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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.drop;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.LineBorder;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple demo application that opens up an undecorated window onto which MRLs can be dropped.
 * <p>
 * For example you can drop local video files or YouTube video links.
 * <p>
 * The window is always-on-top and can be moved by clicking and dragging in the window client area
 * (at least this is possible on Linux).
 */
public class DropVideoPlayer extends VlcjTest {

    private final DataFlavor uriListFlavor;
    private final DataFlavor javaUrlFlavor;
    private final DataFlavor javaFileListFlavor;

    private final MediaPlayerFactory mediaPlayerFactory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final CanvasVideoSurface videoSurface;
    private final JFrame frame;
    private final JPanel contentPane;
    private final Canvas canvas;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new DropVideoPlayer().start();
                }
                catch(Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }

    public DropVideoPlayer() throws Exception {
        uriListFlavor = new DataFlavor("text/uri-list;class=java.lang.String");
        javaUrlFlavor = new DataFlavor("application/x-java-url;class=java.net.URL");
        javaFileListFlavor = DataFlavor.javaFileListFlavor;

        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mediaPlayer.setPlaySubItems(true);

        canvas = new Canvas();
        canvas.setBackground(Color.black);

        videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);

        contentPane = new JPanel();
        contentPane.setBackground(Color.black);
        contentPane.setBorder(new LineBorder(new Color(190, 190, 190)));
        contentPane.setLayout(new BorderLayout());
        contentPane.add(canvas, BorderLayout.CENTER);
        contentPane.setTransferHandler(new MyTransferHandler());

        MyMouseAdapter mouseAdapter = new MyMouseAdapter();
        canvas.addMouseListener(mouseAdapter);
        canvas.addMouseMotionListener(mouseAdapter);

        frame = new JFrame("vlcj");
        frame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        frame.setContentPane(contentPane);
        frame.setAlwaysOnTop(true);
        frame.setSize(800, 450);
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
                        String value = (String)transferData;
                        String[] uris = value.split("\\r\\n");
                        if(uris.length > 0) {
                            // Play the first MRL that was dropped (the others are discarded)
                            String uri = uris[0];
                            mediaPlayer.playMedia(uri);
                        }
                        return true;
                    }
                    else if(transferData instanceof URL) {
                        URL value = (URL)transferData;
                        String uri = value.toExternalForm();
                        mediaPlayer.playMedia(uri);
                    }
                    else if(transferData instanceof List) {
                        List<?> value = (List<?>)transferData;
                        if(value.size() > 0) {
                            // Play the first MRL that was dropped (the others are discarded)
                            File file = (File)value.get(0);
                            String uri = file.getAbsolutePath();
                            mediaPlayer.playMedia(uri);
                        }
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
            if(support.isDataFlavorSupported(javaUrlFlavor)) {
                return javaUrlFlavor;
            }
            if(support.isDataFlavorSupported(javaFileListFlavor)) {
                return javaFileListFlavor;
            }
            return null;
        }
    }

    /**
     * Mouse adapter implementation to handle dragging the window by clicking and dragging in the
     * window client area.
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
