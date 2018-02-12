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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.videosurface;

import java.awt.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Native;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Encapsulation of a video surface that uses an AWT Window.
 * This is only useful in MacOSX, do not use in other platforms.
 * The window is to be show stand alone but not embedded.
 * This is targeted at mac because Oracle's JDK do not have heavy weight AWT components.
 * And using {@link #CanvasVideoSurface} may cause JVM crashing.
 * This class provide a tricky way of getting AWT window's native id,
 * so passing it to vlc-core will work.
 */
public class WindowVideoSurface extends VideoSurface {

	/**
	 * Log.
	 */
	private final Logger logger = LoggerFactory.getLogger(WindowVideoSurface.class);

	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Video surface component.
	 */
	private final Window window;

	/**
	 * Create a new video surface.
	 * The window must be visible.
	 *
	 * @param window video surface component
	 * @param videoSurfaceAdapter adapter to attach a video surface to a native media player
	 */
	public WindowVideoSurface(Window window, VideoSurfaceAdapter videoSurfaceAdapter) {
		super(videoSurfaceAdapter);
		this.window = window;
	}

	/**
	 * Get the awt window object
	 * @return
	 */
	public Window getWindow() {
		return window;
	}

	@Override
	public void attach(LibVlc libvlc, MediaPlayer mediaPlayer) {
		logger.debug("attach()");
		if(window.isDisplayable()) {
			long windowId = -1;
			if (RuntimeUtil.isMac()) {
				windowId = getNSWindowViewPtr(window);
			}else {
				windowId = Native.getWindowID(window);
			}
			logger.debug("windowId={}", windowId);
			videoSurfaceAdapter.attach(libvlc, mediaPlayer, windowId);
			logger.debug("video surface attached");
		}
		else {
			throw new IllegalStateException("The video surface component must be displayable");
		}
	}

	/**
	 * Get native window ID in MacOSX
	 * Not useful for other platforms.
	 * @param window
	 * @return -1 when fails
	 */
	public static long getNSWindowViewPtr(Window window) {
		try {
			Object componentPeer = window.getClass().getMethod("getPeer").invoke(window);
			Object platformWindow = componentPeer.getClass().getMethod("getPlatformWindow").invoke(componentPeer);
			Object contentView = platformWindow.getClass().getMethod("getContentView").invoke(platformWindow);
			return (Long) contentView.getClass().getMethod("getAWTView").invoke(contentView);
		} catch (Exception exception) {
			exception.printStackTrace();
			return -1;
		}
	}

}
