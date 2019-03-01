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

package uk.co.caprica.vlcj.player.embedded.videosurface.callback;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.nio.ByteBuffer;

/**
 * A render callback adapter implementation that fills an array of integer data for an entire video frame.
 * <p>
 * If you simply want access to the native memory buffer you should consider implementing {@link RenderCallback}
 * directly rather than using this class.
 * <p>
 * This is probably not the most efficient implementation possible of a render callback, ideally the native video data
 * would be written directly to some other construct (like a texture).
 * <p>
 * Having said that, the supplied buffer could be a buffer direct from an image raster, in which case it should be
 * quite quick.
 */
public abstract class RenderCallbackAdapter implements RenderCallback {

    /**
     * Video data buffer.
     */
    private int[] buffer;

    /**
     * Create a new render callback.
     * <p>
     * The caller must ensure the supplied data buffer is large enough to hold the video frame data.
     *
     * @param buffer video data buffer
     */
    public RenderCallbackAdapter(int[] buffer) {
        this.buffer = buffer;
    }

    /**
     * Create a new render callback.
     * <p>
     * The video frame buffer <em>must</em> subsequently by set via {@link #setBuffer(int[])}.
     */
    public RenderCallbackAdapter() {
    }

    /**
     * Set the buffer to use for the video frame data.
     * <p>
     * The caller must ensure the supplied data buffer is large enough to hold the video frame data.
     *
     * @param buffer buffer
     */
    public final void setBuffer(int[] buffer) {
        this.buffer = buffer;
    }

    @Override
    public final void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
        nativeBuffers[0].asIntBuffer().get(buffer, 0, bufferFormat.getHeight() * bufferFormat.getWidth());
        onDisplay(mediaPlayer, buffer);
    }

    /**
     * Template method invoked when a new frame of video data is ready.
     *
     * @param mediaPlayer media player
     * @param buffer video data buffer
     */
    protected abstract void onDisplay(MediaPlayer mediaPlayer, int[] buffer);

}
