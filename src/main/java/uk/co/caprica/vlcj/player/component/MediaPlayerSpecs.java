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
import uk.co.caprica.vlcj.player.component.callback.CallbackImagePainter;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive.AdaptiveFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.fullscreen.unsupported.UnsupportedFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;

import javax.swing.*;
import java.awt.*;

/**
 * Builders for the various media player components.
 * <p>
 * The builders can be used directly as constructor arguments for the corresponding component, or can be used to create
 * a component. The former case is useful when you are dynamically sub-classing the component.
 * <p>
 * Note that components and their "list" component counterparts use the same builders.
 * <p>
 * Generally, all component constructor (and therefore builder) parameters are optional, components will themselves
 * provide reasonable defaults.
 */
public final class MediaPlayerSpecs {

    /**
     * Create a new builder for an embedded media player, or an embedded media list player.
     *
     * @return embedded media player builder
     */
    public static EmbeddedMediaPlayerSpec embeddedMediaPlayerSpec() {
        return new EmbeddedMediaPlayerSpec();
    }

    /**
     * Create a new builder for a callback media player, or a callback media list player.
     *
     * @return callback media player builder
     */
    public static CallbackMediaPlayerSpec callbackMediaPlayerSpec() {
        return new CallbackMediaPlayerSpec();
    }

    /**
     * Create a new builder for an audio player, or an audio list player.
     *
     * @return embedded media player builder
     */
    public static AudioPlayerSpec audioPlayerSpec() {
        return new AudioPlayerSpec();
    }

    /**
     * Builder for an embedded media or media list player.
     */
    public static final class EmbeddedMediaPlayerSpec {

        MediaPlayerFactory factory;
        Component videoSurfaceComponent;
        FullScreenStrategy fullScreenStrategy;
        InputEvents inputEvents;
        Window overlay;

        /**
         * Specify the media player factory to use.
         *
         * @param factory media player factory
         * @return this builder
         */
        public EmbeddedMediaPlayerSpec withFactory(MediaPlayerFactory factory) {
            this.factory = factory;
            return this;
        }

        /**
         * Specify the heavyweight video surface component to use.
         * <p>
         * It is possible to render video into any AWT Component - optimally this would be a Canvas, but Window is
         * possible, as in fact is Button or anything else (although this is not recommended).
         * <p>
         * This video surface component will be added into the media player component layout.
         *
         * @param videoSurfaceComponent video surface component
         * @return this builder
         */
        public EmbeddedMediaPlayerSpec withVideoSurfaceComponent(Component videoSurfaceComponent) {
            this.videoSurfaceComponent = videoSurfaceComponent;
            return this;
        }

        /**
         * Specify the full-screen strategy to use.
         * <p>
         * By default if no strategy is set there will be no support for full-screen mode.
         *
         * @param fullScreenStrategy full-screen strategy
         * @return this builder
         */
        public EmbeddedMediaPlayerSpec withFullScreenStrategy(FullScreenStrategy fullScreenStrategy) {
            this.fullScreenStrategy = fullScreenStrategy;
            return this;
        }

        /**
         * Specify to use the default full-screen strategy.
         * <p>
         * The default strategy will use the "best" available native strategy depending on the run-time operating
         * system.
         *
         * @param fullScreenWindow window that will be made full-screen (the window containing the video surface)
         * @return this builder
         */
        public EmbeddedMediaPlayerSpec withDefaultFullScreenStrategy(Window fullScreenWindow) {
            this.fullScreenStrategy = new AdaptiveFullScreenStrategy(fullScreenWindow);
            return this;
        }

        /**
         * Specify to use the do-nothing unsupported full-screen strategy.
         * <p>
         * This is not really necessary as the default situation is to have no full-screen strategy.
         *
         * @return this builder
         */
        public EmbeddedMediaPlayerSpec withUnsupportedFullScreenStrategy() {
            this.fullScreenStrategy = new UnsupportedFullScreenStrategy();
            return this;
        }

        /**
         * Specify keyboard/mouse input-event configuration.
         *
         * @param inputEvents  keyboard/mouse configuration
         * @return this builder
         */
        public EmbeddedMediaPlayerSpec withInputEvents(InputEvents inputEvents) {
            this.inputEvents = inputEvents;
            return this;
        }

        /**
         * Specify a heavyweight overlay.
         * <p>
         * Any {@link Window} can be used as an overlay - it is recommended for the window to have a transparent
         * background.
         *
         * @param overlay overlay
         * @return this builder
         */
        public EmbeddedMediaPlayerSpec withOverlay(Window overlay) {
            this.overlay = overlay;
            return this;
        }

        /**
         * Create an embedded media player component from this builder.
         *
         * @return embedded media player component
         */
        public EmbeddedMediaPlayerComponent embeddedMediaPlayer() {
            return new EmbeddedMediaPlayerComponent(this);
        }

        /**
         * Create an embedded media list player component from this builder.
         *
         * @return embedded media list player component
         */
        public EmbeddedMediaListPlayerComponent embeddedMediaListPlayer() {
            return new EmbeddedMediaListPlayerComponent(this);
        }

        private EmbeddedMediaPlayerSpec() {
        }

    }

    /**
     * Builder for a callback media or callback media list player.
     */
    public static final class CallbackMediaPlayerSpec {

        MediaPlayerFactory factory;
        FullScreenStrategy fullScreenStrategy;
        InputEvents inputEvents;
        boolean lockedBuffers = true;
        CallbackImagePainter imagePainter;
        RenderCallback renderCallback;
        BufferFormatCallback bufferFormatCallback;
        JComponent videoSurfaceComponent;

