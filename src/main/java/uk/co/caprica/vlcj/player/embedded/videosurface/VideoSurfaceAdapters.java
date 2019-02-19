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

package uk.co.caprica.vlcj.player.embedded.videosurface;

import uk.co.caprica.vlcj.binding.RuntimeUtil;

/**
 * Utility class to create a {@link VideoSurfaceAdapter} for the current run-time operating system.
 */
public final class VideoSurfaceAdapters {

    private VideoSurfaceAdapters() {
    }

    /**
     * Get a video surface adapter for the current run-time operating system.
     *
     * @return video surface adapter
     */
    public static VideoSurfaceAdapter getVideoSurfaceAdapter() {
        if (RuntimeUtil.isNix()) {
            return new LinuxVideoSurfaceAdapter();
        } else if (RuntimeUtil.isWindows()) {
            return new WindowsVideoSurfaceAdapter();
        } else if (RuntimeUtil.isMac()) {
            return new OsxVideoSurfaceAdapter();
        } else {
            throw new RuntimeException("Unable to create a video surface - failed to detect a supported operating system");
        }
    }

}
