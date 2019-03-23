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

package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaFactory;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.callback.CallbackMedia;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListFactory;
import uk.co.caprica.vlcj.medialist.MediaListRef;

/**
 * Behaviour pertaining to creation of various types of media and media lists.
 */
public final class MediaApi extends BaseApi {

    MediaApi(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Create a new {@link MediaRef} for a media resource locator.
     * <p>
     * The returned media reference <em>must</em> be freed by the caller when it has no further use for it.
     *
     * @param mrl media resource locator
     * @param options options to add to the media
     * @return media reference, or <code>null</code> on error
     */
    public MediaRef newMediaRef(String mrl, String... options) {
        return MediaFactory.newMediaRef(libvlcInstance, mrl, options);
    }

    /**
     * Create a new {@link MediaRef} for callback media.
     * <p>
     * The returned media reference <em>must</em> be freed by the caller when it has no further use for it.
     * <p>
     * <em>The calling application must make sure to keep a hard references to the callback component to prevent it from
     * being garbage collected, otherwise a fatal JVM crash may occur.</em>
     *
     * @param callbackMedia callback media component
     * @param options options to add to the media
     * @return media reference, or <code>null</code> on error
     */
    public MediaRef newMediaRef(CallbackMedia callbackMedia, String... options) {
        return MediaFactory.newMediaRef(libvlcInstance, callbackMedia, options);
    }

    /**
     * Create a new {@link MediaRef} for an existing {@link Media}.
     * <p>
     * The returned media reference <em>must</em> be freed by the caller when it has no further use for it.
     * <p>
     * Alternatively {@link Media#newMediaRef()} could be used.
     *
     * @param media
     * @param options options to add to the media
     * @return media reference, or <code>null</code> on error
     */
    public MediaRef newMediaRef(Media media, String... options) {
        return MediaFactory.newMediaRef(libvlcInstance, media, options);
    }

    /**
     * Create a new {@link MediaRef} for an existing {@link MediaRef}.
     * <p>
     * The returned media reference <em>must</em> be freed by the caller when it has no further use for it.
     * <p>
     * Alternatively {@link MediaRef#newMediaRef()} could be used.
     *
     * @param mediaRef
     * @param options options to add to the media
     * @return media reference, or <code>null</code> on error
     */
    public MediaRef newMediaRef(MediaRef mediaRef, String... options) {
        return MediaFactory.newMediaRef(libvlcInstance, mediaRef, options);
    }

    /**
     * Create a new {@link Media} for a media resource locator.
     * <p>
     * The returned media <em>must</em> be freed by the caller when it has no further use for it.
     *
     * @param mrl media resource locator
     * @param options options to add to the media
     * @return media, or <code>null</code> on error
     */
    public Media newMedia(String mrl, String... options) {
        return MediaFactory.newMedia(libvlcInstance, mrl, options);
    }

    /**
     * Create a new {@link Media} for callback media.
     * <p>
     * The returned media <em>must</em> be freed by the caller when it has no further use for it.
     * <p>
     * <em>The calling application must make sure to keep a hard references to the callback component to prevent it from
     * being garbage collected, otherwise a fatal JVM crash may occur.</em>
     *
     * @param callbackMedia callback media component
     * @param options options to add to the media
     * @return media, or <code>null</code> on error
     */
    public Media newMedia(CallbackMedia callbackMedia, String... options) {
        return MediaFactory.newMedia(libvlcInstance, callbackMedia, options);
    }

    /**
     * Create a new {@link Media} for an existing {@link MediaRef}.
     * <p>
     * Alternatively {@link MediaRef#newMedia()} could be used.
     * <p>
     * The returned media <em>must</em> be freed by the caller when it has no further use for it.
     *
     * @param mediaRef media reference
     * @param options options to add to the media
     * @return media, or <code>null</code> on error
     */
    public Media newMedia(MediaRef mediaRef, String... options) {
        return MediaFactory.newMedia(libvlcInstance, mediaRef, options);
    }

    /**
     * Create a new {@link Media} for an existing {@link Media}.
     * <p>
     * Alternatively {@link Media#newMedia()} could be used.
     * <p>
     * The returned media <em>must</em> be freed by the caller when it has no further use for it.
     *
     * @param media media
     * @param options options to add to the media
     * @return media, or <code>null</code> on error
     */
    public Media newMedia(Media media, String... options) {
        return MediaFactory.newMedia(libvlcInstance, media, options);
    }

    /**
     * Create a new {@link MediaListRef}.
     *
     * @return media list reference, or <code>null</code> on error
     */
    public MediaListRef newMediaListRef() {
        return MediaListFactory.newMediaListRef(libvlcInstance);
    }

    /**
     * Create a new media list.
     *
     * @return media list, or <code>null</code> on error
     */
    public MediaList newMediaList() {
        return MediaListFactory.newMediaList(libvlcInstance);
    }

}
