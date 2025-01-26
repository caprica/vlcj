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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.videosurface;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.VideoSurfaceApi;

/**
 * Encapsulation of a video surface.
 */
public abstract class VideoSurface {

    /**
     * Operating System specific video surface adapter implementation.
     * <p>
     * May be <pre>null</pre>, e.g. for "callback" media players.
     */
    protected final VideoSurfaceAdapter videoSurfaceAdapter;

    /**
     * Create a new video surface wrapper.
     *
     * @param videoSurfaceAdapter video surface adapter implementation, may be <pre>null</pre>
     */
    protected VideoSurface(VideoSurfaceAdapter videoSurfaceAdapter) {
        this.videoSurfaceAdapter = videoSurfaceAdapter;
    }

    /**
     * Attach the video surface to a media player.
     * <p>
     * The video surface component <em>must</em> be visible at this point otherwise the native call will fail.
     * <p>
     * This method is not ordinarily intended to be called by applications, rather the media player will invoke this
     * method at the appropriate time after the calling application has set the video surface via
     * {@link EmbeddedMediaPlayer#videoSurface()} and {@link VideoSurfaceApi#set(VideoSurface)}
     * <p>
     * If an application does use this method, the media player will not keep a reference to this video surface, and it
     * may become prematurely eligible for garbage collection and cause subsequent failures. In such cases, the calling
     * application should make sure a hard reference to the video surface is kept.
     *
     * @param mediaPlayer media player instance
     */
    public abstract void attach(MediaPlayer mediaPlayer);

}
