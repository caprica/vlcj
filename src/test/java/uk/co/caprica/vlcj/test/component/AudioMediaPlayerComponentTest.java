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

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Test demonstrating the {@link AudioMediaPlayerComponent}.
 */
public class AudioMediaPlayerComponentTest extends VlcjTest {

    /**
     * Media player component.
     */
    private final AudioMediaPlayerComponent audioMediaPlayerComponent;

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

        String mrl = args[0];

        // In this test, we must keep an object reference here otherwise the media
        // player will become eligible for garbage collection immediately, causing
        // a potentially fatal JVM crash - this is just an artefact of this test,
        // ordinarily an application would be keeping a reference to the component
        // anyway
        AudioMediaPlayerComponentTest test = new AudioMediaPlayerComponentTest();
        test.start(mrl);

        // Since there is no UI, we must join here to prevent the application from
        // exiting and destroying the media player
        try {
            Thread.currentThread().join();
        }
        catch(InterruptedException e) {
        }
    }

    /**
     * Create a new test.
     */
    private AudioMediaPlayerComponentTest() {
        audioMediaPlayerComponent = new AudioMediaPlayerComponent();
        audioMediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void stopped(MediaPlayer mediaPlayer) {
                System.exit(0);
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.exit(0);
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.exit(1);
            }
        });
    }

    /**
     * Start playing media.
     *
     * @param mrl mrl
     */
    private void start(String mrl) {
        // One line of vlcj code to play the media...
        audioMediaPlayerComponent.getMediaPlayer().playMedia(mrl);
    }
}
