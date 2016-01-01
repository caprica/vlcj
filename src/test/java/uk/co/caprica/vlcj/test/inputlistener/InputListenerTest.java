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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.inputlistener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Test showing how to add mouse and key listeners to the video surface.
 * <p>
 * Normal Java mouse and key listeners just work on Linux.
 * <p>
 * On Windows, some extra steps are needed - note that these extra steps may become the default in
 * a future version of vlcj.
 * <p>
 * Windows used to require the use of the WindowsCanvas, incorporating a native mouse hook and a
 * global AWT event listener.
 * <p>
 * This test does <strong>not</strong> use a WindowsCanvas, does <strong>not</strong> use a native
 * mouse hook and does <strong>not</strong> use a global AWT event listener. Nevertheless, this test
 * shows fully working mouse and keyboard listeners on Windows.
 */
public class InputListenerTest extends VlcjTest {

    /**
     * Main frame.
     */
    private final JFrame mainFrame;

    /**
     * Media player component.
     */
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    /**
     * Application entry-point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.setProperty("vlcj.log", "DEBUG");

        if(args.length != 1) {
            System.err.println("Must specify an MRL");
            System.exit(1);
        }

        // Specify an MRL on the command-line
        final String mrl = args[0];

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InputListenerTest().start(mrl);
            }
        });
    }

    /**
     * Create the user interface.
     */
    public InputListenerTest() {
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        // You *must* do this...
        mediaPlayerComponent.getMediaPlayer().setEnableKeyInputHandling(false);
        mediaPlayerComponent.getMediaPlayer().setEnableMouseInputHandling(false);

        // Add regular Java listeners, no native hook or global event listener shenanigans...
        MouseHandler mouseHandler = new MouseHandler();
        KeyHandler keyHandler = new KeyHandler();

        mediaPlayerComponent.getVideoSurface().addMouseListener(mouseHandler);
        mediaPlayerComponent.getVideoSurface().addMouseMotionListener(mouseHandler);
        mediaPlayerComponent.getVideoSurface().addMouseWheelListener(mouseHandler);

        mediaPlayerComponent.getVideoSurface().addKeyListener(keyHandler);

        mainFrame = new JFrame("vlcj input listener test");
        mainFrame.setBounds(50, 50, 800, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setContentPane(mediaPlayerComponent);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                mediaPlayerComponent.release(true);
            }
        });
        mainFrame.setVisible(true);

        // You must explicitly request focus to the video surface, this is one way to do it...
        // Clicking in the Canvas will *not* by default set focus (you could request focus in
        // response to a mouse-clicked event)
        mediaPlayerComponent.getVideoSurface().requestFocusInWindow();
    }

    private void start(String mrl) {
        mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent event) {
            System.out.println(event);
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            System.out.println(event);
        }

        @Override
        public void mouseEntered(MouseEvent event) {
            System.out.println(event);
        }

        @Override
        public void mouseExited(MouseEvent event) {
            System.out.println(event);
        }

        @Override
        public void mouseMoved(MouseEvent event) {
            System.out.println(event);
        }

        @Override
        public void mousePressed(MouseEvent event) {
            System.out.println(event);
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            System.out.println(event);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent event) {
            System.out.println(event);
        }
    }

    private class KeyHandler extends KeyAdapter {

        @Override
        public void keyTyped(KeyEvent event) {
            System.out.println(event);
        }

        @Override
        public void keyPressed(KeyEvent event) {
            System.out.println(event);
        }

        @Override
        public void keyReleased(KeyEvent event) {
            System.out.println(event);
        }
    }
}
