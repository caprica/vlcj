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
import uk.co.caprica.vlcj.model.MediaRef;

// FIXME when doing the Javadoc pass, take note of duplicate vs retain, and using the Media#newMediaRef etc

/**
 * This factory is not intended for use by client applications.
 */
public final class MediaFactory {

    /**
     *
     *
     * @param libvlc
     * @param mediaInstance
     * @param options
     * @return
     */
    public static MediaRef newMediaRef(LibVlc libvlc, libvlc_media_t mediaInstance, String... options) {
        MediaRef result = createMediaRef(libvlc, mediaInstance, options);
        if (result != null) {
            libvlc.libvlc_media_retain(mediaInstance);
        }
        return result;
    }

    /**
     *
     *
     * @param libvlc
     * @param libvlcInstance
     * @param mrl
     * @param options
     * @return
     */
    public static MediaRef newMediaRef(LibVlc libvlc, libvlc_instance_t libvlcInstance, String mrl, String... options) {
        return createMediaRef(libvlc, newMediaInstance(libvlc, libvlcInstance, mrl), options);
    }

    /**
     *
     *
     * @param libvlc
     * @param libvlcInstance
     * @param callbackMedia
     * @param options
     * @return
     */
    public static MediaRef newMediaRef(LibVlc libvlc, libvlc_instance_t libvlcInstance, CallbackMedia callbackMedia, String... options) {
        return createMediaRef(libvlc, newMediaInstance(libvlc, libvlcInstance, callbackMedia), options);
    }

    /**
     *
     *
     * @param libvlc
     * @param media
     * @param options
     * @return
     */
    public static MediaRef newMediaRef(LibVlc libvlc, Media media, String... options) {
        return createMediaRef(libvlc, libvlc.libvlc_media_duplicate(media.mediaInstance()), options);
    }

    /**
     *
     *
     * @param libvlc
     * @param mediaRef
     * @param options
     * @return
     */
    public static MediaRef newMediaRef(LibVlc libvlc, MediaRef mediaRef, String... options) {
        return createMediaRef(libvlc, libvlc.libvlc_media_duplicate(mediaRef.mediaInstance()), options);
    }

    /**
     *
     *
     * @param libvlc
     * @param mediaInstance
     * @param options
     * @return
     */
    public static Media newMedia(LibVlc libvlc, libvlc_media_t mediaInstance, String... options) {
        Media result = createMedia(libvlc, mediaInstance, options);
        if (result != null) {
            libvlc.libvlc_media_retain(mediaInstance);
        }
        return result;
    }

    /**
     *
     *
     * @param libvlc
     * @param libvlcInstance
     * @param mrl
     * @param options
     * @return
     */
    public static Media newMedia(LibVlc libvlc, libvlc_instance_t libvlcInstance, String mrl, String... options) {
        return createMedia(libvlc, newMediaInstance(libvlc, libvlcInstance, mrl), options);
    }

    /**
     *
     *
     * @param libvlc
     * @param libvlcInstance
     * @param callbackMedia
     * @param options
     * @return
     */
    public static Media newMedia(LibVlc libvlc, libvlc_instance_t libvlcInstance, CallbackMedia callbackMedia, String... options) {
        return createMedia(libvlc, newMediaInstance(libvlc, libvlcInstance, callbackMedia), options);
    }

    /**
     * Create a new {@link Media} component from a {@link MediaRef}.
     * <p>
     * Internally, the supplied native media instance will be duplicated and any supplied <code>options</code> will be
     * applied only to this duplicate.
     * <p>
     * To create a new component that instead shares the native instance, use {@link MediaRef#newMedia()}.
     * <p>
     * The caller <em>must</em> release the supplied {@link MediaRef} when it has no further use for it.
     * <p>
     * The caller <em>must</em> also release the returned {@link Media} when it has no further use for it.
     *
     * @param libvlc
     * @param mediaRef
     * @param options
     * @return
     */
    public static Media newMedia(LibVlc libvlc, MediaRef mediaRef, String... options) {
        return createMedia(libvlc, libvlc.libvlc_media_duplicate(mediaRef.mediaInstance()), options);
    }

    /**
     *
     *
     * @param libvlc
     * @param media
     * @param options
     * @return
     */
    public static Media newMedia(LibVlc libvlc, Media media, String... options) {
        return createMedia(libvlc, libvlc.libvlc_media_duplicate(media.mediaInstance()), options);
    }

    private static libvlc_media_t newMediaInstance(LibVlc libvlc, libvlc_instance_t libvlcInstance, String mrl) {
        mrl = MediaResourceLocator.encodeMrl(mrl);
        return MediaResourceLocator.isLocation(mrl) ?
            libvlc.libvlc_media_new_location(libvlcInstance, mrl) :
            libvlc.libvlc_media_new_path(libvlcInstance, mrl);
    }

    private static libvlc_media_t newMediaInstance(LibVlc libvlc, libvlc_instance_t libvlcInstance, CallbackMedia callbackMedia) {
        return libvlc.libvlc_media_new_callbacks(libvlcInstance,
            callbackMedia.getOpen(),
            callbackMedia.getRead(),
            callbackMedia.getSeek(),
            callbackMedia.getClose(),
            callbackMedia.getOpaque()
        );
    }

    private static MediaRef createMediaRef(LibVlc libvlc, libvlc_media_t mediaInstance, String[] options) {
        if (mediaInstance != null) {
            MediaOptions.addMediaOptions(libvlc, mediaInstance, options);
            return new MediaRef(libvlc, mediaInstance);
        } else {
            return null;
        }
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
