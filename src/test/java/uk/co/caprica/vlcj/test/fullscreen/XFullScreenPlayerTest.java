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

package uk.co.caprica.vlcj.test.fullscreen;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.x.XFullScreenStrategy;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * An example of using the "X" full-screen strategy.
 * <p>
 * This is without doubt the recommended strategy to use for full-screen media players - at least it
 * is on Linux.
 */
public class XFullScreenPlayerTest extends VlcjTest {

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
                new XFullScreenPlayerTest().start(mrl);
            }
        });
    }

    @SuppressWarnings("serial")
    public XFullScreenPlayerTest() {
        frame = new JFrame("LibX11 Full Screen Strategy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setSize(1200, 800);

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent() {
            @Override
            protected FullScreenStrategy onGetFullScreenStrategy() {
                return new XFullScreenStrategy(frame);
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
