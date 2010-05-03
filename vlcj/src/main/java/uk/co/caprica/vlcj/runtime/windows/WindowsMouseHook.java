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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.event.EventListenerList;

import uk.co.caprica.vlcj.runtime.windows.internal.LowLevelMouseProc;
import uk.co.caprica.vlcj.runtime.windows.internal.MOUSEHOOKSTRUCT;

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
 * </ul>
 * <strong>Any listeners added must execute and return quickly since they will be 
 * executing as part of the native event queue.</strong> 
 * <p>
 * There are several deficiencies that could be addressed:
 * <ul>
 *   <li>Modifiers are not passed along with the event</li>
 *   <li>The semantic events like DRAGGED, CLICKED, ENTER and EXIT are not implemented</li>
 *   <li>Perhaps the events should be notified asynchronously via an executor so as not to hold up the hook</li> 
 * </ul>
 * The hook must be started after it has been created.
 */
public class WindowsMouseHook implements LowLevelMouseProc {

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
  private volatile HHOOK hHook;
  
  /**
   * Create a new mouse hook.
   * 
   * @param relativeTo component to report mouse coordinates relative to
   */
  public WindowsMouseHook(Component relativeTo) {
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
    listenerList.add(MouseListener.class, listener);
  }
  
  /**
   * 
   * 
   * @param listener
   */
  public void removeMouseListener(MouseListener listener) {
    listenerList.remove(MouseListener.class, listener);
  }
  
  /**
   * 
   * 
   * @param listener
   */
  public void addMouseMotionListener(MouseMotionListener listener) {
    listenerList.add(MouseMotionListener.class, listener);
  }
  
  /**
   * 
   * 
   * @param listener
   */
  public void removeMouseMotionListener(MouseMotionListener listener) {
    listenerList.remove(MouseMotionListener.class, listener);
  }

  /**
   * Start the hook.
   */
  public void start() {
    if(hookThread != null) {
      throw new IllegalStateException("Mouse hook already installed");
    }
    hookThread = new MouseHookThread();
    hookThread.start();
  }
  
  /**
   * 
   */
  public synchronized void release() {
    HHOOK hook = getHook();
    if(hook != null) {
      USER32_INSTANCE.UnhookWindowsHookEx(hHook);
      hook = null;
      hookThread.interrupt();
    }
  }
  
  @Override
  protected void finalize() throws Throwable {
    release();
  }

  /**
   * 
   * 
   * @return
   */
  private synchronized HHOOK getHook() {
    return hHook;
  }
  
  @Override
  public LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT lParam) {
    if(nCode >= 0) {
      // Is the component visible...
      if(relativeTo.isVisible() && relativeTo.isValid()) {
        // Did the event occur inside the component bounds...
        int absX = lParam.pt.x;
        int absY = lParam.pt.y;
        Point componentPoint = relativeTo.getLocationOnScreen();
        int relX = componentPoint.x;
        int relY = componentPoint.y;
        int relW = relX + relativeTo.getWidth();
        int relH = relY + relativeTo.getHeight();
        if(absX >= relX && absY >= relY && absX < relW && absY < relH) {
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
              
            default:
                break;
          }
        }
      }
    }
    return USER32_INSTANCE.CallNextHookEx(hHook, nCode, wParam, lParam.getPointer());
  }

  /**
   * Fire a mouse motion event.
   * 
   * @param eventType
   * @param button
   * @param lParam
   */
  private void fireMouseMotionEvent(int eventType, int button, MOUSEHOOKSTRUCT lParam) {
    MouseMotionListener[] listeners = listenerList.getListeners(MouseMotionListener.class);
    if(listeners.length > 0) {
      MouseEvent evt = createEvent(eventType, button, lParam);
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
   * Fire a mouse event.
   * 
   * @param eventType
   * @param button
   * @param lParam
   */
  private void fireMouseEvent(int eventType, int button, MOUSEHOOKSTRUCT lParam) {
    MouseListener[] listeners = listenerList.getListeners(MouseListener.class);
    if(listeners.length > 0) {
      MouseEvent evt = createEvent(eventType, button, lParam);
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
   * Create a new mouse event.
   * 
   * @param eventType
   * @param button
   * @param lParam
   * @return
   */
  private MouseEvent createEvent(int eventType, int button, MOUSEHOOKSTRUCT lParam) {
    POINT pt = lParam.pt;
    Point rl = relativeTo.getLocationOnScreen();
    int x = pt.x - rl.x;
    int y = pt.y - rl.y;
    return new MouseEvent(relativeTo, eventType, System.currentTimeMillis(), 0, x, y, pt.x, pt.y, 0, false, button);
  }
  
  /**
   * Message loop for the mouse hook.
   */
  private class MouseHookThread extends Thread {
    @Override
    public void run()  {
      try {
        hHook = USER32_INSTANCE.SetWindowsHookEx(User32.WH_MOUSE_LL, WindowsMouseHook.this, Kernel32.INSTANCE.GetModuleHandle(null), 0);
        MSG msg = new MSG();
        while((USER32_INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
          USER32_INSTANCE.TranslateMessage(msg);
          USER32_INSTANCE.DispatchMessage(msg);
          if(getHook() == null) {
            break;
          }
        }
      } 
      catch(Exception e) {
        e.printStackTrace();
      }
    }
  }
}
