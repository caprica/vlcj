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

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.callback.RenderCallback;
import uk.co.caprica.vlcj.player.directaudio.AudioCallback;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;

import javax.swing.*;
import java.awt.*;

// FIXME where are some problems that ideally we would stop with the type-safe builder
//        if setting a videosurfacecomponent or an overlay, Callback should not be available
//        if setting size, rendercallback and videosurfacecomponent should not be available
//        bufferformat and rendercallback must go together

/**
 * Specification for a type-safe builder for creating media player components.
 * <p>
 * @see MediaPlayerComponentBuilder
 */
public interface MediaPlayerComponentBuilders {

    interface Factory extends FactoryArgs, WithFactory {
    }

    interface FactoryArgs extends MediaPlayers {
        FactoryArgs withFactoryArgs(String... factoryArgs);
        FactoryArgs withExtraFactoryArgs(String... extraFactoryArgs);
    }

    interface WithFactory {
        MediaPlayers withFactory(MediaPlayerFactory factory);
    }

    interface MediaPlayers {
        Embedded embedded();
        Audio audio();
        Direct direct();
    }

    interface Embedded {
        Embedded withFullScreenStrategy(FullScreenStrategy fullScreenStrategy);
        Embedded withDefaultFullScreenStrategy(Window fullScreenWindow);
        Embedded withInputEvents(InputEvents inputEvents);
        Embedded withVideoSurfaceComponent(Component videoSurfaceComponent);
        Embedded withOverlay(Window overlay);
        Callback callback();
        EmbeddedMediaPlayerComponent embeddedMediaPlayerComponent();
        EmbeddedMediaListPlayerComponent embeddedMediaListPlayerComponent();
    }

    interface Callback {
        Callback withVideoSurfaceComponent(JComponent videoSurfaceComponent);
        Callback withSize(int width, int height);
        Callback withBufferFormatCallback(BufferFormatCallback bufferFormatCallback);
        Callback withRenderCallback(RenderCallback renderCallback);
        Callback withLockedBuffers(boolean lockBuffers);
        Callback withLockedBuffers();
        CallbackMediaPlayerComponent callbackMediaPlayerComponent();
    }

    interface Audio {
        AudioMediaPlayerComponent audioMediaPlayerComponent();
        AudioMediaListPlayerComponent audioMediaListPlayerComponent();
    }

    interface Direct extends AudioFormat {
    }

    interface AudioFormat {
        DirectAudio withFormat(String format, int rate, int channels);
    }

    interface DirectAudio {
        DirectAudio withCallback(AudioCallback audioCallback);
        DirectAudioPlayerComponent directAudioPlayerComponent();
    }

}
