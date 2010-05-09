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

import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import uk.co.caprica.vlcj.runtime.windows.internal.LowLevelMouseProc;
import uk.co.caprica.vlcj.runtime.windows.internal.MSLLHOOKSTRUCT;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.User32.HHOOK;
import com.sun.jna.platform.win32.User32.MSG;
import com.sun.jna.platform.win32.User32.POINT;
import com.sun.jna.platform.win32.W32API.LRESULT;
import com.sun.jna.platform.win32.W32API.WPARAM;

/**
 * Windows Mouse Hook implementation.
 * <p>
 * This implementation registers a Windows hook and translates the native mouse
 * messages into Java mouse/mouse-motion events.
 * <p>
 * Not all events are supported, the events that are supported are:
 * <ul>
 *   <li>Mouse Moved</li>
 *   <li>Mouse Pressed</li>
 *   <li>Mouse Released</li>
 *   <li>Mouse Wheel Moved</li>
 * </ul>
 * <strong>Any listeners added must execute and return quickly since they will be 
 * executing as part of the native event queue.</strong> 
 * <p>
 * There are several deficiencies that could be addressed:
 * <ul>
 *   <li>Modifiers are not passed along with the event</li>
 *   <li>The semantic events like DRAGGED, CLICKED, ENTER and EXIT are not implemented</li>
 *   <li>Perhaps the events should be notified asynchronously via an executor so as not to hold up the hook</li>
 *   <li>The thread-safetiness issues are unclear especially wrt creating the hook in the thread</li> 
 * </ul>
 * The hook must be started after it has been created.
 */
public class WindowsMouseHook implements LowLevelMouseProc {

  /**
   * Log.
   */
  private static final Logger LOG = Logger.getLogger(WindowsMouseHook.class);
  
  /**
   * Native library instance.
   */
  private static User32 USER32_INSTANCE = User32.INSTANCE;

  /**
   * Collection of registered event listeners.
   */
  private final EventListenerList listenerList = new EventListenerList();

  /**
   * Component to report relative mouse coordinates against.
   */
  private final Component relativeTo;

  /**
   * Thread for the hook message loop.
   */
  private Thread hookThread;

  /**
   * Native hook handle.
   */
  private volatile HHOOK hook;
  
  /**
   * Create a new mouse hook.
   * 
   * @param relativeTo component to report mouse coordinates relative to
   */
  public WindowsMouseHook(Component relativeTo) {
    if(LOG.isDebugEnabled()) {LOG.debug("WindowsMouseHook(relativeTo=" + relativeTo + ")");}
    
    if(!Platform.isWindows()) {
      throw new IllegalStateException("Windows only");
    }
    
    this.relativeTo = relativeTo;
  }
  
  /**
   * 
   * 
   * @param listener
   */
  public void addMouseListener(MouseListener listener) {
    if(LOG.isDebugEnabled()) {LOG.debug("addMouseListener(listener=" + listener + ")");}

    listenerList.add(MouseListener.class, listener);
  }
  
  /**
   * 
   * 
   * @param listener
   */
  public void removeMouseListener(MouseListener listener) {
    if(LOG.isDebugEnabled()) {LOG.debug("removeMouseListener(listener=" + listener + ")");}

    listenerList.remove(MouseListener.class, listener);
  }
  
  /**
   * 
   * 
   * @param listener
   */
  public void addMouseMotionListener(MouseMotionListener listener) {
    if(LOG.isDebugEnabled()) {LOG.debug("addMouseMotionListener(listener=" + listener + ")");}

    listenerList.add(MouseMotionListener.class, listener);
  }
  
  /**
   * 
   * 
   * @param listener
   */
  public void removeMouseMotionListener(MouseMotionListener listener) {
    if(LOG.isDebugEnabled()) {LOG.debug("removeMouseMotionListener(listener=" + listener + ")");}

    listenerList.remove(MouseMotionListener.class, listener);
  }

  /**
   * 
   * 
   * @param listener
   */
  public void addMouseWheelListener(MouseWheelListener listener) {
    if(LOG.isDebugEnabled()) {LOG.debug("addMouseWheelListener(listener=" + listener + ")");}

    listenerList.add(MouseWheelListener.class, listener);
  }
  
  /**
   * 
   * 
   * @param listener
   */
  public void removeMouseWheelListener(MouseWheelListener listener) {
    if(LOG.isDebugEnabled()) {LOG.debug("removeMouseWheelListener(listener=" + listener + ")");}

    listenerList.remove(MouseWheelListener.class, listener);
  }
  