        /**
         * Specify the media player factory to use.
         *
         * @param factory media player factory
         * @return this builder
         */
        public CallbackMediaPlayerSpec withFactory(MediaPlayerFactory factory) {
            this.factory = factory;
            return this;
        }

        /**
         * Specify the full-screen strategy to use.
         * <p>
         * By default if no strategy is set there will be no support for full-screen mode.
         *
         * @param fullScreenStrategy full-screen strategy
         * @return this builder
         */
        public CallbackMediaPlayerSpec withFullScreenStrategy(FullScreenStrategy fullScreenStrategy) {
            this.fullScreenStrategy = fullScreenStrategy;
            return this;
        }

        /**
         * Specify to use the default full-screen strategy.
         * <p>
         * The default strategy will use the "best" available native strategy depending on the run-time operating
         * system.
         *
         * @param fullScreenWindow window that will be made full-screen (the window containing the video surface)
         * @return this builder
         */
        public CallbackMediaPlayerSpec withDefaultFullScreenStrategy(Window fullScreenWindow) {
            this.fullScreenStrategy = new AdaptiveFullScreenStrategy(fullScreenWindow);
            return this;
        }

        /**
         * Specify to use the do-nothing unsupported full-screen strategy.
         * <p>
         * This is not really necessary as the default situation is to have no full-screen strategy.
         *
         * @return this builder
         */
        public CallbackMediaPlayerSpec withUnsupportedFullScreenStrategy() {
            this.fullScreenStrategy = new UnsupportedFullScreenStrategy();
            return this;
        }

        /**
         * Specify keyboard/mouse input-event configuration.
         *
         * @param inputEvents  keyboard/mouse configuration
         * @return this builder
         */
        public CallbackMediaPlayerSpec withInputEvents(InputEvents inputEvents) {
            this.inputEvents = inputEvents;
            return this;
        }

        /**
         * Specify whether or not the native video frame buffer should use operating system primitives to "lock" the
         * native memory (the aim is to prevent the native memory from being swapped to disk).
         * <p>
         * Buffers <em>are</em> locked by default.
         *
         * @param lockedBuffers <code>true</code> if the buffers should be locked; <code>false</code> if they should not
         * @return this builder
         */
        public CallbackMediaPlayerSpec withLockedBuffers(boolean lockedBuffers) {
            this.lockedBuffers = lockedBuffers;
            return this;
        }

        /**
         * Specify whether or not the native video frame buffer should use operating system primitives to "lock" the
         * native memory (the aim is to prevent the native memory from being swapped to disk).
         * <p>
         * Buffers <em>are</em> locked by default.
         * <p>
         * This method is unnecessary at the moment but is supplied in case the default changes to <code>false</code> in
         * the future.
         *
         * @return this builder
         */
        public CallbackMediaPlayerSpec withLockedBuffers() {
            this.lockedBuffers = true;
            return this;
        }

        /**
         * Specify the image painter (video renderer) to use.
         *
         * @param imagePainter image painter
         * @return this builder
         */
        public CallbackMediaPlayerSpec withImagePainter(CallbackImagePainter imagePainter) {
            this.imagePainter = imagePainter;
            return this;
        }

        /**
         * Specify the render callback to use.
         * <p>
         * A render callback is used where the application intends to take care of rendering the video itself.
         *
         * @param renderCallback render callback
         * @return this builder
         */
        public CallbackMediaPlayerSpec withRenderCallback(RenderCallback renderCallback) {
            this.renderCallback = renderCallback;
            return this;
        }

        /**
         * Specify the buffer format callback to use.
         * <p>
         * A buffer format callback is used where the application intends to take care of rendering the video itself.
         *
         * @param bufferFormatCallback buffer format callback
         * @return this builder
         */
        public CallbackMediaPlayerSpec withBufferFormatCallback(BufferFormatCallback bufferFormatCallback) {
            this.bufferFormatCallback = bufferFormatCallback;
            return this;
        }

        /**
         * Specify the lightweight video surface component to use.
         *
         * @param videoSurfaceComponent video surface component
         * @return this builder
         */
        public CallbackMediaPlayerSpec withVideoSurfaceComponent(JComponent videoSurfaceComponent) {
            this.videoSurfaceComponent = videoSurfaceComponent;
            return this;
        }

        /**
         * Create a callback media player component from this builder.
         *
         * @return callback media player component
         */
        public CallbackMediaPlayerComponent callbackMediaPlayer() {
            return new CallbackMediaPlayerComponent(this);
        }

        /**
         * Create a callback media list player component from this builder.
         *
         * @return callback media list player component
         */
        public CallbackMediaListPlayerComponent callbackMediaListPlayer() {
            return new CallbackMediaListPlayerComponent(this);
        }

        private CallbackMediaPlayerSpec() {
        }
        
    }

    /**
     * Builder for an audio or audio list player.
     */
    public static final class AudioPlayerSpec {

        MediaPlayerFactory factory;

        /**
         * Specify the media player factory to use.
         *
         * @param factory media player factory
         * @return this builder
         */
        public AudioPlayerSpec withFactory(MediaPlayerFactory factory) {
            this.factory = factory;
            return this;
        }

        /**
         * Create an audio player component from this builder.
         *
         * @return audio player component
         */
        public AudioPlayerComponent audioPlayer() {
            return new AudioPlayerComponent(this);
        }

        /**
         * Create an audio list player component from this builder.
         *
         * @return audio list player component
         */
        public AudioListPlayerComponent audioListPlayer() {
            return new AudioListPlayerComponent(this);
        }

        private AudioPlayerSpec() {
        }

    }

}
