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

package uk.co.caprica.vlcj.player.embedded;

import uk.co.caprica.vlcj.player.embedded.videosurface.ComponentVideoSurface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Behaviour pertaining to the heavyweight overlay.
 * <p>
 * Overlay is only support if the video surface associated with the media player is a {@link ComponentVideoSurface}.
 */
public final class OverlayApi extends BaseApi {

    /**
     * Listener implementation used to keep the overlay position and size in sync with the video
     * surface.
     */
    private final OverlayComponentAdapter overlayComponentAdapter = new OverlayComponentAdapter();

    /**
     * Listener implementation used to keep the overlay visibility state in sync with the video
     * surface.
     */
    private final OverlayWindowAdapter overlayWindowAdapter = new OverlayWindowAdapter();

    /**
     * Reusable rectangle to determine the overlay bounds.
     */
    private final Rectangle bounds = new Rectangle();

    /**
     * Optional overlay component.
     */
    private Window overlay;

    /**
     * Track the requested overlay enabled/disabled state so it can be restored when needed.
     */
    private boolean requestedOverlay;

    /**
     * Track whether or not the overlay should be restored when the video surface is shown/hidden.
     */
    private boolean restoreOverlay;

    OverlayApi(EmbeddedMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the overlay component.
     *
     * @return overlay component, may be <code>null</code>
     */
    public Window get() {
        return overlay;
    }

    /**
     * Set a new overlay component.
     * <p>
     * The existing overlay if there is one will be disabled.
     * <p>
     * The new overlay will <strong>not</strong> automatically be enabled.
     * <p>
     * The overlay should be a sub-class of <code>Window</code> or <code>JWindow</code>. If your overlay contains
     * dynamically updated content such as a timer or animated graphics, then you should use <code>JWindow</code> so
     * that your updates will be double-buffered and there will be no tearing or flickering when you paint the overlay.
     * If you do this, you must take care to erase the overlay background before you paint it.
     * <p>
     * <strong>When the overlay is no longer needed it is your responsibility to {@link Window#dispose()} it - if you do
     * not do this you may leak resources. If you set multiple different overlays you must remember to dispose the old
     * overlay.</strong>
     * <p>
     * It is recommended to set the overlay once as early as possible in your application.
     *
     * @param overlay overlay component, may be <code>null</code>
     */
    public void set(Window overlay) {
        if (mediaPlayer.videoSurface().getVideoSurface() instanceof ComponentVideoSurface) {
            // Disable the current overlay if there is one
            enable(false);
            // Remove the existing overlay if there is one
            removeOverlay();
            // Add the new overlay, but do not enable it
            addOverlay(overlay);
        }
        else {
            throw new IllegalStateException("Overlay requires a ComponentVideoSurface");
        }
    }

    /**
     * Enable/disable the overlay component if there is one.
     *
     * @param enable whether to enable the overlay or disable it
     */
    public void enable(boolean enable) {
        requestedOverlay = enable;
        if (overlay != null) {
            if (enable) {
                if (!overlay.isVisible()) {
                    Component component = getComponent();
                    component.getBounds(bounds);
                    bounds.setLocation(component.getLocationOnScreen());
                    overlay.setBounds(bounds);
                    Window window = getAncestorWindow(component);
                    window.addComponentListener(overlayComponentAdapter);
                    overlay.setVisible(true);
                }
            }
            else {
                if (overlay.isVisible()) {
                    overlay.setVisible(false);
                    Window window = getAncestorWindow(getComponent());
                    window.removeComponentListener(overlayComponentAdapter);
                }
            }
        }
    }

    /**
     * Check whether or not there is an overlay component currently enabled.
     *
     * @return true if there is an overlay enabled, otherwise false
     */
    public boolean enabled() {
        return overlay != null && overlay.isVisible();
    }

    /**
     * Install an overlay component.
     *
     * @param overlay overlay window
     */
    private void addOverlay(Window overlay) {
        if (overlay != null) {
            this.overlay = overlay;
            Window window = getAncestorWindow(getComponent());
            if (window != null) {
                window.addWindowListener(overlayWindowAdapter);
            }
            else {
                // This should not be possible
            }
        }
    }

    /**
     * Remove the overlay component.
     */
    private void removeOverlay() {
        if (overlay != null) {
            Window window = getAncestorWindow(getComponent());
            window.removeWindowListener(overlayWindowAdapter);
            overlay = null;
        }
    }

    /**
     * Component event listener to keep the overlay component in sync with the video surface component.
     * <p>
     * This adapter will only be used if there is a valid component-based video surface, so we can forego some checks.
     */
    private final class OverlayComponentAdapter extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            overlay.setSize(getComponent().getSize());
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            overlay.setLocation(getComponent().getLocationOnScreen());
        }

        @Override
        public void componentShown(ComponentEvent e) {
            showOverlay();
        }

        @Override
        public void componentHidden(ComponentEvent e) {
            hideOverlay();
        }
    }

    /**
     * Window event listener to hide the overlay when the video window is hidden, and vice versa.
     */
    private final class OverlayWindowAdapter extends WindowAdapter {

        @Override
        public void windowIconified(WindowEvent e) {
            // Nothing, this is taken care of by "windowDeactivated"
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            showOverlay();
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            hideOverlay();
        }

        @Override
        public void windowActivated(WindowEvent e) {
            showOverlay();
        }
    }

    /**
     * Make the overlay visible.
     */
    private void showOverlay() {
        if (restoreOverlay) {
            enable(true);
        }
    }

    /**
     * Hide the overlay.
     */
    private void hideOverlay() {
        if (requestedOverlay) {
            restoreOverlay = true;
            enable(false);
        }
        else {
            restoreOverlay = false;
        }
    }

    /**
     * Get the video surface component.
     * <p>
     * This method will only be used if the video surface is a {@link ComponentVideoSurface} so we can forgo some
     * checks.
     *
     * @return
     */
    private Component getComponent() {
        return ((ComponentVideoSurface) mediaPlayer.videoSurface().getVideoSurface()).component();
    }

    /**
     * Find the Window ancestor for a component.
     * <p>
     * This method will only be used if the video surface is a {@link ComponentVideoSurface} so we can forgo some
     * checks.
     *
     * @param component component to find the ancestor for
     * @return ancestor window if found, or <code>null</code>
     */
    private Window getAncestorWindow(Component component) {
        return (Window)SwingUtilities.getAncestorOfClass(Window.class, component);
    }

}
