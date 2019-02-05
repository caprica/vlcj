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

package uk.co.caprica.vlcj.test.cue;

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Very basic test showing using a cue sheet paired with a single mp3 file
 * containing a series of individual tracks.
 * <p>
 * Shows how to "play" the cue-sheet to generate the list of sub-items, and
 * then jump straight to a particular track.
 */
public class CueTest extends VlcjTest {

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.err.println("Specify a single cue sheet");
            System.exit(1);
        }

        final AudioMediaPlayerComponent player = new AudioMediaPlayerComponent();

        player.getMediaPlayer().subItems().setPlaySubItems(false);

        player.getMediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                System.out.println("finished");
                dump(mediaPlayer);

                // Play an arbitrary sub-item - note that in this basic test each
                // time the media finishes it will be replayed - a more useful
                // implementation would do something more sophisticated here
                mediaPlayer.submit(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer.subItems().player().controls().playItem(5);
                    }
                });
            }
        });

        // The sub-items will not be created until the cue sheet is "played" and the
        // media player receives a "finished" event
        System.out.println("before play");

        player.getMediaPlayer().media().playMedia(args[0]);

        System.out.println("played");

        Thread.currentThread().join();
    }

    private static void dump(MediaPlayer player) {
        // Dump meta (this is just a demo, we would actually have to parse the media to get the meta)
        MediaList subitems = player.media().get().subitems().get();
        for (int i = 0; i < subitems.items().count(); i++) {
            Media media = subitems.items().getMedia(i);
            System.out.println("title -> " + media.meta().asMetaData());
        }
        subitems.release();
    }

}
