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

import uk.co.caprica.vlcj.callbackmedia.CallbackMedia;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaFactory;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListFactory;
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.media.MediaRef;

/**
 * Behaviour pertaining to creation of various types of media and media lists.
 */
public final class MediaService extends BaseService {

    MediaService(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     *
     *
     * @param mrl
     * @return
     */
    public MediaRef newMediaRef(String mrl, String... options) {
        return MediaFactory.newMediaRef(libvlc, libvlcInstance, mrl, options);
    }

    /**
     *
     *
     * <em>The calling application must make sure to keep hard references to the callback implementation objects to
     * prevent them from being garbage collected, otherwise a fatal JVM crash may occur.</em>
     *
     * @param callbackMedia
     * @return
     */
    public MediaRef newMediaRef(CallbackMedia callbackMedia, String... options) {
        return MediaFactory.newMediaRef(libvlc, libvlcInstance, callbackMedia, options);
    }

    /**
     *
     *
     * @param media
     * @param options
     * @return
     */
    public MediaRef newMediaRef(Media media, String... options) {
        return MediaFactory.newMediaRef(libvlc, libvlcInstance, media, options);
    }

    /**
     *
     *
     * @param mediaRef
     * @param options
     * @return
     */
    public MediaRef newMediaRef(MediaRef mediaRef, String... options) {
        return MediaFactory.newMediaRef(libvlc, libvlcInstance, mediaRef, options);
    }

    /**
     *
     *
     * @param mrl
     * @return
     */
    public Media newMedia(String mrl, String... options) {
        return MediaFactory.newMedia(libvlc, libvlcInstance, mrl, options);
    }

    /**
     *
     *
     * <em>The calling application must make sure to keep hard references to the callback implementation objects to
     * prevent them from being garbage collected, otherwise a fatal JVM crash may occur.</em>
     *
     * @param callbackMedia
     * @return
     */
    public Media newMedia(CallbackMedia callbackMedia, String... options) {
        return MediaFactory.newMedia(libvlc, libvlcInstance, callbackMedia, options);
    }

    /**
     *
     *
     * @param mediaRef
     * @param options
     * @return
     */
    public Media newMedia(MediaRef mediaRef, String... options) {
        return MediaFactory.newMedia(libvlc, libvlcInstance, mediaRef, options);
    }

    /**
     *
     *
     * @param media
     * @param options
     * @return
     */
    public Media newMedia(Media media, String... options) {
        return MediaFactory.newMedia(libvlc, libvlcInstance, media, options);
    }

    /**
     *
     *
     * @return
     */
    public MediaListRef newMediaListRef() {
        return MediaListFactory.newMediaListRef(libvlc, libvlcInstance);
    }

    /**
     * Create a new media list for a play-list media player.
     *
     * @return media list instance
     */
    public MediaList newMediaList() {
        return MediaListFactory.newMediaList(libvlc, libvlcInstance);
    }

}
