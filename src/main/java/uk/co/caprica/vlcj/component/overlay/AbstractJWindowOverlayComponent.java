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

package uk.co.caprica.vlcj.component.overlay;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;

import javax.swing.JWindow;

import uk.co.caprica.vlcj.logger.Logger;

import com.sun.jna.platform.WindowUtils;

/**
 * Base implementation for a video overlay component.
 * <p>
 * Overlay contents may be implemented by using Swing/AWT components in a layout using
 * {@link #onCreateOverlay()}, and/or by implementing custom painting using
 * {@link #onNewSize(int, int)} and {@link #onPaintOverlay(Graphics2D)}.
 * <p>
 * Various template method are provided for sub-classes to provide the required behaviour.
 * <p>
 * The default implementation of the {@link #onSetWindowTransparency()} template method attempts to
 * make a transparent overlay by either the preferred method of simply setting the background colour
 * to a transparent colour for Java7, or by using <code>com.sun.awt.AWTUtilities</code>, if it
 * exists, for Java6.
 * <p>
 * Best results will be obtained by <em>disabling</em> any compositing desktop window manager.
 */
public abstract class AbstractJWindowOverlayComponent extends JWindow {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Current layout width.
     */
    private int layoutWidth = -1;

    /**
     * Current layout height.
     */
    private int layoutHeight = -1;

    /**
     * Create an overlay.
     * <p>
     * An "alpha-compatible" graphics configuration will be used when creating the window so as to
     * enable a transparent overlay.
     *
     * @param owner owning window, must not be <code>null</code>
     * @throws IllegalArgumentException if the <code>owner</code> parameter is <code>null</code>
     */
    public AbstractJWindowOverlayComponent(Window owner) {
        this(owner, WindowUtils.getAlphaCompatibleGraphicsConfiguration());
    }

    /**
     * Create an overlay.
     *
     * @param owner owning window, must not be <code>null</code>
     * @param graphicsConfiguration graphics configuration to use when creating the <code>Window</code>
     * @throws IllegalArgumentException if the <code>owner</code> parameter is <code>null</code>
     */
    public AbstractJWindowOverlayComponent(Window owner, GraphicsConfiguration graphicsConfiguration) {
        super(owner, graphicsConfiguration);
        if(owner == null) {
            throw new IllegalArgumentException("The overlay window owner must not be null");
        }
        onSetWindowTransparency();
        onCreateOverlay();
        if(onHideCursor()) {
            setCursor(getBlankCursor());
        }
    }

    /**
     * Make the overlay window transparent.
     */
    protected void onSetWindowTransparency() {
        String javaSpecificationVersion = System.getProperty("java.specification.version");
        // If Java7 or later...
        if("1.7".compareTo(javaSpecificationVersion) <= 0) {
            // ...simply set the background colour to a fully transparent colour
            setBackground(new Color(0, 0, 0, 0));
        }
        // Otherwise earlier than Java7...
        else {
            // ...try AWTUtilities if the class can be found...
            try {
                Class<?> awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
                Method setWindowOpaqueMethod = awtUtilitiesClass.getMethod("setWindowOpaque", Window.class, Boolean.class);
                setWindowOpaqueMethod.invoke(null, this, false);
            }
            catch(Exception e) {
                Logger.debug("No apparent support for transparent windows", e.getMessage());
                // Fall-back, this is the best that can be done
                setBackground(new Color(0, 0, 0, 0));
            }
        }
    }

    /**
     * Template method invoked when the overlay is created for the first time.
     * <p>
     * Implementing classes may override this method to, for example, set a layout and add
     * components to the overlay.
     * <p>
     * Implementing classes may opt to create the overlay in the constructor instead.
     */
    protected void onCreateOverlay() {
        // Default implementation does nothing.
    }

    /**
     * Template method to determine whether or not the mouse pointer should be hidden when the
     * overlay is active.
     * <p>
     * The default behaviour is to hide the mouse pointer.
     *
     * @return <code>true</code> to hide the mouse pointer; otherwise <code>false</code>
     */
    protected boolean onHideCursor() {
        return true;
    }

    @Override
    public final void paint(Graphics g) {
        // The use of the non-short-circuit logical '|' operator here is intentional
        if(layoutWidth != (layoutWidth = getWidth()) | layoutHeight != (layoutHeight = getHeight())) {
            onNewSize(layoutWidth, layoutHeight);
        }
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        onPrepareGraphicsContext(g2);
        onPaintOverlay(g2);
    }

    /**
     * Template method invoked when the overlay size changes.
     *
     * @param width new width
     * @param height new height
     */
    protected void onNewSize(int width, int height) {
        // Default implementation does nothing.
    }

    /**
     * Template method used to configure the graphics context before painting the overlay.
     * <p>
     * The default implementation enables anti-aliasing.
     *
     * @param g2 graphics context
     */
    protected void onPrepareGraphicsContext(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    /**
     * Template method used to paint the overlay.
     *
     * @param g2 graphics context
     */
    protected void onPaintOverlay(Graphics2D g2) {
        // Default implementation does nothing
    }

    /**
     * Get a blank cursor to use for the mouse pointer.
     *
     * @return blank cursor
     */
    private Cursor getBlankCursor() {
        Image blankImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        return Toolkit.getDefaultToolkit().createCustomCursor(blankImage, new Point(0, 0), "");
    }
}