  /**
   * Start the hook.
   */
  public void start() {
    LOG.debug("start()");
    
    if(hookThread != null) {
      throw new IllegalStateException("Mouse hook already installed");
    }
    
    hookThread = new MouseHookThread();
    hookThread.start();
    
    installHook();
  }
  
  /**
   * 
   */
  public synchronized void release() {
    LOG.debug("release()");

    removeHook();
    
    LOG.debug("released");
  }
  
  @Override
  protected void finalize() throws Throwable {
    LOG.debug("finalize()");
    
    release();
  }

  @Override
  public LRESULT callback(int nCode, WPARAM wParam, MSLLHOOKSTRUCT lParam) {
    if(LOG.isTraceEnabled()) {LOG.trace("callback(nCode=" + nCode + ",wParam=" + wParam + ",lParam=" + lParam);}
    
    if(nCode >= 0) {
      Window window = SwingUtilities.getWindowAncestor(relativeTo);
      if(LOG.isTraceEnabled()) {LOG.trace("window=" + window);}

      // Is the window active...
      if(window != null && window.isActive()) {
        LOG.trace("window is active");
        
        // Is the component visible...
        // TODO is this still needed or is isActive good enough?
        if(relativeTo.isVisible() && relativeTo.isValid()) {
          LOG.trace("window is visible");
          
          // Did the event occur inside the component bounds...
          int absX = lParam.pt.x;
          int absY = lParam.pt.y;
          // FIXME there is a race here where relativeTo may no longer be visible
          Point componentPoint = relativeTo.getLocationOnScreen();
          int relX = componentPoint.x;
          int relY = componentPoint.y;
          int relW = relX + relativeTo.getWidth();
          int relH = relY + relativeTo.getHeight();
          if(absX >= relX && absY >= relY && absX < relW && absY < relH) {
            LOG.trace("event inside component bounds");
            
            // The event did occur inside the component bounds, so translate it...
            switch(wParam.intValue()) {
              case WM_MOUSEMOVE:
                fireMouseMotionEvent(MouseEvent.MOUSE_MOVED, MouseEvent.NOBUTTON, lParam);
                break;
            
              case WM_LBUTTONDOWN:
                fireMouseEvent(MouseEvent.MOUSE_PRESSED, MouseEvent.BUTTON1, lParam);
                break;
                  
              case WM_LBUTTONUP:
                fireMouseEvent(MouseEvent.MOUSE_RELEASED, MouseEvent.BUTTON1, lParam);
                break;
                  
              case WM_RBUTTONDOWN:
                fireMouseEvent(MouseEvent.MOUSE_PRESSED, MouseEvent.BUTTON2, lParam);
                break;
      
              case WM_RBUTTONUP:
                fireMouseEvent(MouseEvent.MOUSE_RELEASED, MouseEvent.BUTTON2, lParam);
                break;
                  
              case WM_MBUTTONDOWN:
                fireMouseEvent(MouseEvent.MOUSE_PRESSED, MouseEvent.BUTTON3, lParam);
                break;
            
              case WM_MBUTTONUP:
                fireMouseEvent(MouseEvent.MOUSE_RELEASED, MouseEvent.BUTTON3, lParam);
                break;
                
              case WM_MOUSEWHEEL:
                fireMouseWheelEvent(MouseEvent.MOUSE_WHEEL, lParam);
                break;
                
              default:
                break;
            }
          }
        }
      }
    }
    return USER32_INSTANCE.CallNextHookEx(hook, nCode, wParam, lParam.getPointer());
  }

  private void installHook() {
    LOG.debug("installHook()");
    hook = USER32_INSTANCE.SetWindowsHookEx(User32.WH_MOUSE_LL, WindowsMouseHook.this, Kernel32.INSTANCE.GetModuleHandle(null), 0);
    LOG.debug("hook installed");
  }
  
  private void removeHook() {
    LOG.debug("removeHook()");
    if(hook != null) {
      USER32_INSTANCE.UnhookWindowsHookEx(hook);
      hook = null;
    }
    LOG.debug("hook removed");
  }
  
  /**
   * Fire a mouse motion event to the registered listeners.
   * 
   * @param eventType
   * @param button
   * @param lParam
   */
  private void fireMouseMotionEvent(int eventType, int button, MSLLHOOKSTRUCT lParam) {
    if(LOG.isTraceEnabled()) {LOG.trace("fireMouseMotionEvent(eventType=" + eventType + ",button=" + button + ",lParam=" + lParam + ")");}

    MouseMotionListener[] listeners = listenerList.getListeners(MouseMotionListener.class);
    if(listeners.length > 0) {
      MouseEvent evt = createMouseEvent(eventType, button, lParam);
      for(int i = listeners.length -1; i >= 0; i--) {
        switch(eventType) {
          case MouseEvent.MOUSE_MOVED:
            listeners[i].mouseMoved(evt);
            break;
        }
      }
    }
  }

