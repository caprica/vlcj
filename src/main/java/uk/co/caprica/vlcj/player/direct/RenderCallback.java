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
 * Specification for a component that wishes to be called back to process video frames.
 * <p>
 * The render callback provides access to the native memory buffer - this is the most
 * efficient use-case.
 * <p>
 * If instead the full RGB integer data is required for the full video frame then consider
 * using the default {@link RenderCallbackAdapter}.
 */
public interface RenderCallback {

    /**
     * callback invoked by the native library to prepare the next frame to do display.
     * <p>
     * Implementations should use this method to <em>prepare</em> the frame, <strong>not</strong>
     * to <em>render</em> it - see {@link #display(DirectMediaPlayer)}. 
     * 
     * @param mediaPlayer media player to which the event relates
     * @param nativeBuffers video data for one frame
     * @param bufferFormat information about the format of the buffer used
     */
    public void prepare(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat);
    
    /**
     * callback invoked by the native library when it is ready to display a video frame.
     * <p>
     * Implementations should use this method to do the minimum necessary to <em>render</em> a
     * pre-prepared video frame.
     * <p>
     * This method must execute as quickly as possible.
     * 
     * @param mediaPlayer media player to which the event relates
     */
    public void display(DirectMediaPlayer mediaPlayer);
}
