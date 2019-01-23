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

package uk.co.caprica.vlcj.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

import javax.swing.*;
import java.awt.*;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Private helper class to ensure the native libraries are properly initialised on Linux.
 */
final class LinuxNativeInit {

    private static final Logger logger = LoggerFactory.getLogger(LinuxNativeInit.class);

    static void init() {
        // Only apply for Linux, not for a headless environment...
        if (RuntimeUtil.isNix() && !GraphicsEnvironment.isHeadless()) {
            // Only apply if the run-time version is Java 1.7.0 or later...
            logger.debug("Trying workaround for Java7+ on Linux");
            try {
                logger.debug("Attempting to load jawt...");
                // To prevent crashses in some applications, we must seemingly make sure that Swing is
                // initialised before force-loading libjawt - empirically both of these things are required
                new JPanel();
                System.loadLibrary("jawt");
                logger.debug("...loaded jawt");
            }
            catch(UnsatisfiedLinkError e) {
                logger.debug("Failed to load jawt", e);
            }
            logger.debug("Java7 on Linux workaround complete.");
        }
        // With recent VLC/JDK it seems necessary to do this (it will be silently ignored on non-
        // X platforms) - it can however cause problems if using the JVM splash-screen options
        // Ultimately this needs more investigation, it may no longer be necessary to do this with
        // VLC 3.0.0+
        //
        // Without this, it is also possible that opening a JavaFX FileChooser will cause a fatal
        // JVM crash
        String initX = System.getProperty("VLCJ_INITX"); // FIXME is this still needed?
        logger.debug("initX={}", initX);
        if (!"no".equalsIgnoreCase(initX)) {
            LibXUtil.initialise();
        }

    }

    private LinuxNativeInit() {
    }

}
