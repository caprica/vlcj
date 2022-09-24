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
 * Copyright 2009-2020 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.videosurface;

import com.sun.jna.Native;
import uk.co.caprica.vlcj.binding.lib.LibX11;
import uk.co.caprica.vlcj.binding.support.runtime.RuntimeUtil;

import java.awt.*;

/**
 * Base implementation for a video surface that requires AWT.
 * <p>
 * This base class is used to perform the necessary bespoke initialisations for any video surface that uses an AWT
 * component (most likely a Canvas).
 * <p>
 * Presently the only bespoke initialisations are for Linux, specifically:
 * <ol>
 *   <li>
 *       force the the libjawt shared object - without doing this, the JNA {@link Native#getComponentID(Component)} call
 *       will likely fail with an {@link UnsatisfiedLinkError};
 *   </li>
 *   <li>
 *       invoke XInitThreads - without doing this VLC will complain about XLib not being properly initialised for
 *       threads, and some VLC plugins will fail to load, likely resulting in sub-optimal video playback or some VLC
 *       plugin functionality not being available.
 *   </li>
 * </ol>
 * This bespoke initialisation is localised to this component since it is not required, and can cause fatal JVM issues,
 * with a JavaFX application where AWT is not used.
 */
abstract class AWTVideoSurface extends VideoSurface {

    /**
     * One-time initialisation.
     */
    static {
        if (RuntimeUtil.isNix()) {
            LibX11.INSTANCE.XInitThreads();
            System.loadLibrary("jawt");
        }
    }

    /**
     * Create a new video surface wrapper.
     *
     * @param videoSurfaceAdapter video surface adapter implementation
     */
    protected AWTVideoSurface(VideoSurfaceAdapter videoSurfaceAdapter) {
        super(videoSurfaceAdapter);
    }
}
