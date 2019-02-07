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
import uk.co.caprica.vlcj.player.directaudio.AudioCallback;
import uk.co.caprica.vlcj.player.embedded.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive.AdaptiveFullScreenStrategy;

import java.awt.*;

public final class MediaPlayerSpecs {

    public static EmbeddedMediaPlayerSpec embeddedMediaPlayerSpec() {
        return new EmbeddedMediaPlayerSpec();
    }

    public static CallbackMediaPlayerSpec callbackMediaPlayerSpec() {
        return new CallbackMediaPlayerSpec();
    }

    public static AudioPlayerSpec audioPlayerSpec() {
        return new AudioPlayerSpec();
    }

    public static final class EmbeddedMediaPlayerSpec {

        MediaPlayerFactory factory;
        Component videoSurfaceComponent;
        FullScreenStrategy fullScreenStrategy;
        InputEvents inputEvents;
        Window overlay;

        public EmbeddedMediaPlayerSpec withFactory(MediaPlayerFactory factory) {
            this.factory = factory;
            return this;
        }

        public EmbeddedMediaPlayerSpec withVideoSurfaceComponent(Component videoSurfaceComponent) {
            this.videoSurfaceComponent = videoSurfaceComponent;
            return this;
        }

        public EmbeddedMediaPlayerSpec withFullScreenStrategy(FullScreenStrategy fullScreenStrategy) {
            this.fullScreenStrategy = fullScreenStrategy;
            return this;
        }

        public EmbeddedMediaPlayerSpec withDefaultFullScreenStrategy(Window fullScreenWindow) {
            this.fullScreenStrategy = new AdaptiveFullScreenStrategy(fullScreenWindow);
            return this;
        }

        public EmbeddedMediaPlayerSpec withInputEvents(InputEvents inputEvents) {
            this.inputEvents = inputEvents;
            return this;
        }

        public EmbeddedMediaPlayerSpec withOverlay(Window overlay) {
            this.overlay = overlay;
            return this;
        }

        public EmbeddedMediaPlayerComponent embeddedMediaPlayer() {
            return new EmbeddedMediaPlayerComponent(this);
        }

        public EmbeddedMediaListPlayerComponent embeddedMediaListPlayer() {
            return new EmbeddedMediaListPlayerComponent(this);
        }

        private EmbeddedMediaPlayerSpec() {
        }

    }

    public static final class CallbackMediaPlayerSpec {

        MediaPlayerFactory factory;
        Component videoSurfaceComponent;
        Dimension size;
        BufferFormatCallback bufferFormatCallback;
        RenderCallback renderCallback;
        Boolean lockedBuffers;
        FullScreenStrategy fullScreenStrategy;
        InputEvents inputEvents;

        public CallbackMediaPlayerSpec withFactory(MediaPlayerFactory factory) {
            this.factory = factory;
            return this;
        }

        public CallbackMediaPlayerSpec withVideoSurfaceComponent(Component videoSurfaceComponent) {
            this.videoSurfaceComponent = videoSurfaceComponent;
            return this;
        }

        public CallbackMediaPlayerSpec withSize(Dimension size) {
            this.size = new Dimension(size);
            return this;
        }

        public CallbackMediaPlayerSpec withBufferFormatCallback(BufferFormatCallback bufferFormatCallback) {
            this.bufferFormatCallback = bufferFormatCallback;
            return this;
        }

        public CallbackMediaPlayerSpec withRenderCallback(RenderCallback renderCallback) {
            this.renderCallback = renderCallback;
            return this;
        }

        public CallbackMediaPlayerSpec withLockedBuffers(boolean lockedBuffers) {
            this.lockedBuffers = lockedBuffers;
            return this;
        }

        public CallbackMediaPlayerSpec withLockedBuffers() {
            this.lockedBuffers = true;
            return this;
        }

        public CallbackMediaPlayerSpec withFullScreenStrategy(FullScreenStrategy fullScreenStrategy) {
            this.fullScreenStrategy = fullScreenStrategy;
            return this;
        }

        public CallbackMediaPlayerSpec withDefaultFullScreenStrategy(Window fullScreenWindow) {
            this.fullScreenStrategy = new AdaptiveFullScreenStrategy(fullScreenWindow);
            return this;
        }

        public CallbackMediaPlayerSpec withInputEvents(InputEvents inputEvents) {
            this.inputEvents = inputEvents;
            return this;
        }

        public CallbackMediaPlayerComponent callbackMediaPlayer() {
            return new CallbackMediaPlayerComponent(this);
        }

        private CallbackMediaPlayerSpec() {
        }
        
    }

    public static final class AudioPlayerSpec {

        MediaPlayerFactory factory;

        public AudioPlayerSpec withFactory(MediaPlayerFactory factory) {
            this.factory = factory;
            return this;
        }

        public AudioPlayerComponent audioPlayer() {
            return new AudioPlayerComponent(this);
        }

        public AudioListPlayerComponent audioListPlayer() {
            return new AudioListPlayerComponent(this);
        }

        private AudioPlayerSpec() {
        }

    }

    public static void main(String[] args) {
        callbackMediaPlayerSpec().withFactory(null).withBufferFormatCallback(null);
    }
    
}
