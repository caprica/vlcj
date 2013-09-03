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

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.win32.W32APIOptions;

/**
 * Partial implementation of the Win32 User API.
 */
public interface ExtendedUser32 extends User32 {

    /**
     * Native library instance.
     */
    ExtendedUser32 INSTANCE = (ExtendedUser32)Native.loadLibrary("user32", ExtendedUser32.class, W32APIOptions.DEFAULT_OPTIONS);

    int SC_RESTORE = 0x0000f120;

    int WS_THICKFRAME = 0x00040000;
    int WS_CAPTION = 0x00c00000;

    int WS_EX_DLGMODALFRAME = 0x00000001;
    int WS_EX_WINDOWEDGE = 0x00000100;
    int WS_EX_CLIENTEDGE = 0x00000200;
    int WS_EX_STATICEDGE = 0x00020000;

    int SWP_NOZORDER = 0x0004;
    int SWP_NOACTIVATE = 0x0010;
    int SWP_FRAMECHANGED = 0x0020;

    DWORD MONITOR_DEFAULTTONEAREST = new DWORD(2);

    /**
     * Is the window zoomed (maximised) or not?
     *
     * @param hWnd native window handle
     * @return <code>true</code> if the window is zoomed; <code>false</code> if it is not
     */
    boolean IsZoomed(HWND hWnd);

    /**
     * Get a native monitor handle from a window handle.
     *
     * @param hWnd native window handle
     * @param dwFlags flags
     * @return native monitor handle
     */
    Pointer MonitorFromWindow(HWND hWnd, DWORD dwFlags);

    /**
     * Get native monitor information.
     *
     * @param hMonitor native monitor handle
     * @param lpMonitorInfo structure to receive monitor information
     * @return <code>true</code> on success; <code>false</code> otherwise
     */
    boolean GetMonitorInfoA(Pointer hMonitor, MONITORINFO lpMonitorInfo);

    /**
     * Send a message to a native window.
     *
     * @param hWnd native window handle
     * @param Msg message identifier
     * @param wParam message parameter
     * @param lParam message parameter
     * @return result
     */
    LRESULT SendMessage(HWND hWnd, int Msg, WPARAM wParam, LPARAM lParam);
}
