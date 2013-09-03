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

package uk.co.caprica.vlcj.player.embedded.windows;

import static com.sun.jna.platform.win32.WinUser.GWL_EXSTYLE;
import static com.sun.jna.platform.win32.WinUser.GWL_STYLE;
import static uk.co.caprica.vlcj.player.embedded.windows.ExtendedUser32.SWP_FRAMECHANGED;
import static uk.co.caprica.vlcj.player.embedded.windows.ExtendedUser32.SWP_NOACTIVATE;
import static uk.co.caprica.vlcj.player.embedded.windows.ExtendedUser32.SWP_NOZORDER;
import static uk.co.caprica.vlcj.player.embedded.windows.ExtendedUser32.WS_CAPTION;
import static uk.co.caprica.vlcj.player.embedded.windows.ExtendedUser32.WS_EX_CLIENTEDGE;
import static uk.co.caprica.vlcj.player.embedded.windows.ExtendedUser32.WS_EX_DLGMODALFRAME;
import static uk.co.caprica.vlcj.player.embedded.windows.ExtendedUser32.WS_EX_STATICEDGE;
import static uk.co.caprica.vlcj.player.embedded.windows.ExtendedUser32.WS_EX_WINDOWEDGE;
import static uk.co.caprica.vlcj.player.embedded.windows.ExtendedUser32.WS_THICKFRAME;

import java.awt.Window;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;

/**
 * Native full-screen implementation.
 * <p>
 * This is based on the full-screen implementation in Chromium.
 */
final class Win32FullScreenHandler {

    /**
     * Window to make full-screen.
     */
    private final Window window;

    /**
     * Current state of the window.
     */
    private WindowState windowState;

    /**
     * Create a native full-screen implementation.
     *
     * @param window window to make full-screen
     */
    Win32FullScreenHandler(Window window) {
        this.window = window;
    }

    /**
     * Set the full-screen state of the window.
     *
     * @param fullScreen <code>true</code> to set full-screen; <code>false</code> to exit full-screen
     */
    void setFullScreen(boolean fullScreen) {
        HWND hWnd = getHWND(Native.getComponentID(window));
        if(fullScreen) {
            windowState = getWindowState(hWnd);
            ExtendedUser32.INSTANCE.SetWindowLong(hWnd, GWL_STYLE, windowState.getStyle() & ~(WS_CAPTION | WS_THICKFRAME));
            ExtendedUser32.INSTANCE.SetWindowLong(hWnd, GWL_EXSTYLE, windowState.getExStyle() & ~(WS_EX_DLGMODALFRAME | WS_EX_WINDOWEDGE | WS_EX_CLIENTEDGE | WS_EX_STATICEDGE));
            MONITORINFO monitorInfo = getMonitorInfo(hWnd);
            RECT rect = monitorInfo.rcMonitor;
            ExtendedUser32.INSTANCE.SetWindowPos(hWnd, null, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, SWP_NOZORDER | SWP_NOACTIVATE | SWP_FRAMECHANGED);
        }
        else {
            ExtendedUser32.INSTANCE.SetWindowLong(hWnd, GWL_STYLE, windowState.getStyle());
            ExtendedUser32.INSTANCE.SetWindowLong(hWnd, GWL_EXSTYLE, windowState.getExStyle());
            ExtendedUser32.INSTANCE.SetWindowPos(hWnd, null, windowState.getLeft(), windowState.getTop(), windowState.getRight() - windowState.getLeft(), windowState.getBottom() - windowState.getTop(), SWP_NOZORDER | SWP_NOACTIVATE | SWP_FRAMECHANGED);
            if(windowState.getMaximized()) {
                ExtendedUser32.INSTANCE.SendMessage(hWnd, User32.WM_SYSCOMMAND, new WPARAM(WinUser.SC_MAXIMIZE), new LPARAM(0));
            }
        }
    }

    /**
     * Get a window handle for a component identifier.
     *
     * @param componentId component identifier
     * @return native window handle
     */
    private HWND getHWND(long componentId) {
        return new HWND(Pointer.createConstant(componentId));
    }

    /**
     * Get the current state of a window.
     *
     * @param hWnd native window handle
     * @return window state
     */
    private WindowState getWindowState(HWND hWnd) {
        WindowState windowState = new WindowState();
        windowState.setMaximized(ExtendedUser32.INSTANCE.IsZoomed(hWnd));
        if(windowState.getMaximized()) {
            ExtendedUser32.INSTANCE.SendMessage(hWnd, User32.WM_SYSCOMMAND, new WPARAM(ExtendedUser32.SC_RESTORE), new LPARAM(0));
        }
        windowState.setStyle(ExtendedUser32.INSTANCE.GetWindowLong(hWnd, ExtendedUser32.GWL_STYLE));
        windowState.setExStyle(ExtendedUser32.INSTANCE.GetWindowLong(hWnd, ExtendedUser32.GWL_EXSTYLE));
        RECT rect = new RECT();
        boolean gotWindowRect = ExtendedUser32.INSTANCE.GetWindowRect(hWnd, rect);
        if(gotWindowRect) {
            windowState.setLeft(rect.left);
            windowState.setTop(rect.top);
            windowState.setRight(rect.right);
            windowState.setBottom(rect.bottom);
        }
        return windowState;
    }

    /**
     * Get monitor information.
     *
     * @param hWnd native window handle
     * @return native monitor information
     */
    private MONITORINFO getMonitorInfo(HWND hWnd) {
        Pointer hMonitor = ExtendedUser32.INSTANCE.MonitorFromWindow(hWnd, ExtendedUser32.MONITOR_DEFAULTTONEAREST);
        MONITORINFO monitorInfo = new MONITORINFO();
        ExtendedUser32.INSTANCE.GetMonitorInfoA(hMonitor, monitorInfo);
        return monitorInfo;
    }
}
