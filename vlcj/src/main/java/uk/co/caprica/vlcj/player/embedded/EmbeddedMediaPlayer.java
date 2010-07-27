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

package uk.co.caprica.vlcj.player.embedded;

import java.awt.Canvas;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.log.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * Implementation of a media player that renders video to an embedded Canvas
 * component.
 */
public abstract class EmbeddedMediaPlayer extends MediaPlayer {

  /**
   * Full-screen strategy implementation, may be <code>null</code>. 
   */
  private final FullScreenStrategy fullScreenStrategy;
  
  /**
   * Component to render the video to.
   */
  private Canvas videoSurface;
  
  /**
   * Create a new media player.
   * <p>
   * Full-screen will not be supported.
   * 
   * @param instance libvlc instance
   */
  public EmbeddedMediaPlayer(libvlc_instance_t instance) {
    this(instance, null);
  }

  /**
   * Create a new media player.
   * 
   * @param instance liblc instance
   * @param fullScreenStrategy
   */
  public EmbeddedMediaPlayer(libvlc_instance_t instance, FullScreenStrategy fullScreenStrategy) {
    super(instance);
    
    this.fullScreenStrategy = fullScreenStrategy;
  }

  /**
   * Set the component used to render video.
   * <p>
   * The video surface component must be visible and fully 'realised' before
   * calling this method.
   * 
   * @param videoSurface component to render video to
   */
  public void setVideoSurface(Canvas videoSurface) {
    Logger.debug("setVideoSurface(videoSurface={})", videoSurface);
    
    // Keep a hard reference to the video surface component
    this.videoSurface = videoSurface;
    
    // Delegate to the template method in the OS-specific implementation 
    // class to actually set the video surface
    nativeSetVideoSurface(mediaPlayerInstance(), videoSurface);
  }
  
  /**
   * Toggle whether the video display is in full-screen or not.
   * <p>
   * This method defers to the full-screen strategy implementation.
   */
  public void toggleFullScreen() {
    Logger.debug("toggleFullScreen()");
    
    if(fullScreenStrategy != null) {
      setFullScreen(!fullScreenStrategy.isFullScreenMode());
    }
  }

  /**
   * Set full-screen mode. 
   * <p>
   * This method defers to the full-screen strategy implementation.
   * 
   * @param fullScreen true for full-screen, otherwise false
   */
  public void setFullScreen(boolean fullScreen) {
    Logger.debug("setFullScreen(fullScreen={})", fullScreen);
    
    if(fullScreenStrategy != null) {
      if(fullScreen) {
        fullScreenStrategy.enterFullScreenMode();
      }
      else {
        fullScreenStrategy.exitFullScreenMode();
      }
    }
  }
  
  /**
   * Test the current full-screen mode.
   * <p>
   * This method defers to the full-screen strategy implementation.
   * 
   * @return true if full-screen is active, otherwise false
   */
  public boolean isFullScreen() {
    Logger.debug("isFullScreen()");
    
    if(fullScreenStrategy != null) {
      return fullScreenStrategy.isFullScreenMode();
    }
    else {
      return false;
    }
  }
  
  /**
   * Get the contents of the video surface component. 
   * <p>
   * This implementation uses the AWT Robot class to capture the contents of
   * the video surface component.
   * <p>
   * The size of the returned image will match the current size of the video
   * surface.
   * <p>
   * <strong>Since this implementation uses the AWT Robot class to make a
   * screen capture, care must be taken when invoking this method to ensure 
   * that nothing else is overlaying the video surface!</strong>
   * 
   * @return current contents of the video surface
   */
  public BufferedImage getVideoSurfaceContents() {
    Logger.debug("getVideoSurfaceContents()");
    try {
      Rectangle bounds = videoSurface.getBounds();
      bounds.setLocation(videoSurface.getLocationOnScreen());
      return new Robot().createScreenCapture(bounds);
    } 
    catch(Exception e) {
      throw new RuntimeException("Failed to get video surface contents", e);
    }
  }
  
  /**
   * Template method for setting the video surface natively.
   * <p>
   * Implementing classes should override this method to invoke the appropriate
   * libvlc method to set the video surface.
   * 
   * @param instance media player instance
   * @param videoSurface video surface component
   */
  protected abstract void nativeSetVideoSurface(libvlc_media_player_t instance, Canvas videoSurface);
}
