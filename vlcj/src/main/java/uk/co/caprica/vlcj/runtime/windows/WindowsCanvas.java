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

package uk.co.caprica.vlcj.runtime.windows;

import java.awt.Canvas;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Implementation of a Canvas that uses a native windows message hook to
 * make sure events are received while the video is playing.
 */
public class WindowsCanvas extends Canvas {

  /**
   * Mouse hook implementation.
   */
  private WindowsMouseHook mouseHook;
  
  /**
   * Create a new canvas.
   */
  public WindowsCanvas() {
    // Register a shutdown hook to make sure the mouse hook is removed
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        if(mouseHook != null) {
          mouseHook.release();
        }
      }
    });
    
    mouseHook = new WindowsMouseHook(this);
    mouseHook.start();
  }

  @Override
  public synchronized void addMouseListener(MouseListener l) {
    mouseHook.addMouseListener(l);
  }

  @Override
  public synchronized void removeMouseListener(MouseListener l) {
    mouseHook.removeMouseListener(l);
  }

  @Override
  public synchronized void addMouseMotionListener(MouseMotionListener l) {
    mouseHook.addMouseMotionListener(l);
  }
  
  @Override
  public synchronized void removeMouseMotionListener(MouseMotionListener l) {
    mouseHook.removeMouseMotionListener(l);
  }

  @Override
  protected void finalize() throws Throwable {
    if(mouseHook != null) {
      mouseHook.release();
      mouseHook = null;
    }
  }
}
