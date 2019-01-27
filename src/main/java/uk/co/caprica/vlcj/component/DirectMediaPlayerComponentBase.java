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

import com.sun.jna.Memory;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;

import java.nio.ByteBuffer;

/**
 *
 *
 * <p>
 * Having this class saves cluttering the {@link DirectMediaPlayerComponent} with all of these empty event listener
 * methods.
 */
abstract class DirectMediaPlayerComponentBase extends MediaPlayerEventAdapter implements RenderCallback {

    protected static final Spec spec() {
        return new Spec();
    }

    public static final class Spec {

        protected MediaPlayerFactory factory;
        protected BufferFormatCallback formatCallback;
        protected RenderCallback renderCallback;
	protected boolean lockedBuffers;

        public Spec withFactory(MediaPlayerFactory factory) {
            this.factory = factory;
            return this;
        }

        public Spec withFormatCallback(BufferFormatCallback formatCallback) {
            this.formatCallback = formatCallback;
            return this;
        }

        public Spec withRenderCallback(RenderCallback renderCallback) {
            this.renderCallback = renderCallback;
            return this;
        }

        public Spec withLockedBuffers() {
            this.lockedBuffers = true;
            return this;
        }

        public Spec withLockedBuffers(boolean lockedBuffers) {
            this.lockedBuffers = lockedBuffers;
            return this;
        }

        private Spec() {
        }

    }

    protected DirectMediaPlayerComponentBase() {
    }

    // === RenderCallback =======================================================

    @Override
    public void display(DirectMediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
        // Default implementation does nothing, sub-classes should override this or provide their own implementation of
        // a RenderCallback
    }

}
