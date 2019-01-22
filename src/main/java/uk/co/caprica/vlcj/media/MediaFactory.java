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

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.callbackmedia.CallbackMedia;
import uk.co.caprica.vlcj.player.MediaResourceLocator;

public final class MediaFactory {

    public static Media newMedia(LibVlc libvlc, libvlc_media_t mediaInstance, String... options) {
        return createMedia(libvlc, mediaInstance, options);
    }

    public static Media newMedia(LibVlc libvlc, libvlc_instance_t libvlcInstance, String mrl, String... options) {
        mrl = MediaResourceLocator.encodeMrl(mrl);

        libvlc_media_t mediaInstance = MediaResourceLocator.isLocation(mrl) ?
                libvlc.libvlc_media_new_location(libvlcInstance, mrl) :
                libvlc.libvlc_media_new_path(libvlcInstance, mrl);

        return createMedia(libvlc, mediaInstance, options);
    }

    public static Media newMedia(LibVlc libvlc, libvlc_instance_t libvlcInstance, CallbackMedia callbackMedia, String... options) {
        libvlc_media_t mediaInstance = libvlc.libvlc_media_new_callbacks(libvlcInstance,
                callbackMedia.getOpen(),
                callbackMedia.getRead(),
                callbackMedia.getSeek(),
                callbackMedia.getClose(),
                callbackMedia.getOpaque()
        );

        return createMedia(libvlc, mediaInstance, options);
    }

    private static Media createMedia(LibVlc libvlc, libvlc_media_t mediaInstance, String... options) {
        if (mediaInstance != null) {
            Media media = new Media(libvlc, mediaInstance);
            media.options().addOptions(options);
            return media;
        } else {
            return null;
        }
    }

    private MediaFactory() {
    }

}
