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

package uk.co.caprica.vlcj.component;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListEventListener;
import uk.co.caprica.vlcj.player.embedded.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class CallbackMediaListPlayerComponentBase extends CallbackMediaPlayerComponent implements MediaListPlayerEventListener, MediaListEventListener {

    public CallbackMediaListPlayerComponentBase(MediaPlayerFactory mediaPlayerFactory, JComponent videoSurfaceComponent, Dimension size, BufferFormatCallback bufferFormatCallback, RenderCallback renderCallback, boolean lockBuffers, FullScreenStrategy fullScreenStrategy, InputEvents inputEvents) {
        super(mediaPlayerFactory, videoSurfaceComponent, size, bufferFormatCallback, renderCallback, lockBuffers, fullScreenStrategy, inputEvents);
    }

    // === MediaListPlayerEventListener =========================================

    @Override
    public void mediaListEndReached(MediaList mediaList) {

    }

    @Override
    public void mediaListPlayerFinished(MediaListPlayer mediaListPlayer) {

    }

    @Override
    public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item) {

    }

    @Override
    public void stopped(MediaListPlayer mediaListPlayer) {

    }

    // === MediaListEventListener ===============================================

    @Override
    public void mediaListWillAddItem(MediaList mediaList, libvlc_media_t mediaInstance, int index) {

    }

    @Override
    public void mediaListItemAdded(MediaList mediaList, libvlc_media_t mediaInstance, int index) {

    }

    @Override
    public void mediaListWillDeleteItem(MediaList mediaList, libvlc_media_t mediaInstance, int index) {

    }

    @Override
    public void mediaListItemDeleted(MediaList mediaList, libvlc_media_t mediaInstance, int index) {

    }

}
