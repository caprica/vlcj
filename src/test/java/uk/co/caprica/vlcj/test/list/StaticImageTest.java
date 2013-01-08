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

import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;
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
        MediaList playlist = factory.newMediaList();
        playlist.addMedia("/home/mark/1.jpg", "image-duration=5"); // Play picture for 5 seconds
        playlist.addMedia("/home/mark/2.jpg", "image-duration=5");
        MediaListPlayer player = factory.newMediaListPlayer();
        player.setMediaList(playlist);
        player.setMode(MediaListPlayerMode.LOOP);
        player.play();
        Thread.currentThread().join();
    }
}
