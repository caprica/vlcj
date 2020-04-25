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

import com.sun.jna.Native;
import uk.co.caprica.vlcj.binding.OsxComponentId;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.awt.*;

/**
 * Encapsulation of a video surface that uses an AWT Component (optimally a Canvas, or maybe a Window).
 */
public class ComponentVideoSurface extends AWTVideoSurface {

    /**
     * Video surface component.
     */
    private final Component component;

    /**
     * Create a new video surface.
     *
     * @param component video surface component
     * @param videoSurfaceAdapter adapter to attach a video surface to a native media player
     */
    public ComponentVideoSurface(Component component, VideoSurfaceAdapter videoSurfaceAdapter) {
        super(videoSurfaceAdapter);
        this.component = component;
    }

    /**
     * Get the canvas.
     *
     * @return canvas
     */
    public final Component component() {
        return component;
    }

    @Override
    public void attach(MediaPlayer mediaPlayer) {
        if(component.isDisplayable()) {
            long componentId = getComponentId(component);
            videoSurfaceAdapter.attach(mediaPlayer, componentId);
        }
        else {
            throw new IllegalStateException("The video surface component must be displayable");
        }
    }

    private long getComponentId(Component component) {
        if (!RuntimeUtil.isMac()) {
            return Native.getComponentID(component);
        } else {
            return OsxComponentId.getOsxComponentId(component);
        }
    }

}
