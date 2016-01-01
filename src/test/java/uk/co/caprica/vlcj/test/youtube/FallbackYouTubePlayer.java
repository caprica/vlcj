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

package uk.co.caprica.vlcj.test.youtube;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * This test demonstrates how to manually handle the playing of YouTube videos.
 * <p>
 * Ordinarily, vlcj will play YouTube videos automatically if you simply invoke
 * {@link MediaPlayer#setPlaySubItems(boolean)}. This will instruct the media player to
 * automatically play any sub-items that get created.
 * <p>
 * To play a YouTube video with vlc, vlc is given the http: "watch" URL - this is not itself a
 * streaming MRL, it is a web-page. This web-page is then scraped, by a LUA script inside of vlc,
 * to locate the actual streaming MRL. If the vlc LUA script finds the MRL, then a sub-item gets
 * created containing that MRL.
 * <p>
 * This sub-item MRL must then itself be played.
 * <p>
 * At this point, either the movie will start playing, or the vlc LUA script did not find the MRL,
 * so it falls back to a "secondary" API which attempts to scrape the streaming MRL in a different
 * way. The result of this secondary API is yet another sub-item, (i.e. sub-item of the sub-item of
 * the original media), and you play that.
 * <p>
 * That all sounds long-winded and complicated, but in essence, to manually handle YouTube videos,
 * you simply play your URL, and in the "finished" event handler you simply keep playing the first
 * sub-item until there are no more.
 * <p>
 * Note that the primary API is the preferred API - the vlc LUA script is out-of-sync with the
 * YouTube web-page format. Ideally the LUA script would be changed. The whole scheme is brittle
 * since the YouTube web-page format could change at any time and be incompatible with the vlc LUA
 * script.
 * <p>
 * The secondary API is invoked by the vlc LUA script as a last-resort.
 * <p>
 * <em>A future version of vlcj will likely support this automatically.</em>
 */
@SuppressWarnings("serial")
public class FallbackYouTubePlayer extends VlcjTest {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private final JFrame frame;

    public static void main(String[] args) throws Exception {
        final String mrl = "http://www.youtube.com/watch?v=gOTyD6ZYcP0";

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
            public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
                // Show the sub-item being added for purposes of the test...
                System.out.println("mediaSubItemAdded: " + mediaPlayerComponent.getMediaPlayer().mrl(subItem));
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.out.println("finished");

                // This is key...
                //
                // On receipt of a "finished" event, check if sub-items have been created...
                List<String> subItems = mediaPlayer.subItems();
                System.out.println("subItems=" + subItems);
                // If sub-items were created...
                if(subItems != null && !subItems.isEmpty()) {
                    // Pick the first sub-item, and play it...
                    String subItemMrl = subItems.get(0);
                    mediaPlayer.playMedia(subItemMrl);
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
        mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
    }
}
