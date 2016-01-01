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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.videosurface;

import java.awt.Canvas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayer;

import com.sun.jna.Native;

/**
 * Encapsulation of a video surface that uses a Canvas.
 */
public class CanvasVideoSurface extends VideoSurface {

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(CanvasVideoSurface.class);

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Video surface component.
     */
    private final Canvas canvas;

    /**
     * Create a new video surface.
     *
     * @param canvas video surface component
     * @param videoSurfaceAdapter adapter to attach a video surface to a native media player
     */
    public CanvasVideoSurface(Canvas canvas, VideoSurfaceAdapter videoSurfaceAdapter) {
        super(videoSurfaceAdapter);
        this.canvas = canvas;
    }

    /**
     * Get the canvas.
     *
     * @return canvas
     */
    public final Canvas canvas() {
        return canvas;
    }

    @Override
    public void attach(LibVlc libvlc, MediaPlayer mediaPlayer) {
        logger.debug("attach()");
        if(canvas.isDisplayable()) {
            long componentId = Native.getComponentID(canvas);
            logger.debug("componentId={}", componentId);
            videoSurfaceAdapter.attach(libvlc, mediaPlayer, componentId);
            logger.debug("video surface attached");
        }
        else {
            throw new IllegalStateException("The video surface component must be displayable");
        }
    }
}
