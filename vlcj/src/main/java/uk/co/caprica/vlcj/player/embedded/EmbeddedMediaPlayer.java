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
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.log.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * Implementation of a media player that renders video to an embedded Canvas
 * component.
 * <p>
 * This implementation supports the use of an 'overlay' window that will track
 * the video surface position and size. Such an overlay could be used to paint
 * custom graphics over the top of the video.
 * <p>
 * The overlay window should be non-opaque - support for this depends on the 
 * JVM, desktop window manager and graphics device hardware and software.
 * <p>
 * The overlay also has some significant limitations, it is a component that
 * covers the video surface component and will prevent mouse and keyboard 
 * events from being processed by the video surface. Workarounds to delegate
 * the mouse and keyboard events to the underlying Canvas may be possible but
 * that is a responsibility of the overlay component itself and not these
 * bindings.
 * <p>
 * The overlay will also 'lag' the main application frame when the frame is
 * dragged - the event used to track the frame position does not fire until 
 * after the window drag operation has completed (i.e. the mouse pointer is
 * released).
 * <p>
 * A further limitation is that the overlay will not appear when full-screen
 * exclusive mode is used - if an overlay is required in full-screen mode then
 * the full-screen mode must be simulated (by re-sizing the main window, 
 * removing decorations and so on).
 * <p>
 * If an overlay is used, then because the window is required to be non-opaque
 * then it will appear in front of <strong>all</strong> other desktop windows,
 * including application dialog windows. For this reason, it may be necessary
 * to disable the overlay while displaying dialog boxes, or when the window
 * is deactivated.
 * <p>
 * The overlay implementation in this class simply keeps a supplied window in
 * sync with the video surface. It is the responsibility of the client 
 * application itself to supply an appropriate overlay component.
 * <p>
 * <strong>Finally, the overlay is experimental and support for the overlay may
 * be changed or removed.</strong> 
 */
public abstract class EmbeddedMediaPlayer extends MediaPlayer {

  /**
   * Full-screen strategy implementation, may be <code>null</code>. 
   */
  private final FullScreenStrategy fullScreenStrategy;
  
  /**
   * Listener implementation used to keep the overlay position and size in sync
   * with the video surface.
   */
  private final OverlayComponentAdapter overlayComponentAdapter;
  
  /**
   * Listener implementation used to keep the overlay visibility state in sync
   * with the video surface.
   */
  private final OverlayWindowAdapter overlayWindowAdapter;
  
  /**
   * Component to render the video to.
   */
  private Canvas videoSurface;
  
  /**
   * Setting the video surface is deferred, this flag tracks whether or not the
   * video surface has been set for the native player.
   */
  private boolean videoSurfaceSet;

  /**
   * Optional overlay component.
   */
  private Window overlay;
  
  /**
   * Track the requested overlay enabled/disabled state so it can be restored
   * when needed.
   */
  private boolean requestedOverlay;
  
