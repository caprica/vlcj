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
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.videosurface;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * Encapsulation of a video surface that wraps the native component id of the video surface
 * component.
 * <p>
 * This is required for example when using remote out-of-process media players but with video
 * rendering in a local application. In these scenarios, it is not possible to serialize the Canvas
 * component to the remote process to get the proper component ID (the copied Canvas component would
 * have a different native ID).
 * <p>
 * It is also not possible to get a native component ID if the component is not displayable.
 */
public class ComponentIdVideoSurface extends AWTVideoSurface {

    /**
     * Native component identifier for the video surface.
     */
    private final long componentId;

    /**
     * Create a new video surface.
     *
     * @param componentId native component identifier for the video surface
     * @param videoSurfaceAdapter adapter to attach a video surface to a native media player
     */
    public ComponentIdVideoSurface(long componentId, VideoSurfaceAdapter videoSurfaceAdapter) {
        super(videoSurfaceAdapter);
        this.componentId = componentId;
    }

    @Override
    public void attach(MediaPlayer mediaPlayer) {
        videoSurfaceAdapter.attach(mediaPlayer, componentId);
    }

}
