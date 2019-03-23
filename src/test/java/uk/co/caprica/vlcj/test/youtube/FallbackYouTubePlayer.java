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

package uk.co.caprica.vlcj.test.youtube;

import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import java.util.List;

/**
 * This test demonstrates how to manually handle the playing of YouTube videos.
 * <p>
 * To play a YouTube video with vlc, vlc is given the http: "watch" URL - this is not itself a streaming MRL, it is a
 * web-page. This web-page is then scraped, by a LUA script inside of VLC, to locate the actual streaming MRL. If the
 * VLC LUA script finds the MRL, then a sub-item gets created containing that MRL.
 * <p>
 * This sub-item MRL must then itself be played.
 */
@SuppressWarnings("serial")
public class FallbackYouTubePlayer extends VlcjTest {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private final JFrame frame;

    public static void main(String[] args) throws Exception {
        final String mrl = "https://www.youtube.com/watch?v=d4GGZluIqJo";

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FallbackYouTubePlayer().start(mrl);
            }
        });

        Thread.currentThread().join();
    }

    public FallbackYouTubePlayer() {
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent() {

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.out.println("finished");

                // This is key...
                //
                // On receipt of a "finished" event, check if sub-items have been created...
                MediaList mediaList = mediaPlayer.media().subitems().newMediaList();
                List<String> subitems = mediaList.media().mrls();
                mediaList.release();
                System.out.println("subitems=" + subitems);
                // If sub-items were created...
                if(subitems != null && !subitems.isEmpty()) {
                    // Pick the first sub-item, and play it...
                    String subitemMrl = subitems.get(0);
                    mediaPlayer.media().play(subitemMrl);
                    // What will happen next...
                    //
                    // 1. if the vlc lua script finds the streaming MRL via the normal i.e.
                    //    "primary" method, then this subitem MRL will be the streaming MRL; or
                    // 2. if the vlc lua script does not find the streaming MRL via the primary
                    //    method, then the vlc lua script fallback method is tried to locate the
                    //    streaming MRL and the next time a "finished" event is received there will
                    //    be a new sub-item for the just played subitem, and that will be the
                    //    streaming MRL
                }
                else {
                    // Your own application would not exit, but would instead probably set some
                    // state flag or fire some sort of signal or whatever that the media actually
                    // finished
                    System.exit(0);
                }
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                // For some reason, even if things work, you get an error... you have to ignore
                // this error - but that of course makes handling real errors tricky
                System.out.println("Error!!!");
            }
        };

        frame = new JFrame("vlcj YouTube fallback test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 1200, 800);
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);
    }

    private void start(String mrl) {
        mediaPlayerComponent.mediaPlayer().media().play(mrl);
    }
}
