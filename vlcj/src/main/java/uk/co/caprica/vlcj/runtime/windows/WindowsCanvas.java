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

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

/**
 * Implementation of a Canvas that uses a native windows message hook to
 * make sure events are received while the video is playing.
 * <p>
 * When VLC plays a movie file, it does not send keyboard or mouse events to
 * the Canvas component used as the video surface.
 * <p>
 * To work around this requires two strategies.
 * <p>
 * For keyboard events add a global AWTEventListener.
 * <p>
 * For mouse events a register a global Windows message hook.
 * <p>
 * This component implements both of those strategies behind the scenes - as
 * far as client code is concerned key and mouse listeners are added in the
 * usual way. 
 * <p>
 * <strong>This class is experimental, unsupported and unstable in operation.</strong>
 */
public class WindowsCanvas extends Canvas {

  /**
   * Log.
   */
  private static final Logger LOG = Logger.getLogger(WindowsCanvas.class);
  
  /**
   * List of registered event listeners.
   */
  private final EventListenerList listenerList = new EventListenerList();

  /**
   * Mouse hook implementation.
   */
  private WindowsMouseHook mouseHook;
  
  /**
   * Create a new canvas.
   */
  public WindowsCanvas() {
    LOG.debug("WindowsCanvas()");
    
    LOG.warn("You are using the WindowsCanvas implementation, this may cause spurious random VM crashes when you shut down your application");

    // Register a shutdown hook to make sure the mouse hook is removed
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        LOG.debug("run()");
        if(mouseHook != null) {
          mouseHook.release();
        }
        LOG.debug("runnable exits");
      }
    });
    
    mouseHook = new WindowsMouseHook(this);
    if(LOG.isDebugEnabled()) {LOG.debug("mouseHook=" + mouseHook);}
    
    mouseHook.start();
    
    Toolkit.getDefaultToolkit().addAWTEventListener(new WindowsKeyListener(), AWTEvent.KEY_EVENT_MASK);
  }

  /**
   * 
   */
  public void release() {
    LOG.debug("release()");
    
    if(mouseHook != null) {
      mouseHook.release();
      mouseHook = null;
    }
  }
  
  @Override
  public synchronized void addMouseListener(MouseListener l) {
    if(LOG.isDebugEnabled()) {LOG.debug("addMouseListener(l=" + l + ")");}
    
    mouseHook.addMouseListener(l);
  }

  @Override
  public synchronized void removeMouseListener(MouseListener l) {
    if(LOG.isDebugEnabled()) {LOG.debug("removeMouseListener(l=" + l + ")");}

    mouseHook.removeMouseListener(l);
  }

  @Override
  public synchronized void addMouseMotionListener(MouseMotionListener l) {
    if(LOG.isDebugEnabled()) {LOG.debug("addMouseMotionListener(l=" + l + ")");}

    mouseHook.addMouseMotionListener(l);
  }
  
  @Override
  public synchronized void removeMouseMotionListener(MouseMotionListener l) {
    if(LOG.isDebugEnabled()) {LOG.debug("removeMouseMotionListener(l=" + l + ")");}

    mouseHook.removeMouseMotionListener(l);
  }

  @Override
  public synchronized void addMouseWheelListener(MouseWheelListener l) {
    if(LOG.isDebugEnabled()) {LOG.debug("addMouseWheelListener(l=" + l + ")");}

    mouseHook.addMouseWheelListener(l);
  }

  @Override
  public synchronized void removeMouseWheelListener(MouseWheelListener l) {
    if(LOG.isDebugEnabled()) {LOG.debug("removeMouseWheelListener(l=" + l + ")");}

    mouseHook.removeMouseWheelListener(l);
  }

  @Override
  public synchronized void addKeyListener(KeyListener l) {
    if(LOG.isDebugEnabled()) {LOG.debug("addKeyListener(l=" + l + ")");}

    listenerList.add(KeyListener.class, l);
  }

  @Override
  public synchronized void removeKeyListener(KeyListener l) {
    if(LOG.isDebugEnabled()) {LOG.debug("removeKeyListener(l=" + l + ")");}

    listenerList.remove(KeyListener.class, l);
  }

  @Override
  protected void finalize() throws Throwable {
    LOG.debug("finalize()");
    
    release();
  }
  
  /**
   * Global AWT event listener used to propagate key events to listeners
   * registered in the usual way.
   */
  private final class WindowsKeyListener implements AWTEventListener {

    @Override
    public void eventDispatched(AWTEvent event) {
      if(LOG.isTraceEnabled()) {LOG.trace("eventDispatched(event=" + event + ")");}
      
      // Only interested in key events...
      if(event instanceof KeyEvent) {
        KeyEvent keyEvent = (KeyEvent)event;
        // Only interested in events for this component...
        if(keyEvent.getComponent() == WindowsCanvas.this) {
          // Propagate the event to each registered listener...
          KeyListener[] listeners = listenerList.getListeners(KeyListener.class);
          for(int i = listeners.length - 1; i >= 0; i--) {
            switch(keyEvent.getID()) {
              case KeyEvent.KEY_PRESSED:
                listeners[i].keyPressed(keyEvent);
                break;
                
              case KeyEvent.KEY_RELEASED:
                listeners[i].keyReleased(keyEvent);
                break;
            
              case KeyEvent.KEY_TYPED:
                listeners[i].keyTyped(keyEvent);
                break;
            }
          }
        }
      }
    }
  }
}