  /**
   * Track whether or not the overlay should be restored when the video surface
   * is shown/hidden. 
   */
  private boolean restoreOverlay;

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
    this.overlayComponentAdapter = new OverlayComponentAdapter();
    this.overlayWindowAdapter = new OverlayWindowAdapter();
  }

  /**
   * Set the component used to render video.
   * <p>
   * Setting the video surface on the native component is actually deferred so
   * the component used as the video surface need <em>not</em> be visible and
   * fully realised before calling this method.
   * 
   * @param videoSurface component to render video to
   */
  public void setVideoSurface(Canvas videoSurface) {
    Logger.debug("setVideoSurface(videoSurface={})", videoSurface);
    
    // Keep a hard reference to the video surface component
    this.videoSurface = videoSurface;

    // Defer setting the video surface until later
    this.videoSurfaceSet = false;
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
   * Get the overlay component.
   * 
   * @return overlay component, may be <code>null</code>
   */
  public Window getOverlay() {
    Logger.debug("getOverlay()");
    
    return overlay;
  }
  
  /**
   * Set a new overlay component.
   * <p>
   * The existing overlay if there is one will be disabled.
   * <p>
   * The new overlay will <strong>not</strong> automatically be enabled.
   * 
   * @param overlay overlay component
   */
  public void setOverlay(Window overlay) {
    Logger.debug("setOverlay(overlay={})", overlay);
    
    if(videoSurface != null) {
      // Disable the current overlay if there is one
      enableOverlay(false);
    
      // Remove the existing overlay if there is one
      removeOverlay();

      // Add the new overlay, but do not enable it
      addOverlay(overlay);
    }
    else {
      throw new IllegalStateException("Can't set an overlay when there's no video surface");
    }
  }

  /**
   * Enable/disable the overlay component if there is one.
   * 
   * @param enable whether to enable the overlay or disable it
   */
  public void enableOverlay(boolean enable) {
    Logger.debug("enableOverlay(enable={})", enable);
    
    requestedOverlay = enable;
    
    if(overlay != null) {
      if(enable) {
        if(!overlay.isVisible()) {
          overlay.setLocation(videoSurface.getLocationOnScreen());
          overlay.setSize(videoSurface.getSize());
          Window window = (Window)SwingUtilities.getAncestorOfClass(Window.class, videoSurface);
          window.addComponentListener(overlayComponentAdapter);
          overlay.setVisible(true);
        }
      }
      else {
        if(overlay.isVisible()) {
          overlay.setVisible(false);
          Window window = (Window)SwingUtilities.getAncestorOfClass(Window.class, videoSurface);
          window.removeComponentListener(overlayComponentAdapter);
        }
      }
    }
  }
  
  /**
   * Check whether or not there is an overlay component currently enabled.
   * 
   * @return true if there is an overlay enabled, otherwise false 
   */
  public boolean overlayEnabled() {
    Logger.debug("overlayEnabled()");
    
    return overlay != null && overlay.isVisible();
  }
  
  /**
   * 
   * 
   * @param overlay overlay window
   */
  private void addOverlay(Window overlay) {
    Logger.debug("addOverlay(overlay={})", overlay);
    if(overlay != null) {
      Window window = (Window)SwingUtilities.getAncestorOfClass(Window.class, videoSurface);
      window.addWindowListener(overlayWindowAdapter);
      this.overlay = overlay;
    }
  }
  
  /**
   * 
   * 
   * @param overlay overlay window
   */
  private void removeOverlay() {
    Logger.debug("removeOverlay()");
    if(overlay != null) {
      Window window = (Window)SwingUtilities.getAncestorOfClass(Window.class, videoSurface);
      window.removeWindowListener(overlayWindowAdapter);
      overlay = null;
    }
  }
  
//  @Override
  protected void onBeforePlay() {
    Logger.debug("onBeforePlay()");
    Logger.debug("videoSurfaceSet={}", videoSurfaceSet);
    if(!videoSurfaceSet && videoSurface != null) {
      // Delegate to the template method in the OS-specific implementation 
      // class to actually set the video surface
      nativeSetVideoSurface(mediaPlayerInstance(), videoSurface);
      videoSurfaceSet = true;
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
  
  /**
   * Component event listener to keep the overlay component in sync with the
   * video surface component.
   */
  private final class OverlayComponentAdapter extends ComponentAdapter {

//    @Override
    public void componentResized(ComponentEvent e) {
      Logger.trace("componentResized(e={})", e);
      overlay.setSize(videoSurface.getSize());
    }

//    @Override
    public void componentMoved(ComponentEvent e) {
      Logger.trace("componentMoved(e={})", e);
      overlay.setLocation(videoSurface.getLocationOnScreen());
    }

//    @Override
    public void componentShown(ComponentEvent e) {
      Logger.trace("componentShown(e={})", e);
      showOverlay();
    }

//    @Override
    public void componentHidden(ComponentEvent e) {
      Logger.trace("componentHidden(e={})", e);
      hideOverlay();
    }
  }
  
  /**
   * Window event listener to hide the overlay when the video window is hidden,
   * and vice versa.
   */
  private final class OverlayWindowAdapter extends WindowAdapter {
    
//    @Override
    public void windowIconified(WindowEvent e) {
      Logger.trace("windowIconified(e={})", e);
      hideOverlay();
    }

//    @Override
    public void windowDeiconified(WindowEvent e) {
      Logger.trace("windowDeiconified(e={})", e);
      showOverlay();
    }
  }
  
  /**
   * 
   */
  private void showOverlay() {
    Logger.trace("showOverlay()");
    if(restoreOverlay) {
      enableOverlay(true);
    }
  }
  
  /**
   * 
   */
  private void hideOverlay() {
    Logger.trace("hideOverlay()");
    if(requestedOverlay) {
      restoreOverlay = true;
      enableOverlay(false);
    }
    else {
      restoreOverlay = false;
    }
  }
}
