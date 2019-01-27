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

package uk.co.caprica.vlcj.player.direct;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

import com.sun.jna.Memory;

import java.nio.ByteBuffer;

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

    /**
     * Lock the native memory buffers.
     * <p>
     * The lock must be held for as short a time as possible - locking the buffers will prevent the
     * native video player from filling more frames.
     * <p>
     * A corresponding call to {@link #unlock()} <strong>must</strong> always be made, no matter what
     * the return value from this method.
     * <p>
     * A client application must not interfere with the returned array - the returned array is the
     * "live" array used by the media player, so a client must not add/remove/re-order/etc the
     * returned array.
     * <p>
     * To mitigate this, a defensive copy of the array could have been returned, but this would be
     * added overhead (even though slight) that can be avoided.
     *
     * @return native memory buffers, may be <code>null</code>
     */
    ByteBuffer[] lock();

    /**
     * Unlock the native memory buffers.
     */
    void unlock();
}
