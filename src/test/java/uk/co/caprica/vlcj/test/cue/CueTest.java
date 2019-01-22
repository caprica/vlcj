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
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

import java.util.List;

/**
 * Very basic test showing using a cue sheet paired with a single mp3 file
 * containing a series of individual tracks.
 * <p>
 * Shows how to "play" the cue-sheet to generate the list of sub-items, and
 * then jump straight to a particular track.
 */
public class CueTest extends VlcjTest {

    public static void main(String[] args) throws InterruptedException {
        args = new String[] {"/home/mark/temp/cue-test/100_blank_and_jones-relax_edition_eight-cd1-2014.m3u"};

        if(args.length != 1) {
            System.err.println("Specify a single cue sheet");
            System.exit(1);
        }

        final AudioMediaPlayerComponent player = new AudioMediaPlayerComponent();
//        player.getMediaPlayer().media().prepareMedia(args[0]);

        player.getMediaPlayer().subItems().setPlaySubItems(true);

        player.getMediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.out.println("finished");
                dump(player);

                // Play an arbitrary sub-item - note that in this basic test each
                // time the media finishes it will be replayed - a more useful
                // implementation would do something more sophisticated here
//                player.getMediaPlayer().subItems().player().controls().playItem(5);
            }
        });

        // The sub-items will not be created until the cue sheet is "played" and the
        // media player receives a "finished" event
        System.out.println("before play");

        player.getMediaPlayer().media().playMedia(args[0]);

        System.out.println("played");

        Thread.currentThread().join();
    }

    private static void dump(AudioMediaPlayerComponent player) {
        // Dump meta
//        List<MediaMeta> metas = player.getMediaPlayer().getSubItemMediaMeta();
//        for(MediaMeta meta : metas) {
//            System.out.println("meta: " + meta);
//        } FIXME
        // Dump the media list
//        MediaList mediaList = player.getMediaPlayer().media().get().subitems().get();
//        List<String> items = mediaList.items().mrls();
//        for(String item : items) {
//            System.out.println(item);
//        }
    }
}
