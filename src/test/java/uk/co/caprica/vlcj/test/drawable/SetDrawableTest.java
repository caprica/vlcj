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

package uk.co.caprica.vlcj.test.drawable;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.peer.ComponentPeer;
import java.lang.reflect.Method;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapter;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.test.VlcjTest;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * Test the behaviour of the various set drawable methods.
 * <p>
 * This application creates a launcher for three frames, each with an embedded media player.
 * Clicking a button launches a frame and plays a video.
 * <p>
 * Any particular test only works if the video is played correctly embedded inside the frame and no
 * native video window is opened.
 * <p>
 * The three available methods to set the video surface are:
 * <ul>
 * <li>libvlc_media_player_set_agl</li>
 * <li>libvlc_media_player_set_nsobject</li>
 * <li>libvlc_media_player_set_xwindow, this is what is used on Linux</li>
 * </ul>
 * <p>
 * You must pass an MRL for a media file to play as the only command-line argument.
 * <p>
 * This is used primarily to test behaviour on MacOS platforms. If this test does not work (which is
 * likely the case on MacOS), then you simply can not have a media player embedded in your
 * application. There is no other way provided by libvlc to make this work.
 * <p>
 * The Windows implementation is excluded from this since it works and is irrelevant.
 * <p>
 * Setting a Canvas for the video surface does not work on MacOS because a standard java.awt.Canvas
 * knows nothing about the VLC view embedding protocol. The solution is to provide a CococaComponent
 * implementation and a native NSView implementation (similar to that in vlc's VLCVideoView.m).
 */
public class SetDrawableTest extends VlcjTest {

    private final MediaPlayerFactory mediaPlayerFactory;
    private final EmbeddedMediaPlayer aglMediaPlayer;
    private final EmbeddedMediaPlayer nsobjectMediaPlayer;
    private final EmbeddedMediaPlayer nsviewMediaPlayer;
    private final EmbeddedMediaPlayer xwindowMediaPlayer;

    private final Canvas aglCanvas;
    private final Canvas nsobjectCanvas;
    private final Canvas nsviewCanvas;
    private final Canvas xwindowCanvas;

    private final CanvasVideoSurface aglVideoSurface;
    private final CanvasVideoSurface nsobjectVideoSurface;
    private final CanvasVideoSurface nsviewVideoSurface;
    private final CanvasVideoSurface xwindowVideoSurface;

    private final JPanel mainFrameContentPane;
    private final JButton aglButton;
    private final JButton nsobjectButton;
    private final JButton nsviewButton;
    private final JButton xwindowButton;

    private final JFrame mainFrame;
    private final VideoFrame aglFrame;
    private final VideoFrame nsobjectFrame;
    private final VideoFrame nsviewFrame;
    private final VideoFrame xwindowFrame;

    public static void main(final String[] args) {
        if(args.length != 1) {
            System.out.println("Specify a single MRL");
            System.exit(1);
        }

        Logger.setLevel(Logger.Level.INFO);

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

        mediaPlayerFactory = new MediaPlayerFactory("--no-video-title-show");
        aglMediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        nsobjectMediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        nsviewMediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        xwindowMediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();

        aglCanvas = new Canvas();
        aglCanvas.setBackground(Color.black);

        nsobjectCanvas = new Canvas();
        nsobjectCanvas.setBackground(Color.black);

        nsviewCanvas = new Canvas();
        nsviewCanvas.setBackground(Color.black);

        xwindowCanvas = new Canvas();
        xwindowCanvas.setBackground(Color.black);

        aglVideoSurface = new CanvasVideoSurface(aglCanvas, new VideoSurfaceAdapter() {
            @Override
            public void attach(LibVlc libvlc, MediaPlayer mediaPlayer, long componentId) {
                dump("AGL", aglCanvas);
                libvlc.libvlc_media_player_set_agl(aglMediaPlayer.mediaPlayerInstance(), RuntimeUtil.safeLongToInt(componentId));
            }
        });

        nsobjectVideoSurface = new CanvasVideoSurface(nsobjectCanvas, new VideoSurfaceAdapter() {
            @Override
            public void attach(LibVlc libvlc, MediaPlayer mediaPlayer, long componentId) {
                dump("NSObject", nsobjectCanvas);
                libvlc.libvlc_media_player_set_nsobject(nsobjectMediaPlayer.mediaPlayerInstance(), componentId);
            }
        });

        nsviewVideoSurface = new CanvasVideoSurface(nsviewCanvas, new VideoSurfaceAdapter() {
            @Override
            public void attach(LibVlc libvlc, MediaPlayer mediaPlayer, long componentId) {
                dump("NSView", nsviewCanvas);
                // TODO shouldn't this component pointer be on the attach template method?
                // (that may be moot since it returns the same value anyway)
                try {
                    long viewPtr = getViewPointer(nsviewCanvas);
                    libvlc.libvlc_media_player_set_nsobject(nsobjectMediaPlayer.mediaPlayerInstance(), viewPtr);
                }
                catch(Throwable t) {
                    System.out.println("Failed to set nsobject view");
                }
            }
        });

        xwindowVideoSurface = new CanvasVideoSurface(xwindowCanvas, new VideoSurfaceAdapter() {
            @Override
            public void attach(LibVlc libvlc, MediaPlayer mediaPlayer, long componentId) {
                dump("XWindow", xwindowCanvas);
                libvlc.libvlc_media_player_set_xwindow(xwindowMediaPlayer.mediaPlayerInstance(), RuntimeUtil.safeLongToInt(componentId));
            }
        });

        aglMediaPlayer.setVideoSurface(aglVideoSurface);
        nsobjectMediaPlayer.setVideoSurface(nsobjectVideoSurface);
        nsviewMediaPlayer.setVideoSurface(nsviewVideoSurface);
        xwindowMediaPlayer.setVideoSurface(xwindowVideoSurface);

        aglFrame = new VideoFrame("AGL", aglMediaPlayer);
        nsobjectFrame = new VideoFrame("NSObject", nsobjectMediaPlayer);
        nsviewFrame = new VideoFrame("NSView", nsviewMediaPlayer);
        xwindowFrame = new VideoFrame("XWindow", xwindowMediaPlayer);

        aglFrame.getContentPane().setLayout(new BorderLayout());
        aglFrame.getContentPane().add(aglCanvas, BorderLayout.CENTER);

        nsobjectFrame.getContentPane().setLayout(new BorderLayout());
        nsobjectFrame.getContentPane().add(nsobjectCanvas, BorderLayout.CENTER);

        nsviewFrame.getContentPane().setLayout(new BorderLayout());
        nsviewFrame.getContentPane().add(nsviewCanvas, BorderLayout.CENTER);

        xwindowFrame.getContentPane().setLayout(new BorderLayout());
        xwindowFrame.getContentPane().add(xwindowCanvas, BorderLayout.CENTER);

        aglFrame.setLocation(50, 150);
        nsobjectFrame.setLocation(50, 350);
        nsviewFrame.setLocation(50, 550);
        xwindowFrame.setLocation(50, 750);

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

        nsviewButton = new JButton("NSView");
        nsviewButton.setMnemonic('v');
        nsviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nsviewFrame.start(mrl);
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
        mainFrameContentPane.add(nsviewButton);
        mainFrameContentPane.add(xwindowButton);

        mainFrame = new JFrame("vlcj Drawable Test");
        mainFrame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        mainFrame.setLocation(50, 50);
        mainFrame.setContentPane(mainFrameContentPane);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);

        aglFrame.setVisible(true);
        nsobjectFrame.setVisible(true);
        nsviewFrame.setVisible(true);
        xwindowFrame.setVisible(true);
    }

    private class VideoFrame extends JFrame {

        private static final long serialVersionUID = 1L;

        private final EmbeddedMediaPlayer mediaPlayer;

        private VideoFrame(String title, EmbeddedMediaPlayer mediaPlayer) {
            super(title);
            this.mediaPlayer = mediaPlayer;
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            setSize(320, 180);
            pack();
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // VideoFrame.this.mediaPlayer.stop();
                }
            });
        }

        private void start(final String mrl) {
            setVisible(true);
            toFront();
            mediaPlayer.playMedia(mrl);
        }
    }

    @SuppressWarnings("deprecation")
    private void dump(String s, Canvas c) {
        System.out.println();
        System.out.println(s);
        System.out.println("        component: " + c);
        System.out.println("   component peer: " + c.getPeer());
        System.out.println("component pointer: " + Native.getComponentPointer(c));
        System.out.println("   native pointer: " + Pointer.nativeValue(Native.getComponentPointer(c)));
        System.out.println("     component id: " + Native.getComponentID(c));
        try {
            System.out.println("     view pointer: " + getViewPointer(c));
        }
        catch(Throwable t) {
            System.out.println("     view pointer: " + t.getMessage());
        }
        System.out.println();
    }

    /**
     * Get the component's peer handle.
     * <p>
     * Note that this does in fact return the same value as the JNA Native.componentID() method, so
     * is actually redundant.
     * <p>
     * It exists here for the sake of investigation.
     * <p>
     * On MacOS, the peer's getViewPtr() method returns a handle for an NSView object instance.
     *
     * @param component component
     * @return peer
     */
    private long getViewPointer(Component component) {
        try {
            @SuppressWarnings("deprecation")
            ComponentPeer peer = component.getPeer();
            Method method = peer.getClass().getMethod("getViewPtr");
            Object result = method.invoke(peer);
            System.out.println("result: " + result);
            if(result != null) {
                System.out.println("class: " + result.getClass());
                return Long.parseLong(result.toString());
            }
            else {
                throw new RuntimeException("Failed to get view pointer for " + component);
            }
        }
        catch(Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
