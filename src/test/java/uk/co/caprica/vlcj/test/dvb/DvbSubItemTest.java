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
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

import java.util.List;

/**
 * A simple test to dump out a DVB play-list from a channels.conf file.
 * <p>
 * This test is not actually constrained to DVB, it will work for any play-list file.
 */
public class DvbSubItemTest extends VlcjTest {

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
                int subItemCount = mediaPlayer.media().get().subitems().get().items().count();
                System.out.println("subItemCount=" + subItemCount);

                System.out.println("Getting sub-items...");

                List<String> subItems = mediaPlayer.media().get().subitems().get().items().mrls();
                for(String subItem : subItems) {
                    System.out.println(subItem);
                }

                System.out.println("Getting sub-item meta data...");

//                List<MediaMeta> metas = mediaPlayer.getSubItemMediaMeta();
//                for(MediaMeta meta : metas) {
//                    System.out.println("title -> " + meta.getTitle());
//                } FIXME

                System.out.println("Done.");

                mediaPlayer.release();
                factory.release();
                System.exit(0);
            }
        });

        mediaPlayer.media().playMedia(args[0]);

        Thread.currentThread().join();
    }
}
