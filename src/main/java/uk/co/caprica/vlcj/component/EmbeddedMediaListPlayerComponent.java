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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.component;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;

/**
 * Encapsulation of an embedded media list player.
 * <p>
 * This component extends the {@link EmbeddedMediaPlayerComponent} to incorporate a
 * {@link MediaListPlayer} and an associated {@link MediaList}.
 */
@SuppressWarnings("serial")
public class EmbeddedMediaListPlayerComponent extends EmbeddedMediaPlayerComponent implements MediaListPlayerEventListener {

    /**
     * Media list player.
     */
    private final MediaListPlayer mediaListPlayer;

    /**
     * Media list.
     */
    private final MediaList mediaList;

    /**
     * Construct a media list player component.
     */
    public EmbeddedMediaListPlayerComponent() {
        // Create the native resources
        MediaPlayerFactory mediaPlayerFactory = getMediaPlayerFactory();
        mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();
        mediaList = mediaPlayerFactory.newMediaList();
        mediaListPlayer.setMediaList(mediaList);
        mediaListPlayer.setMediaPlayer(getMediaPlayer());
        // Register listeners
        mediaListPlayer.addMediaListPlayerEventListener(this);
        // Sub-class initialisation
        onAfterConstruct();
    }

    /**
     * Get the embedded media list player reference.
     * <p>
     * An application uses this handle to control the media player, add listeners and so on.
     *
     * @return media list player
     */
    public final MediaListPlayer getMediaListPlayer() {
        return mediaListPlayer;
    }

    /**
     * Get the embedded media list reference.
     *
     * @return media list
     */
    public final MediaList getMediaList() {
        return mediaList;
    }

    @Override
    protected final void onBeforeRelease() {
        onBeforeReleaseComponent();
        mediaListPlayer.release();
        mediaList.release();
    }

    /**
     * Template method invoked before the media list player is released.
     */
    protected void onBeforeReleaseComponent() {
    }

    // === MediaListPlayerEventListener =========================================

    @Override
    public void played(MediaListPlayer mediaListPlayer) {
    }

    @Override
    public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl) {
    }

    @Override
    public void stopped(MediaListPlayer mediaListPlayer) {
    }

    @Override
    public void mediaMetaChanged(MediaListPlayer mediaListPlayer, int metaType) {
    }

    @Override
    public void mediaSubItemAdded(MediaListPlayer mediaListPlayer, libvlc_media_t subItem) {
    }

    @Override
    public void mediaDurationChanged(MediaListPlayer mediaListPlayer, long newDuration) {
    }

    @Override
    public void mediaParsedChanged(MediaListPlayer mediaListPlayer, int newStatus) {
    }

    @Override
    public void mediaFreed(MediaListPlayer mediaListPlayer) {
    }

    @Override
    public void mediaStateChanged(MediaListPlayer mediaListPlayer, int newState) {
    }
}
