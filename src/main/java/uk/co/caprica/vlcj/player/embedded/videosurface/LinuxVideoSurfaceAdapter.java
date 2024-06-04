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
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.videosurface;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_set_xwindow;

/**
 * Implementation of a video surface adapter for Linux.
 */
@SuppressWarnings("serial")
final public class LinuxVideoSurfaceAdapter implements VideoSurfaceAdapter {

    @Override
    public void attach(MediaPlayer mediaPlayer, long componentId) {
        libvlc_media_player_set_xwindow(mediaPlayer.mediaPlayerInstance(), (int)componentId);
    }

}
