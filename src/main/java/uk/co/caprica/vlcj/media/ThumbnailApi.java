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

package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_thumbnail_request_t;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_thumbnail_cancel;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_thumbnail_request_by_pos;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_thumbnail_request_by_time;

/**
 * Behaviour pertaining to media thumbnails.
 */
public final class ThumbnailApi extends BaseApi {

    ThumbnailApi(Media media) {
        super(media);
    }

    /**
     * Request a thumbnail be created for a particular time within the media.
     *
     * @param time time from media start, milliseconds
     * @param speed seek speed (fast, or precise)
     * @param width width for the thumbnail
     * @param height height for the thumbnail
     * @param pictureType picture format for the thumbnail
     * @param timeout timeout for thumbnail generation
     * @return thunbnail request
     */
    public ThumbnailRequest requestByTime(long time, ThumbnailerSeekSpeed speed, int width, int height, PictureType pictureType, long timeout) {
        libvlc_media_thumbnail_request_t request = libvlc_media_thumbnail_request_by_time(mediaInstance, time, speed.intValue(), width, height, pictureType.intValue(), timeout);
        if (request != null) {
            return new ThumbnailRequest(request);
        } else {
            return null;
        }
    }

    /**
     * Request a thumbnail be created for a particular position within the media.
     *
     * @param position position within the media, a percentage (for example, 0.2f is 20%)
     * @param speed seek speed (fast, or precise)
     * @param width width for the thumbnail
     * @param height height for the thumbnail
     * @param pictureType picture format for the thumbnail
     * @param timeout timeout for thumbnail generation
     * @return thunbnail request
     */
    public ThumbnailRequest requestByPosition(float position, ThumbnailerSeekSpeed speed, int width, int height, PictureType pictureType, long timeout) {
        libvlc_media_thumbnail_request_t request = libvlc_media_thumbnail_request_by_pos(mediaInstance, position, speed.intValue(), width, height, pictureType.intValue(), timeout);
        if (request != null) {
            return new ThumbnailRequest(request);
        } else {
            return null;
        }
    }

    /**
     * Cancel a thumbnail request.
     *
     * @param request request to cancel
     */
    public void cancel(ThumbnailRequest request) {
        libvlc_media_thumbnail_cancel(request.instance());
    }

}
