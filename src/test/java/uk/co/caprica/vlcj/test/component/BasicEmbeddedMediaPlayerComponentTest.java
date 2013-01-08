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

package uk.co.caprica.vlcj.test.component;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Test demonstrating the {@link EmbeddedMediaPlayerComponent}.
 * <p>
 * Leaving aside the standard Swing initialisation code, there are only <em>two</em> lines of vlcj
 * code required to create a media player and play a video.
 */
public class BasicEmbeddedMediaPlayerComponentTest extends VlcjTest {

    /**
     * Media player component.
     */
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    /**
     * Application entry point.
     *
     * @param args
     */
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Specify an mrl");
            System.exit(1);
        }

        final String mrl = args[0];

        setLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BasicEmbeddedMediaPlayerComponentTest().start(mrl);
            }
        });
    }

    /**
     * Create a new test.
     */
    private BasicEmbeddedMediaPlayerComponentTest() {
        JFrame frame = new JFrame("vlcj Media Player Component Test");

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.setContentPane(mediaPlayerComponent);

        frame.setLocation(100, 100);
        frame.setSize(1050, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Start playing a movie.
     *
     * @param mrl mrl
     */
    private void start(String mrl) {
        // One line of vlcj code to play the media...
        mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
    }
}
