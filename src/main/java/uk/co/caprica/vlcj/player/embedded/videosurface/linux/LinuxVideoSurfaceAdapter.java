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

package uk.co.caprica.vlcj.player.embedded.videosurface.linux;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapter;

/**
 * Implementation of a video surface adapter for Linux.
 */
public class LinuxVideoSurfaceAdapter implements VideoSurfaceAdapter {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void attach(LibVlc libvlc, MediaPlayer mediaPlayer, long componentId) {
        Logger.debug("attach(componentId={})", componentId);
        libvlc.libvlc_media_player_set_xwindow(mediaPlayer.mediaPlayerInstance(), (int)componentId);
    }
}
