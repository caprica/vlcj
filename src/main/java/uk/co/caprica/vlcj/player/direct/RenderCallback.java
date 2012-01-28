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
 * The render call-back provides access to the native memory buffer, if instead the full RGB integer
 * data is required for the full video frame then consider using {@link RenderCallbackAdapter}.
 */
public interface RenderCallback {

    /**
     * Call-back when ready to display a video frame.
     * 
     * @param nativeBuffer video data for one frame
     */
    public void display(Memory nativeBuffer);
}
