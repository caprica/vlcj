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

package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_thumbnail_request_t;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_thumbnail_request_by_pos;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_thumbnail_request_by_time;

/**
 * Behaviour pertaining to media thumbnails.
 */
public final class ThumbnailApi extends BaseApi {

    ThumbnailApi(Media media) {
        super(media);
    }

    /**
     * Request a thumbnail be created for a particular time within the media.
     * <p>
     * The returned {@link ThumbnailRequest} must be released via {@link ThumbnailRequest#release()} when it is no
     * longer needed.
     *
     * @param time time from media start, milliseconds
     * @param speed seek speed (fast, or precise)
     * @param width width for the thumbnail
     * @param height height for the thumbnail
     * @param crop <code>true</code> if the thumbnail should be cropped; <code>false</code> if not
     * @param pictureType picture format for the thumbnail
     * @param timeout timeout for thumbnail generation
     * @return thumbnail request
     */
    public ThumbnailRequest requestByTime(long time, ThumbnailerSeekSpeed speed, int width, int height, boolean crop, PictureType pictureType, long timeout) {
        libvlc_media_thumbnail_request_t request = libvlc_media_thumbnail_request_by_time(libvlcInstance, mediaInstance, time, speed.intValue(), width, height, crop ? 1 : 0, pictureType.intValue(), timeout);
        if (request != null) {
            return new ThumbnailRequest(request);
        } else {
            return null;
        }
    }

    /**
     * Request a thumbnail be created for a particular position within the media.
     * <p>
     * The returned {@link ThumbnailRequest} must be released via {@link ThumbnailRequest#release()} when it is no
     * longer needed.
     *
     * @param position position within the media, a percentage (for example, 0.2f is 20%)
     * @param speed seek speed (fast, or precise)
     * @param width width for the thumbnail
     * @param height height for the thumbnail
     * @param crop <code>true</code> if the thumbnail should be cropped; <code>false</code> if not
     * @param pictureType picture format for the thumbnail
     * @param timeout timeout for thumbnail generation
     * @return thumbnail request
     */
    public ThumbnailRequest requestByPosition(double position, ThumbnailerSeekSpeed speed, int width, int height, boolean crop, PictureType pictureType, long timeout) {
        libvlc_media_thumbnail_request_t request = libvlc_media_thumbnail_request_by_pos(libvlcInstance, mediaInstance, position, speed.intValue(), width, height, crop ? 1 : 0, pictureType.intValue(), timeout);
        if (request != null) {
            return new ThumbnailRequest(request);
        } else {
            return null;
        }
    }

}
