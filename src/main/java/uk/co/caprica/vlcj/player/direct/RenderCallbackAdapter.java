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
 * Copyright 2009, 2010, 2011, 2012 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.direct;

import com.sun.jna.Memory;

/**
 * A render call-back adapter implementation that fills an array of RGB integer data for an entire
 * video frame.
 * <p>
 * The media player must be sending pixels in the RV32 format.
 * <p>
 * If you simply want access to the native memory buffer you should consider sub-classing
 * {@link RenderCallback} directly rather than using this class.
 */
public abstract class RenderCallbackAdapter implements RenderCallback {

    /**
     * Video data buffer.
     */
    private final int[] rgbBuffer;

    /**
     * Create a new render call-back.
     * 
     * @param rgbBuffer video data buffer
     */
    public RenderCallbackAdapter(int[] rgbBuffer) {
        this.rgbBuffer = rgbBuffer;
    }

    @Override
    public final void display(Memory nativeBuffer) {
        nativeBuffer.read(0, rgbBuffer, 0, rgbBuffer.length);
        onDisplay(rgbBuffer);
    }

    /**
     * Get the video data buffer.
     * 
     * @return video buffer
     */
    public int[] rgbBuffer() {
        return rgbBuffer;
    }

    /**
     * Template method invoked when a new frame of video data is ready.
     * 
     * @param rgbBuffer video data buffer
     */
    protected abstract void onDisplay(int[] rgbBuffer);
}
