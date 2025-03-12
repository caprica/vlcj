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

import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;

import java.nio.ByteBuffer;

/**
 * Callback invoked by the {@link CallbackVideoSurface} when the format of the video changes.
 */
public interface BufferFormatCallback {

    /**
     * Returns a {@link BufferFormat} instance specifying how the {@link CallbackVideoSurface} should structure its
     * internal buffers.
     * <p>
     * Note that it is possible that some versions of VLC provide the wrong value for the sourceHeight parameter, and
     * more than that it might invoke this callback multiple times with different values for the sourceHeight. Your own
     * callback implementation may need to mitigate this (e.g. by ignoring the sourceHeight changes on subsequent
     * invocations of your callback).
     *
     * @param sourceWidth video buffer width
     * @param sourceHeight video buffer height
     * @return buffer format, must never be <code>null</code>
     */
    BufferFormat getBufferFormat(int sourceWidth, int sourceHeight);

    /**
     * Invoked when a new buffer format is set.
     * <p>
     * The sizes have not necessarily changed, but they are "new".
     * <p>
     * The buffer size and the display size are not necessarily the same. For example with 1080P video, it is common for
     * the video buffer height value to be 1090 with a display height value of 1080.
     * <p>
     * This will be invoked immediately prior to {@link #allocatedBuffers(ByteBuffer[])}.
     *
     * @param bufferWidth video buffer width
     * @param bufferHeight video buffer width
     * @param displayWidth video display width
     * @param displayHeight video display height
     */
    void newFormatSize(int bufferWidth, int bufferHeight, int displayWidth, int displayHeight);

    /**
     * Invoked when new video buffers have been allocated.
     * <p>
     * This will be invoked immediately after {@link #newFormatSize(int, int, int, int)}.
     *
     * @param buffers buffers that were allocated
     */
    void allocatedBuffers(ByteBuffer[] buffers);
}
