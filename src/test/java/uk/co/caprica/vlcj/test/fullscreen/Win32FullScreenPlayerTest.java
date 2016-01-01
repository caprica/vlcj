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

package uk.co.caprica.vlcj.test.fullscreen;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * An example of using the native "Win32" full-screen strategy.
 * <p>
 * This uses the native Win32 API.
 */
public class Win32FullScreenPlayerTest extends VlcjTest {

    private JFrame frame;

    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("Specify an MRL to play");
            System.exit(1);
        }

        final String mrl = args[0];

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Win32FullScreenPlayerTest().start(mrl);
            }
        });
    }

    @SuppressWarnings("serial")
    public Win32FullScreenPlayerTest() {
        frame = new JFrame("Win32 Full Screen Strategy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setSize(1200, 800);

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent() {
            @Override
            protected FullScreenStrategy onGetFullScreenStrategy() {
                return new Win32FullScreenStrategy(frame);
            }
        };

        frame.setContentPane(mediaPlayerComponent);

        frame.setVisible(true);
    }

    protected void start(String mrl) {
        mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
        mediaPlayerComponent.getMediaPlayer().setFullScreen(true);
    }
}
