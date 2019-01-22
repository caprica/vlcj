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

package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.player.embedded.videosurface.ComponentIdVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.ComponentVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.linux.LinuxVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.mac.MacVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.windows.WindowsVideoSurfaceAdapter;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.awt.*;

public final class VideoSurfaceService extends BaseService {

    VideoSurfaceService(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Create a new video surface for a Component.
     * <p>
     * The optimal component in a {@link Canvas}, {@link Window} can be used, as can any other AWT {@link Component}, at
     * least in principle.
     * </p>
     *
     * @param component component
     * @return video surface
     */
    public ComponentVideoSurface newVideoSurface(Component component) {
        // FIXME common adapter code
        VideoSurfaceAdapter videoSurfaceAdapter;
        if (RuntimeUtil.isNix()) {
            videoSurfaceAdapter = new LinuxVideoSurfaceAdapter();
        } else if(RuntimeUtil.isWindows()) {
            videoSurfaceAdapter = new WindowsVideoSurfaceAdapter();
        } else if(RuntimeUtil.isMac()) {
            videoSurfaceAdapter = new MacVideoSurfaceAdapter();
        } else {
            throw new RuntimeException("Unable to create a media player - failed to detect a supported operating system");
        }
        return new ComponentVideoSurface(component, videoSurfaceAdapter);
    }

    /**
     * Create a new video surface for a native component id.
     *
     * @param componentId native component id
     * @return video surface
     */
    public ComponentIdVideoSurface newVideoSurface(long componentId) {
        // FIXME common adapter code
        VideoSurfaceAdapter videoSurfaceAdapter;
        if(RuntimeUtil.isNix()) {
            videoSurfaceAdapter = new LinuxVideoSurfaceAdapter();
        }
        else if(RuntimeUtil.isWindows()) {
            videoSurfaceAdapter = new WindowsVideoSurfaceAdapter();
        }
        else if(RuntimeUtil.isMac()) {
            videoSurfaceAdapter = new MacVideoSurfaceAdapter();
        }
        else {
            throw new RuntimeException("Unable to create a media player - failed to detect a supported operating system");
        }
        ComponentIdVideoSurface videoSurface = new ComponentIdVideoSurface(componentId, videoSurfaceAdapter);
        return videoSurface;
    }

}
