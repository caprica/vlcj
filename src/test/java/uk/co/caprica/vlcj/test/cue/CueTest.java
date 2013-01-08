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

package uk.co.caprica.vlcj.test.cue;

import java.util.List;

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListItem;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
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
        if(args.length != 1) {
            System.err.println("Specify a single cue sheet");
            System.exit(1);
        }

        final AudioMediaPlayerComponent player = new AudioMediaPlayerComponent();
        player.getMediaPlayer().prepareMedia(args[0]);

        player.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.out.println("finished");
                dump(player);

                // Play an arbitrary sub-item - note that in this basic test each
                // time the media finishes it will be replayed - a more useful
                // implementation would do something more sophisticated here
                player.getMediaPlayer().playSubItem(5);
            }
        });

        // The sub-items will not be created until the cue sheet is "played" and the
        // media player receives a "finished" event
        player.getMediaPlayer().play();

        Thread.currentThread().join();
    }

    private static void dump(AudioMediaPlayerComponent player) {
        // Dump meta
        List<MediaMeta> metas = player.getMediaPlayer().getSubItemMediaMeta();
        for(MediaMeta meta : metas) {
            System.out.println("meta: " + meta);
        }
        // Dump the media list
        MediaList mediaList = player.getMediaPlayer().subItemsMediaList();
        List<MediaListItem> items = mediaList.items();
        for(MediaListItem item : items) {
            System.out.println(item);
        }
    }
}
