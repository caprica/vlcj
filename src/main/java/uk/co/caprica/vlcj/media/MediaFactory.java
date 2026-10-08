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

import uk.co.caprica.vlcj.binding.internal.libvlc_media_open_cbs;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.media.callback.CallbackMedia;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_duplicate;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_new_callbacks;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_new_location;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_new_path;

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
     * The client application <em>must</em> release the new {@link MediaRef} when it no long has any use for it.
     *
     * @param mediaInstance native media instance
     * @param options options to add to the media
     * @return media reference
     */
    public static MediaRef newMediaRef(libvlc_media_t mediaInstance, String... options) {
        return createMediaRef(mediaInstance, options);
    }

    /**
     * Create a new {@link MediaRef} for a media resource locator.
     * <p>
     * The caller <em>must</em> release the new {@link MediaRef} when it has no further use for it.
     *
     * @param mrl media resource locator
     * @param options options to add to the media
     * @return media reference
     */
    public static MediaRef newMediaRef(String mrl, String... options) {
        return createMediaRef(newMediaInstance(mrl), options);
    }

    /**
     * Create a new {@link MediaRef} for callback media.
     * <p>
     * The caller <em>must</em> release the new {@link MediaRef} when it has no further use for it.
     *
     * @param callbackMedia callback media component
     * @param options options to add to the media
     * @return media reference
     */
    public static MediaRef newMediaRef(CallbackMedia callbackMedia, String... options) {
        return createMediaRef(newMediaInstance(callbackMedia), options);
    }

    /**
     * Create a new {@link MediaRef} for a {@link Media}.
     * <p>
     * The caller <em>must</em> release the new {@link MediaRef} when it has no further use for it.
     *
     * @param media media
     * @param options options to add to the media
     * @return media reference
     */
    public static MediaRef newMediaRef(Media media, String... options) {
        return createMediaRef(media.mediaInstance(), options);
    }

    /**
     * Create a new {@link MediaRef} for a {@link MediaRef}.
     * <p>
     * The caller <em>must</em> release the new {@link MediaRef} when it has no further use for it.
     *
     * @param mediaRef media reference
     * @param options options to add to the media
     * @return media reference
     */
    public static MediaRef newMediaRef(MediaRef mediaRef, String... options) {
        return createMediaRef(mediaRef.mediaInstance(), options);
    }

    /**
     * Create a duplicate {@link MediaRef} for a {@link MediaRef}.
     * <p>
     * Unlike the "newMediaRef" functions, this function will duplicate the native media instance, meaning it is
     * separate from the native media instance in this component and any changes made to it (such as adding new media
     * options) will <em>not</em> be reflected on the original media.
     * <p>
     * The caller <em>must</em> release the duplicated {@link MediaRef} when it has no further use for it.
     *
     * @param mediaRef media reference
     * @param options options to add to the media
     * @return duplicated media reference
     */
    public static MediaRef duplicateMediaRef(MediaRef mediaRef, String... options) {
        return createMediaRef(libvlc_media_duplicate(mediaRef.mediaInstance()), options);
    }

    /**
     * Create a new {@link Media} component for a native media instance.
     * <p>
     * The caller <em>must</em> release the new {@link Media} when it has no further use for it.
     *
     * @param mediaInstance native media instance
     * @param options options to add to the media
     * @return media
     */
    public static Media newMedia(libvlc_media_t mediaInstance, String... options) {
        return createMedia(mediaInstance, options);
    }

    /**
     * Create a new {@link Media} component for a media resource locator.
     * <p>
     * The caller <em>must</em> release the new {@link Media} when it has no further use for it.
     *
     * @param mrl media resource locator
     * @param options options to add to the media
     * @return media
     */
    public static Media newMedia(String mrl, String... options) {
        return createMedia(newMediaInstance(mrl), options);
    }

    /**
     * Create a new {@link Media} component for callback media.
     * <p>
     * The caller <em>must</em> release the new {@link Media} when it has no further use for it.
     *
     * @param callbackMedia callback media component
     * @param options options to add to the media
     * @return media
     */
    public static Media newMedia(CallbackMedia callbackMedia, String... options) {
        return createMedia(newMediaInstance(callbackMedia), options);
    }

    /**
     * Create a new {@link Media} component for a {@link MediaRef}.
     * <p>
     * The caller <em>must</em> release the new {@link Media} when it has no further use for it.
     *
     * @param mediaRef media reference
     * @param options options to add to the media
     * @return media
     */
    public static Media newMedia(MediaRef mediaRef, String... options) {
        return createMedia(mediaRef.mediaInstance(), options);
    }

    /**
     * Create a new {@link Media} component for a {@link Media}.
     * <p>
     * The caller <em>must</em> release the new {@link Media} when it has no further use for it.
     *
     * @param media media
     * @param options options to add to the media
     * @return media
     */
    public static Media newMedia(Media media, String... options) {
        return createMedia(media.mediaInstance(), options);
    }

    /**
     * Create a duplicate {@link Media} for a {@link Media}.
     * <p>
     * Unlike the "newMedia" functions, this function will duplicate the native media instance, meaning it is separate
     * from the native media instance in this component and any changes made to it (such as adding new media options)
     * will <em>not</em> be reflected on the original media.
     * <p>
     * The caller <em>must</em> release the duplicated {@link Media} when it has no further use for it.
     *
     * @param media media
     * @param options options to add to the media
     * @return duplicated media
     */
    public static Media duplicateMedia(Media media, String... options) {
        return createMedia(libvlc_media_duplicate(media.mediaInstance()), options);
    }

    private static libvlc_media_t newMediaInstance(String mrl) {
        return MediaResourceLocator.isLocation(mrl) ?
            libvlc_media_new_location(mrl) :
            libvlc_media_new_path(mrl);
    }

    private static libvlc_media_t newMediaInstance(CallbackMedia callbackMedia) {
        libvlc_media_open_cbs openCbs = new libvlc_media_open_cbs();
        openCbs.open = callbackMedia.getOpen();
        openCbs.read = callbackMedia.getRead();
        openCbs.seek = callbackMedia.getSeek();
        openCbs.close = callbackMedia.getClose();
        openCbs.write();
        return libvlc_media_new_callbacks(openCbs, callbackMedia.getOpaque());
    }

    private static MediaRef createMediaRef(libvlc_media_t mediaInstance, String[] options) {
        MediaOptions.addMediaOptions(mediaInstance, options);
        return new MediaRef(mediaInstance);
    }

    private static Media createMedia(libvlc_media_t mediaInstance, String... options) {
        Media media = new Media(mediaInstance);
        media.options().add(options);
        return media;
    }
}
