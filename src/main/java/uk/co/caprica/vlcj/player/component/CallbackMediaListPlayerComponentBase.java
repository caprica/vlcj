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
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListEventListener;
import uk.co.caprica.vlcj.player.component.callback.CallbackImagePainter;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;

import javax.swing.*;

/**
 * Base implementation of a callback "direct-rendering" media player.
 * <p>
 * This class serves to keep the {@link CallbackMediaListPlayerComponent} concrete implementation clean and
 * un-cluttered.
 */
@SuppressWarnings("serial")
public class CallbackMediaListPlayerComponentBase extends CallbackMediaPlayerComponent implements MediaListPlayerEventListener, MediaListEventListener {

    /**
     * Create a media player component.
     * <p>
     * All constructor parameters are optional, reasonable defaults will be used as needed.
     *
     * @param mediaPlayerFactory factory used to create the component
     * @param fullScreenStrategy full-screen strategy
     * @param inputEvents required input events
     * @param bufferFormatCallback
     * @param lockBuffers
     * @param imagePainter
     * @param videoSurfaceComponent
     * @param renderCallback
     */
    public CallbackMediaListPlayerComponentBase(MediaPlayerFactory mediaPlayerFactory, FullScreenStrategy fullScreenStrategy, InputEvents inputEvents, BufferFormatCallback bufferFormatCallback, boolean lockBuffers, CallbackImagePainter imagePainter, JComponent videoSurfaceComponent, RenderCallback renderCallback) {
        super(mediaPlayerFactory, fullScreenStrategy, inputEvents, lockBuffers, imagePainter, renderCallback, bufferFormatCallback, videoSurfaceComponent);
    }

    // === MediaListPlayerEventListener =========================================

    @Override
    public void mediaListEndReached(MediaList mediaList) {
    }

    @Override
    public void mediaListPlayerFinished(MediaListPlayer mediaListPlayer) {
    }

    @Override
    public void nextItem(MediaListPlayer mediaListPlayer, MediaRef item) {
    }

    @Override
    public void stopped(MediaListPlayer mediaListPlayer) {
    }

    // === MediaListEventListener ===============================================

    @Override
    public void mediaListWillAddItem(MediaList mediaList, MediaRef item, int index) {
    }

    @Override
    public void mediaListItemAdded(MediaList mediaList, MediaRef item, int index) {
    }

    @Override
    public void mediaListWillDeleteItem(MediaList mediaList, MediaRef item, int index) {
    }

    @Override
    public void mediaListItemDeleted(MediaList mediaList, MediaRef item, int index) {
    }

}
