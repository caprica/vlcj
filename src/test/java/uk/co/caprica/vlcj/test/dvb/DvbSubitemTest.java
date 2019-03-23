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

package uk.co.caprica.vlcj.test.dvb;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.test.VlcjTest;

import java.util.List;

/**
 * A simple test to dump out a DVB play-list from a channels.conf file.
 * <p>
 * This test is not actually constrained to DVB, it will work for any play-list file.
 */
public class DvbSubitemTest extends VlcjTest {

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Specify a channels.conf file");
            System.exit(1);
        }

        final MediaPlayerFactory factory = new MediaPlayerFactory();
        final MediaPlayer mediaPlayer = factory.mediaPlayers().newMediaPlayer();

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                MediaList mediaList = mediaPlayer.media().subitems().newMediaList();

                int subitemCount = mediaList.media().count();
                System.out.println("subitemCount=" + subitemCount);

                System.out.println("Getting sub-items...");

                List<String> mrls = mediaList.media().mrls();
                for(String mrl : mrls) {
                    System.out.println(mrl);
                }

                System.out.println("Getting sub-item meta data...");

                for (int i = 0; i < mediaList.media().count(); i++) {
                    Media media = mediaList.media().newMedia(i);
                    if (media != null) {
                        System.out.println("title -> " + media.meta().get(Meta.TITLE));
                        media.release();
                    }
                }
                mediaList.release();

                System.out.println("Done.");

                mediaPlayer.release();
                factory.release();
                System.exit(0);
            }
        });

        mediaPlayer.media().play(args[0]);

        Thread.currentThread().join();
    }
}
