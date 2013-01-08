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

import uk.co.caprica.vlcj.logger.Logger;

/**
 * Implementation of a Canvas that uses a native windows message hook to make sure events are
 * received while the video is playing.
 * <p>
 * When VLC plays a movie file, it does not send keyboard or mouse events to the Canvas component
 * used as the video surface.
 * <p>
 * To work around this requires two strategies.
 * <p>
 * For keyboard events add a global {@link AWTEventListener}.
 * <p>
 * For mouse events register a global Windows message hook.
 * <p>
 * This component implements both of those strategies behind the scenes - as far as client code is
 * concerned key and mouse listeners are added in the usual way.
 * <p>
 * It is the responsibility of the client application to {@link #release()} this component
 * when it is no longer needed.
 * <p>
 * <strong>This class is experimental, unsupported and unstable in operation.</strong>
 */
public class WindowsCanvas extends Canvas {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

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
        Logger.debug("WindowsCanvas()");
        Logger.warn("You are using the WindowsCanvas implementation, this may cause spurious random VM crashes when you shut down your application");
        mouseHook = new WindowsMouseHook(this);
        Logger.debug("mouseHook={}", mouseHook);
        mouseHook.start();
        Toolkit.getDefaultToolkit().addAWTEventListener(new WindowsKeyListener(), AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * Release the canvas resources.
     */
    public void release() {
        Logger.debug("release()");
        if(mouseHook != null) {
            mouseHook.release();
            mouseHook = null;
        }
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        Logger.debug("addMouseListener(l={})", l);
        mouseHook.addMouseListener(l);
    }

    @Override
    public synchronized void removeMouseListener(MouseListener l) {
        Logger.debug("removeMouseListener(l={})", l);
        mouseHook.removeMouseListener(l);
    }

    @Override
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        Logger.debug("addMouseMotionListener(l={})", l);
        mouseHook.addMouseMotionListener(l);
    }

    @Override
    public synchronized void removeMouseMotionListener(MouseMotionListener l) {
        Logger.debug("removeMouseMotionListener(l={})", l);
        mouseHook.removeMouseMotionListener(l);
    }

    @Override
    public synchronized void addMouseWheelListener(MouseWheelListener l) {
        Logger.debug("addMouseWheelListener(l={})", l);
        mouseHook.addMouseWheelListener(l);
    }

    @Override
    public synchronized void removeMouseWheelListener(MouseWheelListener l) {
        Logger.debug("removeMouseWheelListener(l={})", l);
        mouseHook.removeMouseWheelListener(l);
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        Logger.debug("addKeyListener(l={})", l);
        listenerList.add(KeyListener.class, l);
    }

    @Override
    public synchronized void removeKeyListener(KeyListener l) {
        Logger.debug("removeKeyListener(l={})", l);
        listenerList.remove(KeyListener.class, l);
    }

    /**
     * Global AWT event listener used to propagate key events to listeners registered in the usual
     * way.
     */
    private final class WindowsKeyListener implements AWTEventListener {

        @Override
        public void eventDispatched(AWTEvent event) {
            Logger.trace("eventDispatched(event={})", event);

            // Only interested in key events...
            if(event instanceof KeyEvent) {
                KeyEvent keyEvent = (KeyEvent)event;
                // Only interested in events for this component...
                if(keyEvent.getComponent() == WindowsCanvas.this) {
                    // Propagate the event to each registered listener...
                    KeyListener[] listeners = listenerList.getListeners(KeyListener.class);
                    for(int i = listeners.length - 1; i >= 0; i -- ) {
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
