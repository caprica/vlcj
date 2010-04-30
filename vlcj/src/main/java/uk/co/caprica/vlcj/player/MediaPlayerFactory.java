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

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.player.linux.LinuxMediaPlayer;
import uk.co.caprica.vlcj.player.mac.MacMediaPlayer;
import uk.co.caprica.vlcj.player.windows.WindowsMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Factory for media player instances.
 * <p>
 * The factory initialise a single libvlc instance for the entire application.
 * <p>
 * If required, you can create multiple factory instances each with their own
 * libvlc options.
 * <p>
 * This factory attempts to determine the run-time operating system and 
 * create an appropriate media player instance.
 * <p>
 * You should release the factory when your application terminates to properly
 * clean up native resources.
 * <p>
 * Usage:
 * <pre>
 *   // Set some options for libvlc
 *   String[] libvlcArgs = {};
 * 
 *   // Create a factory instance (once), you can keep a reference to this
 *   MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(libvlcArgs);
 *   
 *   // Create a media player instance for the run-time operating system
 *   MediaPlayer mediaPlayer = mediaPlayerFactory.newMediaPlayer();
 * 
 *   // Do some interesting things with the media player
 *   
 *   ...
 *   
 *   // Release the media player
 *   mediaPlayer.release();
 *   
 *   // Release the factory
 *   factory.release();
 * </pre>
 * 
 * @param args arguments to pass to libvlc
 * @return media player instance
 */
public class MediaPlayerFactory {
    
  /**
   * 
   */
  private final LibVlc libvlc = LibVlc.SYNC_INSTANCE;
  
  /**
   * 
   */
  private libvlc_instance_t instance;
  
  /**
   * 
   * 
   * @param libvlcArgs
   */
  public MediaPlayerFactory(String[] libvlcArgs) {
    this.instance = libvlc.libvlc_new(libvlcArgs.length, libvlcArgs);
    
    if(instance == null) {
      throw new IllegalStateException("Unable to initialise libvlc, check your libvlc options and/or check the console for error messages");
    }
  }

  /**
   * Release the native resources associated with this factory.
   */
  public void release() {
    if(instance != null) {
      libvlc.libvlc_release(instance);
      instance = null;
    }
  }
  
  /**
   * Create a new media player.
   * 
   * @param args arguments to pass to libvlc
   * @return media player instance
   */
  public MediaPlayer newMediaPlayer(FullScreenStrategy fullScreenStrategy) {
    MediaPlayer mediaPlayer;
    
    if(RuntimeUtil.isNix()) {
      mediaPlayer = new LinuxMediaPlayer(fullScreenStrategy, instance);
    }
    else if(RuntimeUtil.isWindows()) {
      mediaPlayer = new WindowsMediaPlayer(fullScreenStrategy, instance);
    }
    else if(RuntimeUtil.isMac()) {
      // Mac is not yet supported
      mediaPlayer = new MacMediaPlayer(fullScreenStrategy, instance);
    }
    else {
      throw new RuntimeException("Unable to create a media player - failed to detect a supported operating system");
    }
    
    return mediaPlayer;
  }
}
