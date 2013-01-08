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

package uk.co.caprica.vlcj.test.dvb;

import java.util.List;

import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

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
        final MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                int subItemCount = mediaPlayer.subItemCount();
                System.out.println("subItemCount=" + subItemCount);

                System.out.println("Getting sub-items...");

                List<String> subItems = mediaPlayer.subItems();
                for(String subItem : subItems) {
                    System.out.println(subItem);
                }

                System.out.println("Getting sub-item meta data...");

                List<MediaMeta> metas = mediaPlayer.getSubItemMediaMeta();
                for(MediaMeta meta : metas) {
                    System.out.println("title -> " + meta.getTitle());
                }

                System.out.println("Done.");

                mediaPlayer.release();
                factory.release();
                System.exit(0);
            }
        });

        mediaPlayer.playMedia(args[0]);

        Thread.currentThread().join();
    }
}
