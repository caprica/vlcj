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

import java.awt.Canvas;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Test demonstrating the {@link EmbeddedMediaPlayerComponent} and how to tailor it by sub-classing
 * and overriding template methods.
 */
@SuppressWarnings("serial")
public class AdvancedEmbeddedMediaPlayerComponentTest extends VlcjTest {

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
                new AdvancedEmbeddedMediaPlayerComponentTest().start(mrl);
            }
        });
    }

    /**
     * Create a new test.
     */
    private AdvancedEmbeddedMediaPlayerComponentTest() {
        JFrame frame = new JFrame("vlcj Media Player Component Test");

        // Create a sub-class of the embedded media player component (it does not
        // need to be an inner class, it could be a standalone top-level class) and
        // override one or more of the template methods to tailor the component to
        // your needs (this example implementation doesn't actually do anything
        // different)...
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent() {
            @Override
            protected MediaPlayerFactory onGetMediaPlayerFactory() {
                return super.onGetMediaPlayerFactory();
            }

            @Override
            protected String[] onGetMediaPlayerFactoryArgs() {
                return super.onGetMediaPlayerFactoryArgs();
            }

            @Override
            protected FullScreenStrategy onGetFullScreenStrategy() {
                return super.onGetFullScreenStrategy();
            }

            @Override
            protected Canvas onGetCanvas() {
                return super.onGetCanvas();
            }

            @Override
            protected void onBeforeRelease() {
                super.onBeforeRelease();
            }

            @Override
            protected void onAfterRelease() {
                super.onAfterRelease();
            }

            // Override whatever listener methods you're interested in...

            @Override
            public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
                super.videoOutput(mediaPlayer, newCount);
            }

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                super.playing(mediaPlayer);
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                super.error(mediaPlayer);
            }
        };

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
        mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
    }
}
