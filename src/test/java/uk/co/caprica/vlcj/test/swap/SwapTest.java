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

package uk.co.caprica.vlcj.test.swap;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * This test demonstrates the problems associated with attempting to change the video surface
 * component while a video is playing.
 * <p>
 * In short any change to the video surface only takes effect if the video is stopped and started
 * again.
 * <p>
 * Consequently this test is only useful to demonstrate that something does NOT work.
 * <p>
 * This behaviour is a result of the libvlc/vlc implementation, not the bindings.
 */
public class SwapTest extends VlcjTest {

    private final JFrame frame;
    private final JPanel contentPane;
    private final Canvas previewCanvas;
    private final Canvas mainCanvas;
    private final JPanel controlsPanel;
    private final JButton showMainButton;
    private final JButton showPreviewButton;
    private final JButton playButton;
    private final JButton pauseButton;
    private final JButton stopButton;

    private final MediaPlayerFactory factory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final CanvasVideoSurface previewVideoSurface;
    private final CanvasVideoSurface mainVideoSurface;

    public static void main(final String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Specify an MRL");
            System.exit(1);
        }

        setLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SwapTest().start(args[0]);
            }
        });
    }

    public SwapTest() {
        previewCanvas = new Canvas();
        previewCanvas.setBackground(Color.black);
        previewCanvas.setPreferredSize(new Dimension(400, 250));

        mainCanvas = new Canvas();
        mainCanvas.setBackground(Color.black);
        mainCanvas.setPreferredSize(new Dimension(800, 500));

        showMainButton = new JButton("Main");
        showMainButton.setMnemonic('m');

        showPreviewButton = new JButton("Preview");
        showPreviewButton.setMnemonic('v');

        playButton = new JButton("Play");
        playButton.setMnemonic('p');

        pauseButton = new JButton("Pause");
        pauseButton.setMnemonic('s');

        stopButton = new JButton("Stop");
        stopButton.setMnemonic('t');

        controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.X_AXIS));
        controlsPanel.add(Box.createHorizontalGlue());
        controlsPanel.add(showPreviewButton);
        controlsPanel.add(Box.createHorizontalStrut(8));
        controlsPanel.add(showMainButton);
        controlsPanel.add(Box.createHorizontalStrut(32));
        controlsPanel.add(playButton);
        controlsPanel.add(Box.createHorizontalStrut(8));
        controlsPanel.add(stopButton);
        controlsPanel.add(Box.createHorizontalStrut(8));
        controlsPanel.add(pauseButton);
        controlsPanel.add(Box.createHorizontalGlue());

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(16, 16, 16, 16));
        contentPane.setLayout(new BorderLayout(16, 16));

        contentPane.add(previewCanvas, BorderLayout.WEST);
        contentPane.add(mainCanvas, BorderLayout.CENTER);
        contentPane.add(controlsPanel, BorderLayout.SOUTH);

        contentPane.add(new JLabel("<html>This test shows that it is <b>not</b> possible to update a video surface on-the-fly, you must <b>stop</b> and <b>play</b> the video again to effect the change.</hrml>"), BorderLayout.NORTH);

        frame = new JFrame("vlcj switch video test");
        frame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(contentPane);
        frame.setSize(1100, 400);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.release();
                factory.release();
            }
        });

        showPreviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.setVideoSurface(previewVideoSurface);
                mediaPlayer.attachVideoSurface();
            }
        });

        showMainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.setVideoSurface(mainVideoSurface);
                mediaPlayer.attachVideoSurface();
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.play();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.stop();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.pause();
            }
        });

        factory = new MediaPlayerFactory("--no-video-title-show");

        mediaPlayer = factory.newEmbeddedMediaPlayer();

        previewVideoSurface = factory.newVideoSurface(previewCanvas);
        mainVideoSurface = factory.newVideoSurface(mainCanvas);
    }

    private void start(String mrl) {
        frame.setVisible(true);
        mediaPlayer.prepareMedia(mrl);
    }
}
