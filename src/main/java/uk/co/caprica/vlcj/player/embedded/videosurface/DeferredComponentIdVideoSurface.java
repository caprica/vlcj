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

import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * Encapsulation of a video surface that wraps the native component id of the video surface component - the component id
 * is obtained when needed via a template method.
 * <p>
 * This is required for example when using remote out-of-process media players but with video rendering in a local
 * application. In these scenarios, it is not possible to serialize the Canvas component to the remote process to get
 * the proper component ID (the copied Canvas component would have a different native ID).
 * <p>
 * It is also not possible to get a native component ID if the component is not displayable.
 */
public abstract class DeferredComponentIdVideoSurface extends VideoSurface {

    /**
     * Create a new video surface.
     *
     * @param videoSurfaceAdapter adapter to attach a video surface to a native media player
     */
    public DeferredComponentIdVideoSurface(VideoSurfaceAdapter videoSurfaceAdapter) {
        super(videoSurfaceAdapter);
    }

    @Override
    public void attach(MediaPlayer mediaPlayer) {
        videoSurfaceAdapter.attach(mediaPlayer, getComponentId());
    }

    /**
     * Get the native component id to use for the video surface.
     *
     * @return component id
     */
    protected abstract long getComponentId();

}
