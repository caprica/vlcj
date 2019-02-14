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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.fullscreen.x;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.platform.unix.X11;

import java.awt.*;

/**
 * Helper class to encapsulate native X Windows full-screen functionality.
 */
final class XFullScreenHandler {

    private static final int _NET_WM_STATE_REMOVE = 0;

    private static final int _NET_WM_STATE_ADD = 1;

    private static final int TRUE = 1;

    /**
     * Ask the window manager to make a window full-screen.
     * <p>
     * This method sends a low-level event to an X window to request that the window be made 'real'
     * full-screen - i.e. the window will be sized to fill the entire screen bounds, and will appear
     * <em>above</em> any window manager screen furniture such as panels and menus.
     * <p>
     * This method should only be called on platforms where X is supported.
     * <p>
     * The implementation makes use of the JNA X11 platform binding.
     *
     * @param w window to make full-screen
     * @param fullScreen <code>true</code> to make the window full-screen; <code>false</code> to restore the window to it's original size and position
     * @return <code>true</code> if the message was successfully sent to the window; <code>false</code> otherwise
     */
    static boolean setFullScreenWindow(Window w, boolean fullScreen) {
        // Use the JNA platform X11 binding
        X11 x = X11.INSTANCE;
        X11.Display display = null;
        try {
            // Open the display
            display = x.XOpenDisplay(null);
            // Send the message
            int result = sendClientMessage(display, Native.getWindowID(w), "_NET_WM_STATE", new NativeLong(fullScreen ? _NET_WM_STATE_ADD : _NET_WM_STATE_REMOVE), x.XInternAtom(display, "_NET_WM_STATE_FULLSCREEN", false));
            return result != 0;
        }
        finally {
            if(display != null) {
                // Close the display
                x.XCloseDisplay(display);
            }
        }
    }

    /**
     * Helper method to send a client message to an X window.
     *
     * @param display display
     * @param wid native window identifier
     * @param msg type of message to send
     * @param data0 message data
     * @param data1 message data
     * @return <code>1</code> if the message was successfully sent to the window; <code>0</code> otherwise
     */
    private static int sendClientMessage(X11.Display display, long wid, String msg, NativeLong data0, NativeLong data1) {
        // Use the JNA platform X11 binding
        X11 x = X11.INSTANCE;
        // Create and populate a client-event structure
        X11.XEvent event = new X11.XEvent();
        event.type = X11.ClientMessage;
        // Select the proper union structure for the event type and populate it
        event.setType(X11.XClientMessageEvent.class);
        event.xclient.type = X11.ClientMessage;
        event.xclient.serial = new NativeLong(0L);
        event.xclient.send_event = TRUE;
        event.xclient.message_type = x.XInternAtom(display, msg, false);
        event.xclient.window = new com.sun.jna.platform.unix.X11.Window(wid);
        event.xclient.format = 32;
        // Select the proper union structure for the event data and populate it
        event.xclient.data.setType(NativeLong[].class);
        event.xclient.data.l[0] = data0;
        event.xclient.data.l[1] = data1;
        event.xclient.data.l[2] = new NativeLong(0L);
        event.xclient.data.l[3] = new NativeLong(0L);
        event.xclient.data.l[4] = new NativeLong(0L);
        // Send the event
        NativeLong mask = new NativeLong(X11.SubstructureRedirectMask | X11.SubstructureNotifyMask);
        int result = x.XSendEvent(display, x.XDefaultRootWindow(display), 0, mask, event);
        // Flush, since we're not processing an X event loop
        x.XFlush(display);
        // Finally, return the result of sending the event
        return result;
    }

    private XFullScreenHandler() {
    }

}
