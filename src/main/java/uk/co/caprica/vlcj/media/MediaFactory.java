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

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.media.callback.CallbackMedia;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_duplicate;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_new_callbacks;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_new_location;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_new_path;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_retain;

/**
 * Factory to create {@link Media} and {@link MediaRef} instances.
 * <p>
 * <em>This factory is <strong>not</strong> intended for use by client applications.</em>
 */
public final class MediaFactory {

    private MediaFactory() {
    }

    /**
     * Create a new {@link MediaRef} for a native media instance.
     * <p>
     * The client application <em>must</em> release the returned {@link MediaRef} when it no long has any use for it.
     *
     * @param libvlcInstance native library instance
     * @param mediaInstance native media instance
     * @param options options to add to the media
     * @return media reference
     */
    public static MediaRef newMediaRef(libvlc_instance_t libvlcInstance, libvlc_media_t mediaInstance, String... options) {
        MediaRef result = createMediaRef(libvlcInstance, mediaInstance, options);
        if (result != null) {
            libvlc_media_retain(mediaInstance);
        }
        return result;
    }

    /**
     * Create a new {@link MediaRef} for a media resource locator.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaRef} when it has no further use for it.
     *
     * @param libvlcInstance native library instance
     * @param mrl media resource locator
     * @param options options to add to the media
     * @return media reference
     */
    public static MediaRef newMediaRef(libvlc_instance_t libvlcInstance, String mrl, String... options) {
        return createMediaRef(libvlcInstance, newMediaInstance(libvlcInstance, mrl), options);
    }

    /**
     * Create a new {@link MediaRef} for callback media.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaRef} when it has no further use for it.
     *
     * @param libvlcInstance native library instance
     * @param callbackMedia callback media component
     * @param options options to add to the media
     * @return media reference
     */
    public static MediaRef newMediaRef(libvlc_instance_t libvlcInstance, CallbackMedia callbackMedia, String... options) {
        return createMediaRef(libvlcInstance, newMediaInstance(libvlcInstance, callbackMedia), options);
    }

    /**
     * Create a new {@link MediaRef} for a {@link Media}.
     * <p>
     * The caller <em>must</em> release the supplied {@link Media} when it has no further use for it.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaRef} when it has no further use for it.
     *
     * @param libvlcInstance native library instance
     * @param media media
     * @param options options to add to the media
     * @return media reference
     */
    public static MediaRef newMediaRef(libvlc_instance_t libvlcInstance, Media media, String... options) {
        return createMediaRef(libvlcInstance, retain(media.mediaInstance()), options);
    }

    /**
     * Create a new {@link MediaRef} for a {@link MediaRef}.
     * <p>
     * The caller <em>must</em> release the supplied {@link MediaRef} when it has no further use for it.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaRef} when it has no further use for it.
     *
     * @param libvlcInstance native library instance
     * @param mediaRef media reference
     * @param options options to add to the media
     * @return media reference
     */
    public static MediaRef newMediaRef(libvlc_instance_t libvlcInstance, MediaRef mediaRef, String... options) {
        return createMediaRef(libvlcInstance, retain(mediaRef.mediaInstance()), options);
    }

    /**
     * Create a duplicate {@link MediaRef} for a {@link MediaRef}.
     * <p>
     * Unlike the "newMediaRef" functions, this function will duplicate the native media instance, meaning it is
     * separate from the native media instance in this component and any changes made to it (such as adding new media
     * options) will <em>not</em> be reflected on the original media.
     * <p>
     * The caller <em>must</em> release the supplied {@link MediaRef} when it has no further use for it.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaRef} when it has no further use for it.
     *
     * @param libvlcInstance native library instance
     * @param mediaRef media reference
     * @param options options to add to the media
     * @return duplicated media reference
     */
    public static MediaRef duplicateMediaRef(libvlc_instance_t libvlcInstance, MediaRef mediaRef, String... options) {
        return createMediaRef(libvlcInstance, libvlc_media_duplicate(mediaRef.mediaInstance()), options);
    }

    /**
     * Create a new {@link Media} component for a native media instance.
     * <p>
     * The caller <em>must</em> release the returned {@link Media} when it has no further use for it.
     *
     * @param libvlcInstance native library instance
     * @param mediaInstance native media instance
     * @param options options to add to the media
     * @return media
     */
    public static Media newMedia(libvlc_instance_t libvlcInstance, libvlc_media_t mediaInstance, String... options) {
        Media result = createMedia(libvlcInstance, mediaInstance, options);
        if (result != null) {
            libvlc_media_retain(mediaInstance);
        }
        return result;
    }

