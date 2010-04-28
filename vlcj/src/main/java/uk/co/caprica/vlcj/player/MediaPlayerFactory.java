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
 * Copyright 2009, 2010 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

import uk.co.caprica.vlcj.player.linux.LinuxMediaPlayer;
import uk.co.caprica.vlcj.player.mac.MacMediaPlayer;
import uk.co.caprica.vlcj.player.windows.WindowsMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Factory for media player instances.
 * <p>
 * This factory attempts to determine the run-time operating system and 
 * create an appropriate media player instance.
 * <p>
 * Usage:
 * <pre>
 *   // Create a factory instance (once), you can keep a reference to this
 *   MediaPlayerFactory factory = new MediaPlayerFactory();
 *   
 *   // Create a media player instance for the run-time operating system
 *   String[] libVlcArgs = {}; // Set whatever options you want
 *   MediaPlayer mediaPlayer = factory.newMediaPlayer(libvlcArgs);
 * 
 *   // Do some interesting things with the media player
 * </pre>
 * 
 * @param args arguments to pass to libvlc
 * @return media player instance
 */
public class MediaPlayerFactory {
    
  /**
   * Create a new media player.
   * 
   * @param args arguments to pass to libvlc
   * @return media player instance
   */
  public MediaPlayer newMediaPlayer(String[] args, FullScreenStrategy fullScreenStrategy) {
    MediaPlayer mediaPlayer;
    
    if(RuntimeUtil.isNix()) {
      mediaPlayer = new LinuxMediaPlayer(args, fullScreenStrategy);
    }
    else if(RuntimeUtil.isWindows()) {
      mediaPlayer = new WindowsMediaPlayer(args, fullScreenStrategy);
    }
    else if(RuntimeUtil.isMac()) {
      // Mac is not yet supported
      mediaPlayer = new MacMediaPlayer(args, fullScreenStrategy);
    }
    else {
      throw new RuntimeException("Unable to create a media player - failed to detect a supported operating system");
    }
    
    return mediaPlayer;
  }
}
