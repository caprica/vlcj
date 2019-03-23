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

package uk.co.caprica.vlcj.test.list;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.PlaybackMode;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Bare bones showing how to "play" static images in a play-list.
 * <p>
 * A proper application would embed a video player and so on, the points to illustrate here are a)
 * playing an image is just like playing any other file, and b) you specify the image duration using
 * a media option.
 * <p>
 * If you do not specify an "image-duration=" media option, you will get a default length of ten
 * seconds.
 * <p>
 * You can also add for example vlc://pause:30 to "play" a delay of 30 (or whatever you want)
 * seconds. This is not to be confused with the media option for image-durations previously
 * described.
 * <p>
 * Note that you can "play" images from any source that vlc can play - not just local files, but
 * things like http: too.
 * <p>
 * Large images can cause problems for vlc when it tries to create the video output - for this
 * reason it is usually better to use an {@link EmbeddedMediaPlayer} linked with the
 * {@link MediaListPlayer}. That way the size of the video surface can be controller. If this is not
 * done, fatal application crashes due to vlc choking on the image file can occur.
 */
public class StaticImageTest extends VlcjTest {

    public static void main(String[] args) throws Exception {
        MediaPlayerFactory factory = new MediaPlayerFactory();
        MediaList playlist = factory.media().newMediaList();
        playlist.media().add("/home/mark/p1.jpg", "image-duration=5"); // Play picture for 5 seconds
        playlist.media().add("/home/mark/p2.jpg", "image-duration=3");
        MediaListPlayer player = factory.mediaPlayers().newMediaListPlayer();
        MediaListRef playlistRef = playlist.newMediaListRef();
        try {
            player.list().setMediaList(playlistRef);
        }
        finally {
            playlistRef.release();
        }
        player.controls().setMode(PlaybackMode.LOOP);
        player.controls().play();
        Thread.currentThread().join();
    }
}
