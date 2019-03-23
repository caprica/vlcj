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

import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.MediaParsedStatus;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;
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

        final AudioPlayerComponent player = new AudioPlayerComponent();

        player.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                System.out.println("finished");
                dump(mediaPlayer);
            }
        });

        // The sub-items will not be created until the cue sheet is parsed and the parsed status event is raised
        System.out.println("before play");

        player.mediaPlayer().media().prepare(args[0]);
        player.mediaPlayer().media().events().addMediaEventListener(new MediaEventAdapter() {
            @Override
            public void mediaSubItemAdded(Media media, MediaRef newChild) {
                System.out.println("ITEM ADDED");
            }

            @Override
            public void mediaSubItemTreeAdded(Media media, MediaRef item) {
                System.out.println("TREE ADDED");
            }

            @Override
            public void mediaParsedChanged(Media media, MediaParsedStatus newStatus) {
                System.out.println("PARSED " + newStatus);
                dump(player.mediaPlayer());
                // Play an arbitrary sub-item to prove it works for the sake of example, this will fail of course if the
                // index is out of range for your playlist
                player.mediaPlayer().subitems().controls().play(2);
            }
        });

        player.mediaPlayer().media().parsing().parse();

        System.out.println("played");

        Thread.currentThread().join();
    }

    private static void dump(MediaPlayer player) {
        // Dump meta (this is just a demo, we would actually have to parse the media to get the meta)
        MediaList subitems = player.media().subitems().newMediaList();
        for (int i = 0; i < subitems.media().count(); i++) {
            Media media = subitems.media().newMedia(i);
            if (media != null) {
                System.out.println("title -> " + media.meta().asMetaData());
                media.release();
            }
        }
        subitems.release();
    }

}
