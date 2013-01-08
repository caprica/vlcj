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

package uk.co.caprica.vlcj.player.embedded;

import java.awt.Window;
import java.awt.image.BufferedImage;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 * Specification for a media player component that is intended to be embedded in a user-interface
 * component.
 */
public interface EmbeddedMediaPlayer extends MediaPlayer {

    /**
     * Set the component used to render video.
     * <p>
     * Setting the video surface on the native component is actually deferred so the component used
     * as the video surface need <em>not</em> be visible and fully realised before calling this
     * method.
     * <p>
     * The video surface will not be associated with the native media player until the media is
     * played.
     * <p>
     * It is possible to change the video surface after it has been set, but the change will not
     * take effect until the media is played.
     *
     * @param videoSurface component to render video to
     */
    void setVideoSurface(CanvasVideoSurface videoSurface);

    /**
     * Ensure that the video surface has been associated with the native media player.
     * <p>
     * Ordinarily when setting the video surface the actual association of the video surface with
     * the native media player is deferred until the first time media is played.
     * <p>
     * This deferring behaviour is usually a good thing because when setting a video surface
     * component on the native media player the video surface component must be a displayable
     * component and this is often not the case during the construction and initialisation of the
     * application.
     * <p>
     * Most applications will not need to call this method.
     * <p>
     * However, in special circumstances such as associating an embedded media player with a media
     * list player, media is played through the media list rather than the media player itself so
     * the deferred association of the video surface would never happen.
     * <p>
     * Calling this method ensures that the video surface is properly associated with the native
     * media player and consequently the video surface component must be visible when this method is
     * called.
     */
    void attachVideoSurface();

    /**
     * Toggle whether the video display is in full-screen or not.
     * <p>
     * Setting the display into or out of full-screen mode is delegate to the
     * {@link FullScreenStrategy} that was used when the media player was created.
     */
    void toggleFullScreen();

    /**
     * Set full-screen mode.
     * <p>
     * Setting the display into or out of full-screen mode is delegate to the
     * {@link FullScreenStrategy} that was used when the media player was created.
     *
     * @param fullScreen true for full-screen, otherwise false
     */
    void setFullScreen(boolean fullScreen);

    /**
     * Check whether the full-screen mode is currently active or not.
     * <p>
     * Testing whether or not the display is in full-screen mode is delegate to the
     * {@link FullScreenStrategy} that was used when the media player was created.
     *
     * @return true if full-screen is active, otherwise false
     */
    boolean isFullScreen();

    /**
     * Get the contents of the video surface component.
     * <p>
     * The implementation is expected to use the AWT Robot class to capture the contents of the
     * video surface component (there is no other way).
     * <p>
     * The size of the returned image will match the current size of the video surface.
     * <p>
     * <strong>Since this implementation will use the AWT Robot class to make a screen capture, care
     * must be taken when invoking this method to ensure that nothing else is overlaying the video
     * surface!</strong>
     *
     * @return current contents of the video surface
     */
    BufferedImage getVideoSurfaceContents();

    /**
     * Get the overlay component.
     *
     * @return overlay component, may be <code>null</code>
     */
    Window getOverlay();

    /**
     * Set a new overlay component.
     * <p>
     * The existing overlay if there is one will be disabled.
     * <p>
     * The new overlay will <strong>not</strong> automatically be enabled.
     * <p>
     * The overlay should be a sub-class of <code>Window</code> or <code>JWindow</code>. If your
     * overlay contains dynamically updated content such as a timer or animated graphics, then you
     * should use <code>JWindow</code> so that your updates will be double-buffered and there will
     * be no tearing or flickering when you paint the overlay. If you do this, you must take care to
     * erase the overlay background before you paint it.
     *
     * @param overlay overlay component, may be <code>null</code>
     */
    void setOverlay(Window overlay);

    /**
     * Enable/disable the overlay component if there is one.
     *
     * @param enable whether to enable the overlay or disable it
     */
    void enableOverlay(boolean enable);

    /**
     * Check whether or not there is an overlay component currently enabled.
     *
     * @return true if there is an overlay enabled, otherwise false
     */
    boolean overlayEnabled();

    /**
     * Set whether or not to enable native media player mouse input handling.
     *
     * @param enable <code>true</code> to enable, <code>false</code> to disable
     */
    void setEnableMouseInputHandling(boolean enable);

    /**
     * Set whether or not to enable native media player keyboard input handling.
     *
     * @param enable <code>true</code> to enable, <code>false</code> to disable
     */
    void setEnableKeyInputHandling(boolean enable);
}
