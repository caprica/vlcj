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

import uk.co.caprica.vlcj.binding.LibX11;

import javax.swing.*;
import java.awt.*;

/**
 * Private helper class to ensure the native libraries are properly initialised on Linux.
 * <p>
 * If there are any errors when executing this initialisation code there is little point in even reporting them as
 * nothing else can be done. The approach therefore is do a best-effort at running this initialisation and robustly
 * ignore errors.
 * <p>
 * The reality is that the nature of the initialisation code means errors simply should not occur.
 */
final class LinuxNativeInit {

    static void init() {
        initAWT();
        initX();
    }

    private static void initAWT() {
        if (!GraphicsEnvironment.isHeadless()) {
            try {
                // To prevent crashes in some applications, we must seemingly make sure that Swing is initialised before
                // force-loading libjawt - empirically both of these things are required
                new JPanel();
                System.loadLibrary("jawt");
            }
            catch (UnsatisfiedLinkError e) {
            }
        }
    }

    /**
     * With recent VLC/JDK it seems necessary to do this - it can however cause problems if using the JVM splash-screen
     * options. Without this, VLC may complain to the console output and it is also possible that opening a JavaFX
     * FileChooser will cause a fatal JVM crash.
     */
    private static void initX() {
        String initX = System.getProperty("VLCJ_INITX");
        if (!"no".equalsIgnoreCase(initX)) {
            try {
                LibX11.INSTANCE.XInitThreads();
            }
            catch (Exception e) {
            }
        }
    }

    private LinuxNativeInit() {
    }

}
