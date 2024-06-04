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
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media.callback.seekable;

import uk.co.caprica.vlcj.media.callback.DefaultCallbackMedia;

/**
 * Base implementation for seekable media.
 */
public abstract class SeekableCallbackMedia extends DefaultCallbackMedia  {

    /**
     * Create a media instance with a default IO buffer size.
     */
    public SeekableCallbackMedia() {
        super(true);
    }

    /**
     * Create a media instance.
     *
     * @param ioBufferSize IO buffer size
     */
    public SeekableCallbackMedia(int ioBufferSize) {
        super(true, ioBufferSize);
    }
}
