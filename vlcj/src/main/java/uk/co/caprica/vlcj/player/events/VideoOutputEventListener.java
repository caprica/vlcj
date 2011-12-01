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
 * Copyright 2009, 2010, 2011 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.events;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/**
 * Specification for a component that is interested in receiving video output 
 * event notifications from the media player.
 * 
 * @deprecated use {@link MediaPlayerEventListener#videoOutput(MediaPlayer, int)} instead, this class will be removed in vlcj 1.3.0
 */
public interface VideoOutputEventListener {

  /**
   * A video output has been created.
   * 
   * @param mediaPlayer media player that generated the event
   * @param videoOutput <code>true</code> if a video output was started, <code>false</code> if the time-out expired (a video output might yet start) 
   */
  void videoOutputAvailable(MediaPlayer mediaPlayer, boolean videoOutput);
}
