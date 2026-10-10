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

package uk.co.caprica.vlcj.player.component;

import org.jspecify.annotations.Nullable;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.component.callback.CallbackImagePainter;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;

import javax.swing.*;

/**
 * Base implementation of a callback "direct-rendering" media player.
 * <p>
 * This class serves to keep the {@link CallbackMediaListPlayerComponent} concrete implementation clean and
 * un-cluttered.
 */
public abstract class CallbackMediaListPlayerComponentBase extends CallbackMediaPlayerComponent {

    /**
     * Create a media player component.
     * <p>
     * All constructor parameters are optional, reasonable defaults will be used as needed.
     *
     * @param mediaPlayerFactory factory used to create the component
     * @param fullScreenStrategy full-screen strategy
     * @param inputEvents required input events
     * @param bufferFormatCallback buffer format callback
     * @param lockBuffers <code>true</code> if the native video buffer should be locked; <code>false</code> if not
     * @param imagePainter image painter (video renderer)
     * @param videoSurfaceComponent lightweight video surface component
     * @param renderCallback render callback
     */
    public CallbackMediaListPlayerComponentBase(@Nullable MediaPlayerFactory mediaPlayerFactory, @Nullable FullScreenStrategy fullScreenStrategy, @Nullable InputEvents inputEvents, @Nullable BufferFormatCallback bufferFormatCallback, boolean lockBuffers, @Nullable CallbackImagePainter imagePainter, @Nullable JComponent videoSurfaceComponent, @Nullable RenderCallback renderCallback) {
        super(mediaPlayerFactory, fullScreenStrategy, inputEvents, lockBuffers, imagePainter, renderCallback, bufferFormatCallback, videoSurfaceComponent);
    }
}
