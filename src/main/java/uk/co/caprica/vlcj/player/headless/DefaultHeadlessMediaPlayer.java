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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.headless;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.player.DefaultMediaPlayer;

/**
 * A media player implementation with no user interface component to render the video to.
 * <p>
 * This is useful for a streaming server component.
 * <p>
 * Client applications must use the correct media options otherwise a native video window may still
 * appear.
 */
public class DefaultHeadlessMediaPlayer extends DefaultMediaPlayer implements HeadlessMediaPlayer {

    /**
     * Create a new media player.
     *
     * @param libvlc native interface
     * @param instance libvlc instance
     */
    public DefaultHeadlessMediaPlayer(LibVlc libvlc, libvlc_instance_t instance) {
        super(libvlc, instance);
    }
}
