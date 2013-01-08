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

package uk.co.caprica.vlcj.player.direct;

import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * Specification for a media player that provides direct access to the video frame data.
 * <p>
 * Such a media player is useful for user interface toolkits that do not support the embedding of an
 * AWT Canvas, like JavaFX or an OpenGL toolkit like jMonkeyEngine.
 * <p>
 * A direct media player is also useful for applications that need access to the video frame data to
 * process it in some way.
 */
public interface DirectMediaPlayer extends MediaPlayer {
    // Nothing extra
}
