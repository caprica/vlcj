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

package uk.co.caprica.vlcj.player.embedded;

import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;

/**
 * Behaviour pertaining to the video surface.
 */
public final class VideoSurfaceApi extends BaseApi {

    /**
     * Component to render the video to.
     */
    private VideoSurface videoSurface;

    VideoSurfaceApi(EmbeddedMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Set the component used to render video.
     * <p>
     * Setting the video surface on the native component is actually deferred so the component used
     * as the video surface need <em>not</em> be visible and fully realised before calling this
     * method.
     * <p>
     * The video surface will not be associated with the native media player until the media is
     * played.
     * <p>
     * It is possible to change the video surface after it has been set, but the change will not
     * take effect until the media is played.
     *
     * @param videoSurface component to render video to
     */
    public void set(VideoSurface videoSurface) {
        this.videoSurface = videoSurface;
    }

    /**
     * Ensure that the video surface has been associated with the native media player.
     * <p>
     * Ordinarily when setting the video surface the actual association of the video surface with
     * the native media player is deferred until the first time media is played.
     * <p>
     * This deferring behaviour is usually a good thing because when setting a video surface
     * component on the native media player the video surface component must be a displayable
     * component and this is often not the case during the construction and initialisation of the
     * application.
     * <p>
     * Most applications will not need to call this method.
     * <p>
     * However, in special circumstances such as associating an embedded media player with a media
     * list player, media is played through the media list rather than the media player itself so
     * the deferred association of the video surface would never happen.
     * <p>
     * Calling this method ensures that the video surface is properly associated with the native
     * media player and consequently the video surface component must be visible when this method is
     * called.
     */
    public void attachVideoSurface() {
        if (videoSurface != null) {
            videoSurface.attach(mediaPlayer);
        }
        else {
            // This is not necessarily an error
        }
    }

    VideoSurface getVideoSurface() {
        return videoSurface;
    }

    @Override
    protected void release() {
        this.videoSurface = null;
    }
}
