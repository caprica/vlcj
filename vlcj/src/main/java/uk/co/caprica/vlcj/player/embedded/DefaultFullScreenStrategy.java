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

import java.awt.GraphicsEnvironment;
import java.awt.Window;

import org.apache.log4j.Logger;

/**
 * Default implementation of a full screen strategy that attempts to use the
 * JDK full-screen support.
 * <p>
 * Client applications may wish to set the DisplayMode - extend this class and
 * override {@link #enterFullScreenMode()} to do so.
 */
public class DefaultFullScreenStrategy implements FullScreenStrategy {

  /**
   * Log.
   */
  private static final Logger LOG = Logger.getLogger(DefaultFullScreenStrategy.class);
  
  /**
   * The component that will be made full-screen. 
   */
  private final Window window;
  
  /**
   * Create a new full-screen strategy.
   * 
   * @param window component that will be made full-screen
   */
  public DefaultFullScreenStrategy(Window window) {
    if(LOG.isDebugEnabled()) {LOG.debug("DefaultFullScreenStrategy(window=" + window + ")");}
    
    if(window != null) {
      this.window = window;
    }
    else {
      throw new IllegalArgumentException("Window must not be null");
    }
  }
  
  @Override
  public void enterFullScreenMode() {
    LOG.debug("enterFullScreenMode()");
    
    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(window);
  }

  @Override
  public void exitFullScreenMode() {
    LOG.debug("exitFullScreenMode()");
    
    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
  }

  @Override
  public boolean isFullScreenMode() {
    LOG.debug("isFullScreenMode()");
    
    return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getFullScreenWindow() != null;
  }
}
