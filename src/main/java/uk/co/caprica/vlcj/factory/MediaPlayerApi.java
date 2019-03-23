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

package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

/**
 * Behaviour pertaining to the creation of various types of media players.
 */
public final class MediaPlayerApi extends BaseApi {

    MediaPlayerApi(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Create a new media player.
     * <p>
     * This method is used to create a "base" media player, i.e. one that does not have a video surface that will be
     * embedded into a client application.
     * <p>
     * This could be used (with appropriate factory arguments) to create a "headless" media player (e.g. for audio only)
     * or to create a media player that uses the standard native video output window.
     *
     * @return media player instance
     */
    public MediaPlayer newMediaPlayer() {
        return new MediaPlayer(libvlcInstance);
    }

    /**
     * Create a new embedded media player.
     *
     * @return media player instance
     */
    public EmbeddedMediaPlayer newEmbeddedMediaPlayer() {
        return new EmbeddedMediaPlayer(libvlcInstance);
    }

    /**
     * Create a new play-list media player.
     *
     * @return media player instance
     */
    public MediaListPlayer newMediaListPlayer() {
        return new MediaListPlayer(libvlcInstance);
    }

}
