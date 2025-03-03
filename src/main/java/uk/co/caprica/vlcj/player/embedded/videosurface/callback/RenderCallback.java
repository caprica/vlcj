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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.videosurface.callback;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.nio.ByteBuffer;

/**
 * Specification for a component that wishes to be called back to process video frames.
 * <p>
 * The render call-back provides access to the native memory buffer, if instead the full RGB integer data is required
 * for the full video frame then consider using {@link RenderCallbackAdapter}.
 * <p>
 * The render call-back is invoked by a <em>native</em> thread.
 *
 * @see RenderCallbackAdapter
 */
public interface RenderCallback {

    /**
     * Lock the video buffer prior to receiving a new frame.
     * <p>
     * Implementations of this method must execute as quickly as possible.
     *
     * @param mediaPlayer media player to which the event relates
     */
    void lock(MediaPlayer mediaPlayer);

    /**
     * Call-back when ready to display a video frame.
     * <p>
     * Implementations of this method must execute as quickly as possible.
     *
     * @param mediaPlayer media player to which the event relates
     * @param nativeBuffers video data for one frame
     * @param bufferFormat information about the format of the buffer used
     * @param displayWidth pixel width of the video, may be different from buffer width
     * @param displayHeight pixel height of the video, may be different from buffer height
     */
    void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat, int displayWidth, int displayHeight);

    /**
     * Unlock the video buffer after receiving a frame.
     * <p>
     * Implementations of this method must execute as quickly as possible.
     *
     * @param mediaPlayer media player to which the event relates
     */
    void unlock(MediaPlayer mediaPlayer);
}
