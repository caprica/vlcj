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

package uk.co.caprica.vlcj.test.viewpoint;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.model.Viewpoint;

import static uk.co.caprica.vlcj.component.MediaPlayerComponentBuilder.mediaPlayerComponentBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 360 degree video player example.
 * <p>
 * Click and drag left mouse button left/right for yaw and up/down for pitch.
 * <p>
 * Click and drag right mouse button left/right for roll and up/down for field of view.
 * <p>
 * Scroll-wheel also can be used to adjust field of view.
 * <p>
 * Obviously your video must be a 360 degree video with proper meta data set.
 * <p>
 * This example only uses mouse drags, and the wheel - you could hook up sliders for each axis, or whatever user
 * interface controls you want.
 */
public class ViewpointTest {

    // Threshold is used to determine the minimum drag distance that should register a change in viewpoint - this might
    // make it easier for example to change pitch (Y-axis) without changing yaw (X-axis) when the mouse drag movements
    // are small
    private static final int thresholdX = 2;
    private static final int thresholdY = 2;

    // Amount to increase/decrease field of view by when using the scroll-wheel
    private static final int fovWheelDelta = 3;

    /**
     * Flag to invert the yaw/pitch axis directions.
     */
    private static final boolean invertYaw = false;
    private static final boolean invertPitch = false;

    /**
     * Default field of view value, used to reset.
     * <p>
     * This is the value that VLC uses by default.
     */
    private static final float resetFov = 80.0f;

    private static EmbeddedMediaPlayerComponent mediaPlayer;

    private static Viewpoint viewpoint;

    private static Media media;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Specify the MRL of a 360-degree video");
            System.exit(1);
        }

        new ViewpointTest().start(args[0]);
    }

    private ViewpointTest() {
        mediaPlayer = mediaPlayerComponentBuilder()
            .embedded()
            .embeddedMediaPlayerComponent();

        MouseHandler mouseHandler = new MouseHandler();
        
        mediaPlayer.getVideoSurfaceComponent().addMouseListener(mouseHandler);
        mediaPlayer.getVideoSurfaceComponent().addMouseMotionListener(mouseHandler);
        mediaPlayer.getVideoSurfaceComponent().addMouseWheelListener(mouseHandler);

        viewpoint = mediaPlayer.getMediaPlayer().video().newViewpoint();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(mediaPlayer, BorderLayout.CENTER);

        JFrame frame = new JFrame("Viewpoint Test");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setContentPane(contentPane);
        frame.setBounds(100, 100, 1200, 800);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.release();
                viewpoint.release();
                media.release();
                System.exit(0);
            }
        });

        JPanel controlsPanel = new JPanel();

        JButton pauseButton = new JButton("Pause");
        controlsPanel.add(pauseButton);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mediaPlayer.getMediaPlayer().controls().pause();
            }
        });

        JButton resetButton = new JButton("Reset");
        controlsPanel.add(resetButton);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                viewpoint.setPitch(0);
                viewpoint.setYaw(0);
                viewpoint.setRoll(0);
                viewpoint.setFov(resetFov);
                mediaPlayer.getMediaPlayer().video().updateViewpoint(viewpoint, true);
            }
        });

        JButton reverseButton = new JButton("Reverse");
        controlsPanel.add(reverseButton);
        reverseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                viewpoint.setPitch(0);
                viewpoint.setYaw(180.0f);
                viewpoint.setRoll(0);
                viewpoint.setFov(resetFov);
                mediaPlayer.getMediaPlayer().video().updateViewpoint(viewpoint, true);
            }
        });

        contentPane.add(controlsPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void start(String mrl) {
        media = mediaPlayer.getMediaPlayerFactory().media().newMedia(mrl);
        mediaPlayer.getMediaPlayer().media().set(media);
        mediaPlayer.getMediaPlayer().controls().play();
    }

    private static class MouseHandler extends MouseEventAdapter {

        private Point lastPoint;

        @Override
        public void mouseDragged(MouseEvent e) {
            Point newPoint = e.getPoint();

            // We use relative values to adjust the viewpoint, a delta of 1 seems to work just fine when dragging the
            // mouse - the main calculation is the sign/direction of the delta according to which direction the user has
            // dragged the mouse

            // Since we are using relative values, care must be taken to zero out values for those viewpoint axes that
            // have not changed

            if (lastPoint != null) {
                int dx = newPoint.x - lastPoint.x;
                int dy = newPoint.y - lastPoint.y;

                viewpoint.setFov(0);

                boolean leftButton = SwingUtilities.isLeftMouseButton(e);
                boolean rightButton = SwingUtilities.isRightMouseButton(e);

                if (Math.abs(dx) > thresholdX) {
                    lastPoint.x = newPoint.x;

                    if (leftButton) {
                        if (!invertYaw) {
                            viewpoint.setYaw(dx > 0 ? -1 : 1);
                        } else {
                            viewpoint.setYaw(dx < 0 ? -1 : 1);
                        }
                    } else if (rightButton) {
                        viewpoint.setRoll(dx < 0 ? -1 : 1);
                    }
                } else {
                    viewpoint.setYaw(0);
                    viewpoint.setRoll(0);
                }

                if (Math.abs(dy) > thresholdY) {
                    lastPoint.y = newPoint.y;

                    if (leftButton) {
                        if (!invertPitch) {
                            viewpoint.setPitch(dy > 0 ? -1 : 1);
                        } else {
                            viewpoint.setPitch(dy < 0 ? -1 : 1);
                        }
                    } else if (rightButton) {
                        viewpoint.setFov(dy < 0 ? -1 : 1);
                    }
                } else {
                    viewpoint.setPitch(0);
                    viewpoint.setFov(0);
                }

                mediaPlayer.getMediaPlayer().video().updateViewpoint(viewpoint, false);
            } else {
                lastPoint = newPoint;
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int rotation = e.getWheelRotation();

            viewpoint.setYaw(0);
            viewpoint.setRoll(0);
            viewpoint.setPitch(0);

            viewpoint.setFov(rotation > 0 ? fovWheelDelta : -fovWheelDelta);
            mediaPlayer.getMediaPlayer().video().updateViewpoint(viewpoint, false);
        }
    }

}
