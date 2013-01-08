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

package uk.co.caprica.vlcj.test;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

import com.sun.jna.NativeLibrary;

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
     * Log level, used only if the -Dvlcj.log= system property has not already been set.
     */
    private static final String VLCJ_LOG_LEVEL = "INFO";

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
        if(null == System.getProperty("vlcj.log")) {
            System.setProperty("vlcj.log", VLCJ_LOG_LEVEL);
        }

        // Safely try to initialise LibX11 to reduce the opportunity for native
        // crashes - this will silently throw an Error on Windows (and maybe MacOS)
        // that can safely be ignored
        LibXUtil.initialise();

        if(null != NATIVE_LIBRARY_SEARCH_PATH) {
            Logger.info("Explicitly adding JNA native library search path: '{}'", NATIVE_LIBRARY_SEARCH_PATH);
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        }

        System.setProperty("jna.dump_memory", DUMP_NATIVE_MEMORY);
    }

    /**
     * Set the standard look and feel.
     */
    protected static final void setLookAndFeel() {
        String lookAndFeelClassName = null;
        LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
        for(LookAndFeelInfo lookAndFeel : lookAndFeelInfos) {
            if("Nimbus".equals(lookAndFeel.getName())) {
                lookAndFeelClassName = lookAndFeel.getClassName();
            }
        }
        if(lookAndFeelClassName == null) {
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
