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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
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

import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.runtime.windows.internal.LowLevelMouseProc;
import uk.co.caprica.vlcj.runtime.windows.internal.MSLLHOOKSTRUCT;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.platform.win32.WinUser.POINT;

/**
 * Windows Mouse Hook implementation.
 * <p>
 * This implementation registers a Windows hook and translates the native mouse messages into Java
 * mouse/mouse-motion events.
 * <p>
 * Not all events are supported, the events that are supported are:
 * <ul>
 * <li>Mouse Moved</li>
 * <li>Mouse Pressed</li>
 * <li>Mouse Released</li>
 * <li>Mouse Wheel Moved</li>
 * <li>Mouse Entered</li>
 * <li>Mouse Exited</li>
 * </ul>
 * <strong>Any listeners added must execute and return quickly since they will be executing as part
 * of the native event queue.</strong>
 * <p>
 * There are several deficiencies that could be addressed:
 * <ul>
 * <li>Modifiers are not passed along with the event</li>
 * <li>The semantic events like DRAGGED and CLICKED are not implemented</li>
 * <li>Perhaps the events should be notified asynchronously via an executor so as not to hold up the
 * hook</li>
 * <li>The thread-safetiness issues are unclear especially wrt creating the hook in the thread</li>
 * <li>There are random fatal crashes during release()</li>
 * </ul>
 * The hook must be started after it has been created.
 * <p>
 * <strong>This class is experimental, unsupported and unstable in operation.</strong>
 * <p>
 * FIXME: There is probably a whole bunch more synchronisation should be going on in here, for
 * example during release() the listeners should be removed and the call-back should be protected.
 */
public class WindowsMouseHook implements LowLevelMouseProc {

    /**
     * Native library instance.
     */
    private static final User32 USER32_INSTANCE = User32.INSTANCE;

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
     * True if the mouse entered the component (rectangle), otherwise false.
     */
    private volatile boolean mouseEntered;

    /**
     * Create a new mouse hook.
     *
     * @param relativeTo component to report mouse coordinates relative to
     */
    public WindowsMouseHook(Component relativeTo) {
        Logger.debug("WindowsMouseHook(relativeTo={})", relativeTo);
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
        Logger.debug("addMouseListener(listener={})", listener);
        listenerList.add(MouseListener.class, listener);
    }

    /**
     *
     *
     * @param listener
     */
    public void removeMouseListener(MouseListener listener) {
        Logger.debug("removeMouseListener(listener={})", listener);
        listenerList.remove(MouseListener.class, listener);
    }

    /**
     *
     *
     * @param listener
     */
    public void addMouseMotionListener(MouseMotionListener listener) {
        Logger.debug("addMouseMotionListener(listener={})", listener);
        listenerList.add(MouseMotionListener.class, listener);
    }

    /**
     *
     *
     * @param listener
     */
    public void removeMouseMotionListener(MouseMotionListener listener) {
        Logger.debug("removeMouseMotionListener(listener={})", listener);
        listenerList.remove(MouseMotionListener.class, listener);
    }

    /**
     *
     *
     * @param listener
     */
    public void addMouseWheelListener(MouseWheelListener listener) {
        Logger.debug("addMouseWheelListener(listener={})", listener);
        listenerList.add(MouseWheelListener.class, listener);
    }

    /**
     *
     *
     * @param listener
     */
    public void removeMouseWheelListener(MouseWheelListener listener) {
        Logger.debug("removeMouseWheelListener(listener={})", listener);
        listenerList.remove(MouseWheelListener.class, listener);
    }

    /**
     * Start the hook.
     */
    public void start() {
        Logger.debug("start()");
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
        Logger.debug("release()");
        HHOOK hook = getHook();
        if(hook != null) {
            USER32_INSTANCE.UnhookWindowsHookEx(hHook);
            hook = null;
            // TODO ordinarily I'd interrupt the thread to force it to exit if it's
            // blocked, but in this case a fatal VM failure would occur
            // hookThread.interrupt();
        }
        Logger.debug("released");
    }

    /**
     *
     *
     * @return hook handle
     */
    private synchronized HHOOK getHook() {
        return hHook;
    }

