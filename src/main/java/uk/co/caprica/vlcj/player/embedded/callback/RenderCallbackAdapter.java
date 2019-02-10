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

package uk.co.caprica.vlcj.player.embedded.callback;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.nio.ByteBuffer;

/**
 * A render call-back adapter implementation that fills an array of RGB integer data for an entire
 * video frame.
 * <p>
 * The media player must be sending pixels in the RV32 format.
 * <p>
 * If you simply want access to the native memory buffer you should consider implementing
 * {@link RenderCallback} directly rather than using this class.
 * <p>
 * This is probably the most <em>inefficient</em> implementation possible of a render callback,
 * ordinarily the video data should be written directly to some other construct (like a texture).
 * <p>
 * Having said that, the supplied rgbBuffer could be a buffer direct from an image raster, in which case it should be
 * quite quick.
 */
public abstract class RenderCallbackAdapter implements RenderCallback {

    /**
     * Video data buffer.
     */
    private int[] rgbBuffer;

    /**
     * Create a new render call-back.
     * <p>
     * The caller must ensure the supplied data buffer is large enough to hold the video frame data.
     *
     * @param rgbBuffer video data buffer
     */
    public RenderCallbackAdapter(int[] rgbBuffer) {
        this.rgbBuffer = rgbBuffer;
    }

    public RenderCallbackAdapter() {
    }

    public void setBuffer(int[] rgbBuffer) {
        System.out.println("SET NEW BUFFER, SIZE IS " + rgbBuffer.length);
        this.rgbBuffer = rgbBuffer;
    }

    @Override
    public final void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
        nativeBuffers[0].asIntBuffer().get(rgbBuffer, 0, bufferFormat.getHeight() * bufferFormat.getWidth());
        onDisplay(mediaPlayer, rgbBuffer);
    }

    /**
     * Template method invoked when a new frame of video data is ready.
     *
     * @param mediaPlayer media player
     * @param rgbBuffer video data buffer
     */
    protected abstract void onDisplay(MediaPlayer mediaPlayer, int[] rgbBuffer);

}
