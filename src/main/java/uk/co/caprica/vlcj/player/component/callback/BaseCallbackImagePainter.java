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

package uk.co.caprica.vlcj.player.component.callback;

import uk.co.caprica.vlcj.player.base.VideoTrack;

/**
 * Base for a {@link CallbackImagePainter} providing default empty implementations for the optional template methods.
 */
abstract public class BaseCallbackImagePainter implements CallbackImagePainter {

    @Override
    public void newVideoBuffer(int width, int height) {
    }

    @Override
    public void videoTrackChanged(VideoTrack videoTrack) {
    }
}
