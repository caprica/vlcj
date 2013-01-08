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

package uk.co.caprica.vlcj.player.embedded.videosurface;

import java.io.Serializable;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * Specification for a component that can attach a video surface to a native media player instance.
 * <p>
 * An adapter is needed since attaching a video surface uses an operating system-specific
 * implementation.
 */
public interface VideoSurfaceAdapter extends Serializable {

    /**
     * Attach a video surface to a media player.
     *
     * @param libvlc native interface
     * @param mediaPlayer media player instance
     * @param componentId native id of the video surface component
     */
    void attach(LibVlc libvlc, MediaPlayer mediaPlayer, long componentId);
}
