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

package uk.co.caprica.vlcj.test.list;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * A simple example showing how to use a media list player.
 * <p>
 * This test does not embed a video window so a new native video window will be created for each
 * movie in the play-list.
 */
public class TestMediaListPlayer extends VlcjTest {

    public static void main(String[] args) throws Exception {
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

        MediaListPlayer mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();

        mediaListPlayer.addMediaListPlayerEventListener(new MediaListPlayerEventAdapter() {
            @Override
            public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl) {
                System.out.println("nextItem()");
            }
        });

        MediaList mediaList = mediaPlayerFactory.newMediaList();
        mediaList.addMedia("/home/movies/one.mp4");
        mediaList.addMedia("/home/movies/two.mp4");
        mediaList.addMedia("/home/movies/three.mp4");

        mediaListPlayer.setMediaList(mediaList);
        mediaListPlayer.setMode(MediaListPlayerMode.LOOP);

        mediaListPlayer.play();

        Thread.currentThread().join();

        // mediaList.release();
        // mediaListPlayer.release();
        // mediaPlayerFactory.release();
    }
}
