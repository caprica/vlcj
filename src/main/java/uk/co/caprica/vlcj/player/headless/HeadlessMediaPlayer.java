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

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

/**
 * Specification for a media player component that is not intended to display the media.
 * <p>
 * This is useful, for example, when a media player component is being used to stream media.
 * <p>
 * Note that client applications must still set proper media options otherwise a native video window
 * may still appear, for example the following media options could be used when creating the
 * {@link MediaPlayerFactory}.
 *
 * <pre>
 * String[] args = {&quot;--vout&quot;, &quot;dummy&quot;};
 * </pre>
 */
public interface HeadlessMediaPlayer extends MediaPlayer {
    // Nothing extra
}
