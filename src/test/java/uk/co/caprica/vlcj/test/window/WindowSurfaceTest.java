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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.window;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MarqueePosition;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This test uses a Window for the video surface.
 * <p>
 * The Window is kept in sync with a parent frame giving the appearance of it being properly embedded.
 * <p>
 * This is an approach that could be used to get the optimal EmbeddedMediaPlayer solution working on OSX - on OSX after
 * JDK 6 there is no heavyweight AWT so a Canvas embedded in a JFrame can not be used, but a Window should still work.
 */
public class WindowSurfaceTest {

    private MediaPlayerFactory factory;

    private EmbeddedMediaPlayer mediaPlayer;

    private Window window;

    private VideoSurface videoSurface;

    private final Rectangle bounds = new Rectangle();

    private JFrame frame;

    private JPanel referencePanel;

    public static void main(String[] args) throws InterruptedException {
        new WindowSurfaceTest(args);

        Thread.currentThread().join();
    }

    public WindowSurfaceTest(String[] args) {
        frame = new JFrame("Window Video Surface Test");
        frame.setBounds(50, 50, 800, 600);
        frame.setBackground(Color.black);
        frame.getContentPane().setBackground(Color.black);

        referencePanel = new JPanel();
        referencePanel.setBackground(Color.black);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(referencePanel);

        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        window = new Window(frame);
        window.setBackground(Color.black);
        videoSurface = factory.videoSurfaces().newVideoSurface(window);
        mediaPlayer.videoSurface().set(videoSurface);
        window.setBounds(100, 100, 800, 600);
        window.setIgnoreRepaint(true);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Pause");
        JButton stopButton = new JButton("Stop");

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mediaPlayer.controls().play();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mediaPlayer.controls().pause();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mediaPlayer.controls().stop();
            }
        });

        buttonsPanel.add(playButton);
        buttonsPanel.add(pauseButton);
        buttonsPanel.add(stopButton);

        frame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                syncVideoSurface();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                syncVideoSurface();
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                release();
                System.exit(0);
            }
        });

        frame.setVisible(true);
        syncVideoSurface();

        window.setVisible(true);

        mediaPlayer.logo().setFile("etc/vlcj-logo.png");
        mediaPlayer.logo().setOpacity(0.3f);
        mediaPlayer.logo().enable(true);

        mediaPlayer.marquee().setText("Window video surface");
        mediaPlayer.marquee().setPosition(MarqueePosition.BOTTOM_RIGHT);
        mediaPlayer.marquee().setLocation(10, 10);
        mediaPlayer.marquee().enable(true);

        mediaPlayer.media().prepare(args[0]);
    }

    private void syncVideoSurface() {
        if (frame.isVisible()) {
            // We re-size and re-position to the reference panel contained within the frame rather than the frame
            // itself, this makes it easy to add other UI elements without worrying about calculating the correct size
            // and position for the video surface ourselves - the referencePanel is in effect a "proxy" for the video
            // surface
            referencePanel.getBounds(bounds);
            bounds.setLocation(referencePanel.getLocationOnScreen());
            window.setBounds(bounds);
        }
    }

    private void release() {
        mediaPlayer.controls().stop();
        mediaPlayer.release();
        factory.release();
    }

}
