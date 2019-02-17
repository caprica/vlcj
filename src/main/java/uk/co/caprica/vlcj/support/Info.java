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

package uk.co.caprica.vlcj.support;

import uk.co.caprica.vlcj.support.version.Version;

import java.util.Properties;

/**
 * Application version/environment information.
 * <p>
 * May be useful for diagnostics.
 */
public final class Info {

    /**
     * Singleton holder.
     */
    private static class InfoHolder {

        /**
         * Singleton instance.
         */
        public static final Info INSTANCE = new Info();
    }

    /**
     * Get application information.
     *
     * @return singleton instance
     */
    public static Info getInstance() {
        return InfoHolder.INSTANCE;
    }

    /**
     * vlcj version.
     */
    private Version vlcjVersion;

    private final String os;

    private final String javaVersion;

    private final String javaHome;

    private final String jnaLibraryPath;

    private final String javaLibraryPath;

    private final String path;

    private final String pluginPath;

    private final String ldLibraryPath;

    private final String dyldLibraryPath;

    private final String dyldFallbackLibraryPath;

    private Info() {
        vlcjVersion = getVlcjVersion();

        os = String.format("%s %s %s", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
        javaVersion = String.format("%s %s", System.getProperty("java.version"), System.getProperty("java.vendor"));

        javaHome = System.getProperty("java.home");
        jnaLibraryPath = System.getProperty("jna.library.path");
        javaLibraryPath = System.getProperty("java.library.path");

        path = System.getenv("PATH");

        pluginPath = System.getenv("VLC_PLUGIN_PATH");

        ldLibraryPath = System.getenv("LD_LIBRARY_PATH");

        dyldLibraryPath = System.getenv("DYLD_LIBRARY_PATH");
        dyldFallbackLibraryPath = System.getenv("DYLD_FALLBACK_LIBRARY_PATH");
    }

    /**
     * Get the vlcj version.
     *
     * @return version
     */
    public Version vlcjVersion() {
        return vlcjVersion;
    }

    /**
     * Get the runtime operating system.
     *
     * @return operating system
     */
    public String os() {
        return os;
    };

    /**
     * Get the Java version.
     *
     * @return version
     */
    public String javaVersion() {
        return javaVersion;
    }

    /**
     * Get the Java home directory.
     *
     * @return home directory
     */
    public String javaHome() {
        return javaHome;
    }

    /**
     * Get the JNA library path system property.
     *
     * @return JNA library path
     */
    public String jnaLibraryPath() {
        return jnaLibraryPath;
    }

    /**
     * Get the Java library path system property.
     *
     * @return Java library path
     */
    public String javaLibraryPath() {
        return javaLibraryPath;
    }

    /**
     * Get the runtime operating system search path.
     *
     * @return operating system path
     */
    public String path() {
        return path;
    }

    /**
     * Get the VLC plugin path directory environment variable.
     *
     * @return plugin path directory
     */
    public String pluginPath() {
        return pluginPath;
    }

    /**
     * Get the native load-library path (Linux).
     *
     * @return library path
     */
    public String ldLibraryPath() {
        return ldLibraryPath;
    }

    /**
     * Get the native dynamic-load-library path (OSX).
     *
     * @return library path
     */
    public String dyldLibraryPath() {
        return dyldLibraryPath;
    }

    /**
     * Get the native fallback dynamic-load-library path (OSX).
     *
     * @return library path
     */
    public String dyldFallbackLibraryPath() {
        return dyldFallbackLibraryPath;
    }

    private Version getVlcjVersion() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("/uk/co/caprica/vlcj/build.properties"));
            return new Version(properties.getProperty("build.version"));
        }
        catch(Exception e) {
            // This can only happen if something went wrong with the build
            return null;
        }
    }

    @Override
    public String toString() {
        return new StringBuilder(200)
            .append(getClass().getSimpleName()).append('[')
            .append("vlcjVersion=").append(vlcjVersion).append(',')
            .append("os=").append(os).append(',')
            .append("javaVersion=").append(javaVersion).append(',')
            .append("javaHome=").append(javaHome).append(',')
            .append("jnaLibraryPath=").append(jnaLibraryPath).append(',')
            .append("javaLibraryPath=").append(javaLibraryPath).append(',')
            .append("path=").append(path).append(',')
            .append("pluginPath=").append(pluginPath).append(',')
            .append("ldLibraryPath=").append(ldLibraryPath).append(',')
            .append("dyldLibraryPath=").append(dyldLibraryPath).append(',')
            .append("dyldFallbackLibraryPath=").append(dyldFallbackLibraryPath).append(']')
            .toString();
    }

}