    /**
     * Create a new {@link Media} component for a media resource locator.
     * <p>
     * The caller <em>must</em> release the returned {@link Media} when it has no further use for it.
     *
     * @param libvlcInstance native library instance
     * @param mrl media resource locator
     * @param options options to add to the media
     * @return media
     */
    public static Media newMedia(libvlc_instance_t libvlcInstance, String mrl, String... options) {
        return createMedia(libvlcInstance, newMediaInstance(libvlcInstance, mrl), options);
    }

    /**
     * Create a new {@link Media} component for callback media.
     * <p>
     * The caller <em>must</em> release the returned {@link Media} when it has no further use for it.
     *
     * @param libvlcInstance native library instance
     * @param callbackMedia callback media component
     * @param options options to add to the media
     * @return media
     */
    public static Media newMedia(libvlc_instance_t libvlcInstance, CallbackMedia callbackMedia, String... options) {
        return createMedia(libvlcInstance, newMediaInstance(libvlcInstance, callbackMedia), options);
    }

    /**
     * Create a new {@link Media} component for a {@link MediaRef}.
     * <p>
     * The caller <em>must</em> release the supplied {@link MediaRef} when it has no further use for it.
     * <p>
     * The caller <em>must</em> release the returned {@link Media} when it has no further use for it.
     *
     * @param libvlcInstance native library instance
     * @param mediaRef media reference
     * @param options options to add to the media
     * @return media
     */
    public static Media newMedia(libvlc_instance_t libvlcInstance, MediaRef mediaRef, String... options) {
        return createMedia(libvlcInstance, retain(mediaRef.mediaInstance()), options);
    }

    /**
     * Create a new {@link Media} component for a {@link Media}.
     * <p>
     * The caller <em>must</em> release the supplied {@link Media} when it has no further use for it.
     * <p>
     * The caller <em>must</em> release the returned {@link Media} when it has no further use for it.
     *
     * @param libvlcInstance native library instance
     * @param media media
     * @param options options to add to the media
     * @return media
     */
    public static Media newMedia(libvlc_instance_t libvlcInstance, Media media, String... options) {
        return createMedia(libvlcInstance, retain(media.mediaInstance()), options);
    }

    /**
     * Create a duplicate {@link Media} for a {@link Media}.
     * <p>
     * Unlike the "newMedia" functions, this function will duplicate the native media instance, meaning it is separate
     * from the native media instance in this component and any changes made to it (such as adding new media options)
     * will <em>not</em> be reflected on the original media.
     * <p>
     * The caller <em>must</em> release the supplied {@link Media} when it has no further use for it.
     * <p>
     * The caller <em>must</em> release the returned {@link Media} when it has no further use for it.
     *
     * @param libvlcInstance native library instance
     * @param media media
     * @param options options to add to the media
     * @return duplicated media
     */
    public static Media duplicateMedia(libvlc_instance_t libvlcInstance, Media media, String... options) {
        return createMedia(libvlcInstance, libvlc_media_duplicate(media.mediaInstance()), options);
    }

    private static libvlc_media_t newMediaInstance(libvlc_instance_t libvlcInstance, String mrl) {
        mrl = MediaResourceLocator.encodeMrl(mrl);
        return MediaResourceLocator.isLocation(mrl) ?
            libvlc_media_new_location(libvlcInstance, mrl) :
            libvlc_media_new_path(libvlcInstance, mrl);
    }

    private static libvlc_media_t newMediaInstance(libvlc_instance_t libvlcInstance, CallbackMedia callbackMedia) {
        return libvlc_media_new_callbacks(libvlcInstance,
            callbackMedia.getOpen(),
            callbackMedia.getRead(),
            callbackMedia.getSeek(),
            callbackMedia.getClose(),
            callbackMedia.getOpaque()
        );
    }

    private static libvlc_media_t retain(libvlc_media_t mediaInstance) {
        libvlc_media_retain(mediaInstance);
        return mediaInstance;
    }

    private static MediaRef createMediaRef(libvlc_instance_t libvlcInstance, libvlc_media_t mediaInstance, String[] options) {
        if (mediaInstance != null) {
            MediaOptions.addMediaOptions(mediaInstance, options);
            return new MediaRef(libvlcInstance, mediaInstance);
        } else {
            return null;
        }
    }

    private static Media createMedia(libvlc_instance_t libvlcInstance, libvlc_media_t mediaInstance, String... options) {
        if (mediaInstance != null) {
            Media media = new Media(libvlcInstance, mediaInstance);
            media.options().add(options);
            return media;
        } else {
            return null;
        }
    }

}
