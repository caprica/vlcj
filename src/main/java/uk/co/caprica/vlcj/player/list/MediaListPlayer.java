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

package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_player_t;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * Specification for a media list player component.
 * <p>
 * A media list player can be used with an embedded media player (without this a native video window
 * will be opened when video is played). For example:
 *
 * <pre>
 * MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
 *
 * Canvas canvas = new Canvas();
 * canvas.setBackground(Color.black);
 * CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
 *
 * EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
 * mediaPlayer.setVideoSurface(videoSurface);
 *
 * MediaListPlayer mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();
 *
 * // Important: associate the media player with the media list player
 * mediaListPlayer.setMediaPlayer(mediaPlayer);
 *
 * MediaList mediaList = mediaPlayerFactory.newMediaList();
 * mediaList.addMedia(&quot;/movies/1.mp4&quot;);
 * mediaList.addMedia(&quot;/movies/2.mp4&quot;);
 * mediaList.addMedia(&quot;/movies/3.mp4&quot;);
 *
 * mediaListPlayer.setMediaList(mediaList);
 * mediaListPlayer.setMode(MediaListPlayerMode.LOOP);
 *
 * mediaListPlayer.play();
 * </pre>
 */
public interface MediaListPlayer {

    ControlsService controls();

    EventService events();

    ListService list();

    MediaPlayerService mediaPlayer();

    ModeService mode();

    StatusService status();

    UserDataService userData();

    /**
     * Release the media list player resources.
     */
    void release();

    void submit(Runnable r);

    libvlc_media_list_player_t mediaListPlayerInstance(); // FIXME check this needs to be on interface

}
