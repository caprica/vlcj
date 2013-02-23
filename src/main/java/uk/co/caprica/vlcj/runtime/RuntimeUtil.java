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

package uk.co.caprica.vlcj.runtime;

import uk.co.caprica.vlcj.logger.Logger;

/**
 * Crude heuristics to determine the current Operating System.
 * <p>
 * The com.sun.jna.Platform class provides similar functionality.
 */
public final class RuntimeUtil {

    /**
     * Operating System Name system property.
     */
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    /**
     * Prevent direct instantiation by others.
     */
    private RuntimeUtil() {
    }

    /**
     * Test whether the runtime operating system is "unix-like".
     *
     * @return true if the runtime OS is unix-like, Linux, Unix, FreeBSD etc
     */
    public static boolean isNix() {
        return OS_NAME.indexOf("nux") != -1 || OS_NAME.indexOf("nix") != -1 || OS_NAME.indexOf("freebsd") != -1;
    }

    /**
     * Test whether the runtime operating system is a Windows variant.
     *
     * @return true if the runtime OS is Windows
     */
    public static boolean isWindows() {
        return OS_NAME.indexOf("win") != -1;
    }

    /**
     * Test whether the runtime operating system is a Mac variant.
     *
     * @return true if the runtime OS is Mac
     */
    public static boolean isMac() {
        return OS_NAME.indexOf("mac") != -1;
    }

    /**
     * Try to safely convert a long value to an int.
     *
     * The current libvlc API requires a 32-bit integer value for the drawable window handle -
     * however, according to the JNA API it is possible that 64-bit integer values are used (since
     * Native.getComponentId returns a long value).
     * <p>
     * Therefore there is a chance that we are given a native window handle that we can not use with
     * libvlc.
     * <p>
     * In practice, I have never seen this happen on Linux or Windows.
     *
     * @param value long value
     * @return int value
     * @throws IllegalArgumentException if the long value can not be safely converted to an int
     */
    public static int safeLongToInt(long value) {
        Logger.debug("nativeComponentId={}", value);
        if(value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            Logger.warn("Native component id is too big for int");
            throw new IllegalArgumentException("long value " + value + " cannot be safely converted to an int.");
        }
        else {
            return (int)value;
        }
    }

    /**
     * Get the native library name.
     *
     * @return library name
     */
    public static String getLibVlcLibraryName() {
        return isWindows() ? "libvlc" : "vlc";
    }

    /**
     * Get the operating system file name for the libvlc shared object.
     * <p>
     * This is only used to generate help/error messages.
     *
     * @return shared object file name
     */
    public static String getLibVlcName() {
        if(RuntimeUtil.isNix()) {
            return "libvlc.so";
        }
        else if(RuntimeUtil.isWindows()) {
            return "libvlc.dll";
        }
        else if(RuntimeUtil.isMac()) {
            return "libvlc.dylib";
        }
        else {
            throw new RuntimeException("Unknown operating system");
        }
    }

    /**
     * Get the operating system file name for the libvlc core shared object.
     * <p>
     * This is only used to generate help/error messages.
     *
     * @return shared object file name
     */
    public static String getLibVlcCoreName() {
        if(RuntimeUtil.isNix()) {
            return "libvlccore.so";
        }
        else if(RuntimeUtil.isWindows()) {
            return "libvlccore.dll";
        }
        else if(RuntimeUtil.isMac()) {
            return "libvlccore.dylib";
        }
        else {
            throw new RuntimeException("Unknown operating system");
        }
    }

    /**
     * Get the default directory name for the vlc plugins directory.
     * <p>
     * This is only used to generate help/error messages.
     *
     * @return plugin directory name
     */
    public static String getPluginsDirectoryName() {
        if(!RuntimeUtil.isWindows()) {
            return "vlc/plugins";
        }
        else {
            return "plugins";
        }
    }
}