    @Override
    public LRESULT callback(int nCode, WPARAM wParam, MSLLHOOKSTRUCT lParam) {
        Logger.trace("callback(nCode={},wParam={},lParam={})", nCode, wParam, lParam);
        if(nCode >= 0) {
            Window window = SwingUtilities.getWindowAncestor(relativeTo);
            Logger.trace("window={}", window);
            // Is the window active...
            if(window != null && window.isActive()) {
                Logger.trace("window is active");
                // Is the component showing...
                // TODO is this still needed or is isActive good enough?
                if(relativeTo.isShowing() && relativeTo.isValid()) {
                    Logger.trace("window is visible");
                    // Did the event occur inside the component bounds...
                    int absX = lParam.pt.x;
                    int absY = lParam.pt.y;
                    // FIXME there is a race here where relativeTo may no longer be visible, should
                    // I lock the component tree - is that OK from non-EDT?
                    Point componentPoint = relativeTo.getLocationOnScreen();
                    int relX = componentPoint.x;
                    int relY = componentPoint.y;
                    int relW = relX + relativeTo.getWidth();
                    int relH = relY + relativeTo.getHeight();
                    if(absX >= relX && absY >= relY && absX < relW && absY < relH) {
                        Logger.trace("event inside component bounds");
                        if(!this.mouseEntered) {
                            this.mouseEntered = true;
                            fireMouseEvent(MouseEvent.MOUSE_ENTERED, MouseEvent.NOBUTTON, lParam);
                        }
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
                    else {
                        Logger.trace("event outside component bounds");

                        if(this.mouseEntered) {
                            this.mouseEntered = false;
                            fireMouseEvent(MouseEvent.MOUSE_EXITED, MouseEvent.NOBUTTON, lParam);
                        }
                    }
                }
            }
        }
        return USER32_INSTANCE.CallNextHookEx(hHook, nCode, wParam, lParam.getPointer());
    }

    /**
     * Fire a mouse motion event to the registered listeners.
     *
     * @param eventType
     * @param button
     * @param lParam
     */
    private void fireMouseMotionEvent(int eventType, int button, MSLLHOOKSTRUCT lParam) {
        Logger.trace("fireMouseMotionEvent(eventType={},button={},lParam={})", eventType, button, lParam);
        MouseMotionListener[] listeners = listenerList.getListeners(MouseMotionListener.class);
        if(listeners.length > 0) {
            MouseEvent evt = createMouseEvent(eventType, button, lParam);
            for(int i = listeners.length - 1; i >= 0; i -- ) {
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
        Logger.trace("fireMouseEvent(eventType={},button={},lParam={})", eventType, button, lParam);
        MouseListener[] listeners = listenerList.getListeners(MouseListener.class);
        if(listeners.length > 0) {
            MouseEvent evt = createMouseEvent(eventType, button, lParam);
            for(int i = listeners.length - 1; i >= 0; i -- ) {
                switch(eventType) {
                    case MouseEvent.MOUSE_PRESSED:
                        listeners[i].mousePressed(evt);
                        break;

                    case MouseEvent.MOUSE_RELEASED:
                        listeners[i].mouseReleased(evt);
                        break;

                    case MouseEvent.MOUSE_ENTERED:
                        listeners[i].mouseEntered(evt);
                        break;

                    case MouseEvent.MOUSE_EXITED:
                        listeners[i].mouseExited(evt);
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
        Logger.trace("fireMouseWheelEvent(eventType={},lParam={})", eventType, lParam);
        MouseWheelListener[] listeners = listenerList.getListeners(MouseWheelListener.class);
        if(listeners.length > 0) {
            MouseWheelEvent evt = createMouseWheelEvent(eventType, lParam);
            for(int i = listeners.length - 1; i >= 0; i -- ) {
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
     * @return mouse event
     */
    private MouseEvent createMouseEvent(int eventType, int button, MSLLHOOKSTRUCT lParam) {
        POINT pt = lParam.pt;
        // FIXME race condition where relativeTo might not be visible
        Point rl = relativeTo.getLocationOnScreen();
        int x = pt.x - rl.x;
        int y = pt.y - rl.y;
        return new MouseEvent(relativeTo, eventType, lParam.time.longValue(), 0, x, y, 0, false, button);
    }

    /**
     * Create a new mouse wheel event.
     *
     * In Windows the rotation amount is positive when moving the wheel away from the user whereas
     * in Java the rotation amount is negative when moving the wheel away from the user.
     * <p>
     * This implementation adjusts the sign so that the value is correct for Java.
     *
     * @param eventType
     * @param lParam
     * @return mouse wheel event
     */
    private MouseWheelEvent createMouseWheelEvent(int eventType, MSLLHOOKSTRUCT lParam) {
        POINT pt = lParam.pt;
        // FIXME race condition where relativeTo might not be visible
        Point rl = relativeTo.getLocationOnScreen();
        int x = pt.x - rl.x;
        int y = pt.y - rl.y;
        int wheelRotation = lParam.mouseData.intValue() >> 16;
        return new MouseWheelEvent(relativeTo, eventType, lParam.time.longValue(), 0, x, y, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, wheelRotation * -1);
    }

    /**
     * Message loop for the mouse hook.
     */
    private class MouseHookThread extends Thread {
        @Override
        public void run() {
            Logger.debug("run()");
            try {
                hHook = USER32_INSTANCE.SetWindowsHookEx(WinUser.WH_MOUSE_LL, WindowsMouseHook.this, Kernel32.INSTANCE.GetModuleHandle(null), 0);
                MSG msg = new MSG();
                while((USER32_INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
                    // This code is never reached
                    USER32_INSTANCE.TranslateMessage(msg);
                    USER32_INSTANCE.DispatchMessage(msg);
                    if(getHook() == null) {
                        break;
                    }
                }
            }
            catch(Exception e) {
                Logger.error("Mouse hook failure", e);
            }

            Logger.debug("mouse hook runnable exits");
        }
    }
}
