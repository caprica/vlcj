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

package uk.co.caprica.vlcj.player.component;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

import java.awt.*;

/**
 * Implementation of an embedded media list player.
 * <p>
 * The component may be added directly to a user interface layout.
 * <p>
 * When the component is no longer needed, it should be released by invoking the {@link #release()} method.
 */
@SuppressWarnings("serial")
public class EmbeddedMediaListPlayerComponent extends EmbeddedMediaListPlayerComponentBase {

    /**
     * Media list player.
     */
    private MediaListPlayer mediaListPlayer;

    /**
     * Media list.
     */
    private MediaList mediaList;

    /**
     * Construct an embedded media list player component.
     * <p>
     * Any constructor parameter may be <code>null</code>, in which case a reasonable default will be used.
     *
     * @param mediaPlayerFactory media player factory
     * @param videoSurfaceComponent heavyweight video surface component, will become part of this components UI layout
     * @param fullScreenStrategy full screen strategy
     * @param inputEvents keyboard/mouse input event configuration
     * @param overlay heavyweight overlay
     */
    public EmbeddedMediaListPlayerComponent(MediaPlayerFactory mediaPlayerFactory, Component videoSurfaceComponent, FullScreenStrategy fullScreenStrategy, InputEvents inputEvents, Window overlay) {
        super(mediaPlayerFactory, videoSurfaceComponent, fullScreenStrategy, inputEvents, overlay);
        applyMediaPlayer();
        applyMediaList();
        onAfterConstruct();
    }

    /**
     * Construct an embedded media list player from a builder.
     *
     * @param spec builder
     */
    public EmbeddedMediaListPlayerComponent(MediaPlayerSpecs.EmbeddedMediaPlayerSpec spec) {
        this(spec.factory, spec.videoSurfaceComponent, spec.fullScreenStrategy, spec.inputEvents, spec.overlay);
    }

    /**
     * Construct an embedded media list player component with LibVLC initialisation arguments and reasonable defaults.
     *
     * @param libvlcArgs LibVLC initialisation arguments
     */
    public EmbeddedMediaListPlayerComponent(String... libvlcArgs) {
        this(new MediaPlayerFactory(libvlcArgs), null, null, null, null);
    }

    /**
     * Construct an embedded media list player component with reasonable defaults.
     */
    public EmbeddedMediaListPlayerComponent() {
        this(null, null, null, null, null);
    }

    @Override
    final protected EmbeddedMediaPlayer onCreateMediaPlayer() {
        this.mediaListPlayer = mediaPlayerFactory().mediaPlayers().newMediaListPlayer();
        this.mediaListPlayer.events().addMediaListPlayerEventListener(this);

        this.mediaList = mediaPlayerFactory().media().newMediaList();
        this.mediaList.events().addMediaListEventListener(this);

        // Use the native media player instance already associated with the media list player
        return mediaPlayerFactory().mediaPlayers().newEmbeddedMediaPlayer(this.mediaListPlayer);
    }

    private void applyMediaPlayer() {
        // Use the EmbeddedMediaPlayer created by the case class as the media player for the media list player
        mediaListPlayer.mediaPlayer().setMediaPlayer(mediaPlayer());
    }

    private void applyMediaList() {
        MediaListRef mediaListRef = mediaList.newMediaListRef();
        try {
            this.mediaListPlayer.list().setMediaList(mediaListRef);
        }
        finally {
            mediaListRef.release();
        }
    }

    /**
     * Get the embedded media list player reference.
     * <p>
     * An application uses this handle to control the media player, add listeners and so on.
     *
     * @return media list player
     */
    public final MediaListPlayer mediaListPlayer() {
        return mediaListPlayer;
    }

    @Override
    protected final void onBeforeRelease() {
        mediaListPlayer.release();
        mediaList.release();
    }

}
