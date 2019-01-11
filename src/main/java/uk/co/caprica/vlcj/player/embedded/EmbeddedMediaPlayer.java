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

package uk.co.caprica.vlcj.player.embedded;

import java.awt.Window;
import java.awt.image.BufferedImage;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 * Specification for a media player component that is intended to be embedded in a user-interface
 * component.
 *  <p>
 * Note that to get mouse and keyboard events delivered via listeners on some platforms (i.e. Windows)
 * you will likely need to invoke {@link #setEnableMouseInputHandling(boolean)} <em>and</em>
 * {@link #setEnableKeyInputHandling(boolean)}.
 */
public interface EmbeddedMediaPlayer extends MediaPlayer {

    FullScreenService fullScreen();

    InputService input();

    OverlayService overlay();

    VideoSurfaceService videoSurface();

}
