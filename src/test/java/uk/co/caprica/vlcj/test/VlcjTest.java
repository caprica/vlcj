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
 * Copyright 2009, 2010, 2011, 2012, 2013, 2014 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.support.Info;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Base class for tests.
 * <p>
 * This makes it a lot easier to switch vlc versions or vlc install directories without having to
 * change system properties on a lot of IDE application run-configurations.
 * <p>
 * Explicitly setting a search path forces JNA to search that path <em>first</em>.
 * <p>
 * The search path should be the directory that contains libvlc.so and libvlccore.so.
 * <p>
 * If you do not explicitly set the search path, the system search path will be used.
 * <p>
 * You can also set the log level here.
 */
public abstract class VlcjTest {

    /**
     * Change this to point to your own vlc installation, or comment out the code if you want to use
     * your system default installation.
     * <p>
     * This is a bit more explicit than using the -Djna.library.path= system property.
     */
    private static final String NATIVE_LIBRARY_SEARCH_PATH = null;

    /**
     * Set to true to dump out native JNA memory structures.
     */
    private static final String DUMP_NATIVE_MEMORY = "false";

    /**
     * Static initialisation.
     */
    static {
        Info info = Info.getInstance();

        System.out.printf("vlcj             : %s%n", info.vlcjVersion() != null ? info.vlcjVersion() : "<version not available>");
        System.out.printf("os               : %s%n", val(info.os()));
        System.out.printf("java             : %s%n", val(info.javaVersion()));
        System.out.printf("java.home        : %s%n", val(info.javaHome()));
        System.out.printf("jna.library.path : %s%n", val(info.jnaLibraryPath()));
        System.out.printf("java.library.path: %s%n", val(info.javaLibraryPath()));
        System.out.printf("PATH             : %s%n", val(info.path()));
        System.out.printf("VLC_PLUGIN_PATH  : %s%n", val(info.pluginPath()));

        if (RuntimeUtil.isNix()) {
            System.out.printf("LD_LIBRARY_PATH  : %s%n", val(info.ldLibraryPath()));
        } else if (RuntimeUtil.isMac()) {
            System.out.printf("DYLD_LIBRARY_PATH          : %s%n", val(info.dyldLibraryPath()));
            System.out.printf("DYLD_FALLBACK_LIBRARY_PATH : %s%n", val(info.dyldFallbackLibraryPath()));
        }

        if (null != NATIVE_LIBRARY_SEARCH_PATH) {
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        }

        System.setProperty("jna.dump_memory", DUMP_NATIVE_MEMORY);
    }

    private static String val(String val) {
        return val != null ? val : "<not set>";
    }

    /**
     * Set the standard look and feel.
     */
    protected static final void setLookAndFeel() {
        String lookAndFeelClassName = null;
        LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
        for(LookAndFeelInfo lookAndFeel : lookAndFeelInfos) {
            if ("Nimbus".equals(lookAndFeel.getName())) {
                lookAndFeelClassName = lookAndFeel.getClassName();
            }
        }
        if (lookAndFeelClassName == null) {
            lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
        }
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
        }
        catch(Exception e) {
            // Silently fail, it doesn't matter
        }
    }
}
