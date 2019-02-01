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
import uk.co.caprica.vlcj.enums.PictureType;
import uk.co.caprica.vlcj.enums.ThumbnailerSeekSpeed;

public class ThumbnailService extends BaseService {

    ThumbnailService(Media media) {
        super(media);
    }

    public ThumbnailRequest requestByTime(long time, ThumbnailerSeekSpeed speed, int width, int height, PictureType pictureType, long timeout) {
        libvlc_media_thumbnail_request_t request = libvlc.libvlc_media_thumbnail_request_by_time(mediaInstance, time, speed.intValue(), width, height, pictureType.intValue(), timeout);
        if (request != null) {
            return new ThumbnailRequest(request);
        } else {
            return null;
        }
    }

    public ThumbnailRequest requestByPosition(float position, ThumbnailerSeekSpeed speed, int width, int height, PictureType pictureType, long timeout) {
        libvlc_media_thumbnail_request_t request = libvlc.libvlc_media_thumbnail_request_by_pos(mediaInstance, position, speed.intValue(), width, height, pictureType.intValue(), timeout);
        if (request != null) {
            return new ThumbnailRequest(request);
        } else {
            return null;
        }
    }

    public void cancel(ThumbnailRequest request) {
        libvlc.libvlc_media_thumbnail_cancel(request.instance());
    }

}
