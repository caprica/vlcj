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

package uk.co.caprica.vlcj.test.overlay;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * This example shows how to create simple overlays if you do not want per- pixel translucency or
 * full transparency in your overlays.
 * <p>
 * The key trick is to make sure that the video surface Canvas is added to the container
 * <strong>last</strong>, i.e. after all of the overlay components have been added. It does not
 * matter if the components are visible or not.
 * <p>
 * The only clunky thing in here is the null layout manager and the absolute positioning of
 * components. This is a feature of this example code only, it is not an inherent part of the
 * solution.
 * <p>
 * This situation can be improved by using a transparent overlaid JPanel together with
 * AWTUtilities.setComponentMixingCutoutShape(comp, new Rectangle()).
 * <p>
 * By using such a transparent JPanel if you want to add lightweight components (you still will not
 * get any transparency).
 * <p>
 * Press 'q', 'w', 'e' or 'r' to toggle an overlay.
 */
public class NonTransparentOverlayTest extends VlcjTest {

    private final Frame f;
    private final Canvas c;
    private final Canvas o1;
    private final Canvas o2;
    private final Canvas o3;
    private final Label o4;

    private final MediaPlayerFactory factory;
    private final EmbeddedMediaPlayer mediaPlayer;

    public static void main(final String[] args) {
        if(args.length != 1) {
            System.err.println("Specify an MRL");
            System.exit(1);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NonTransparentOverlayTest().start(args[0]);
            }
        });
    }

    public NonTransparentOverlayTest() {
        Panel p = new Panel();
        p.setBackground(Color.orange); // The label "transparent" background will actually get this
                                       // background colour
        p.setLayout(null);

        c = new Canvas();
        c.setBackground(Color.black);
        c.setBounds(0, 0, 1000, 900);

        o1 = new Canvas();
        o1.setBackground(Color.green);
        o1.setBounds(100, 60, 200, 300);

        o2 = new Canvas();
        o2.setBackground(Color.red);
        o2.setBounds(400, 200, 150, 150);

        o3 = new Canvas();
        o3.setBackground(Color.blue);
        o3.setBounds(50, 500, 200, 200);

        o4 = new Label("I am a label");
        o4.setForeground(Color.red);
        o4.setFont(new Font("Sansserif", Font.BOLD, 48));
        o4.setBounds(400, 600, 400, 50);

        p.add(o1);
        p.add(o2);
        p.add(o3);
        p.add(o4);

        p.add(c); // <--- must be added LAST to appear underneath the other components

        f = new Frame("Heavyweight Overlay Test");
        f.setLayout(new BorderLayout());
        f.setSize(1000, 900);
        f.add(p, BorderLayout.CENTER);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.release();
                factory.release();
                System.exit(0);
            }
        });

        f.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyChar()) {
                    case 'q':
                        o1.setVisible(!o1.isVisible());
                        break;
                    case 'w':
                        o2.setVisible(!o2.isVisible());
                        break;
                    case 'e':
                        o3.setVisible(!o3.isVisible());
                        break;
                    case 'r':
                        o4.setVisible(!o4.isVisible());
                        break;
                }
            }
        });

        factory = new MediaPlayerFactory();
        mediaPlayer = factory.newEmbeddedMediaPlayer();

        CanvasVideoSurface cvs = factory.newVideoSurface(c);
        mediaPlayer.setVideoSurface(cvs);
    }

    private void start(String mrl) {
        f.setVisible(true);
        mediaPlayer.playMedia(mrl);
    }
}
