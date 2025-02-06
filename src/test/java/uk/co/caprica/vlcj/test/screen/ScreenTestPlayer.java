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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.screen;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import java.awt.*;

/**
 * An example of "playing" the screen/desktop.
 * <p>
 * See {@link ScreenRecorder} if you want to encode and save the video.
 * <p>
 * Additional media options that can be set are:
 *
 * <pre>
 *   :screen-top=
 *   :screen-left=
 *   :screen-width=
 *   :screen-height=
 * </pre>
 *
 * See <a href="http://wiki.videolan.org/Documentation:Modules/screen">Screen Module</a>.
 */
public class ScreenTestPlayer extends VlcjTest {

    private final JFrame frame;

    private final JPanel contentPane;

    private final Canvas canvas;

    private final MediaPlayerFactory factory;

    private final EmbeddedMediaPlayer mediaPlayer;

    private final VideoSurface videoSurface;

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ScreenTestPlayer().start();
            }
        });
    }

    public ScreenTestPlayer() {
        canvas = new Canvas();
        canvas.setBackground(Color.black);
        canvas.setSize(550, 300);

        contentPane = new JPanel();
        contentPane.setBackground(Color.black);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(canvas, BorderLayout.CENTER);

        frame = new JFrame("vlcj desktop capture");
        frame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();

        videoSurface = factory.videoSurfaces().newVideoSurface(canvas);

        mediaPlayer.videoSurface().set(videoSurface);
    }

    private void start() {
        frame.setVisible(true);

        String mrl = "screen://";

        String[] options = {
            ":screen-fps=30",
            ":screen-caching=100"
        };

        mediaPlayer.media().play(mrl, options);
    }
}