  /**
   * Fire a mouse event to the registered listeners.
   * 
   * @param eventType
   * @param button
   * @param lParam
   */
  private void fireMouseEvent(int eventType, int button, MSLLHOOKSTRUCT lParam) {
    if(LOG.isTraceEnabled()) {LOG.trace("fireMouseEvent(eventType=" + eventType + ",button=" + button + ",lParam=" + lParam + ")");}

    MouseListener[] listeners = listenerList.getListeners(MouseListener.class);
    if(listeners.length > 0) {
      MouseEvent evt = createMouseEvent(eventType, button, lParam);
      for(int i = listeners.length -1; i >= 0; i--) {
        switch(eventType) {
          case MouseEvent.MOUSE_PRESSED:
            listeners[i].mousePressed(evt);
            break;
            
          case MouseEvent.MOUSE_RELEASED:
            listeners[i].mouseReleased(evt);
            break;
        }
      }
    }
  }

  /**
   * Fire a mouse wheel event to the registered listeners.
   * 
   * @param eventType
   * @param button
   * @param lParam
   */
  private void fireMouseWheelEvent(int eventType, MSLLHOOKSTRUCT lParam) {
    if(LOG.isTraceEnabled()) {LOG.trace("fireMouseWheelEvent(eventType=" + eventType + ",lParam=" + lParam + ")");}
    
    MouseWheelListener[] listeners = listenerList.getListeners(MouseWheelListener.class);
    if(listeners.length > 0) {
      MouseWheelEvent evt = createMouseWheelEvent(eventType, lParam);
      for(int i = listeners.length -1; i >= 0; i--) {
        switch(eventType) {
          case MouseEvent.MOUSE_WHEEL:
            listeners[i].mouseWheelMoved(evt);
            break;
        }
      }
    }
  }
  
  /**
   * Create a new mouse event.
   * 
   * @param eventType
   * @param button
   * @param lParam
   * @return
   */
  private MouseEvent createMouseEvent(int eventType, int button, MSLLHOOKSTRUCT lParam) {
    POINT pt = lParam.pt;
    // FIXME race condition where relativeTo might not be visible
    Point rl = relativeTo.getLocationOnScreen();
    int x = pt.x - rl.x;
    int y = pt.y - rl.y;
    return new MouseEvent(
      relativeTo, 
      eventType, 
      lParam.time.longValue(), 
      0,
      x, y, 
      pt.x, pt.y, 
      0, 
      false, 
      button
    );
  }

  /**
   * Create a new mouse wheel event.
   * 
   * In Windows the rotation amount is positive when moving the wheel away from
   * the user whereas in Java the rotation amount is negative when moving the
   * wheel away from the user.
   * <p>
   * This implementation adjusts the sign so that the value is correct for Java.
   * 
   * @param eventType
   * @param lParam
   * @return
   */
  private MouseWheelEvent createMouseWheelEvent(int eventType, MSLLHOOKSTRUCT lParam) {
    POINT pt = lParam.pt;
    // FIXME race condition where relativeTo might not be visible
    Point rl = relativeTo.getLocationOnScreen();
    int x = pt.x - rl.x;
    int y = pt.y - rl.y;
    int wheelRotation = lParam.mouseData.intValue()  >> 16;
    return new MouseWheelEvent(
      relativeTo, 
      eventType, 
      lParam.time.longValue(), 
      0, 
      x, y, 
      pt.x, pt.y,
      0,
      false, 
      MouseWheelEvent.WHEEL_UNIT_SCROLL,
      1,
      wheelRotation * -1
    );
  }
  
  /**
   * Message loop for the mouse hook.
   */
  private class MouseHookThread extends Thread {
    @Override
    public void run()  {
      LOG.debug("run()");
      
      try {
        MSG msg = new MSG();
        int result;
        while((result = USER32_INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
          if(LOG.isTraceEnabled()) {LOG.trace("result=" + result);}
          // TODO we never seem to get here, is this working properly?
          if(result != -1) {
            USER32_INSTANCE.TranslateMessage(msg);
            USER32_INSTANCE.DispatchMessage(msg);
          }
        }
      } 
      catch(Exception e) {
        LOG.error(e);
      }
      
      LOG.debug("mouse hook runnable exits");
    }
  }
}
