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

package uk.co.caprica.vlcj.discovery.windows;

import java.util.List;
import java.util.regex.Pattern;

import uk.co.caprica.vlcj.binding.LibC;
import uk.co.caprica.vlcj.discovery.StandardNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;

/**
 * Default implementation of a native library discovery strategy that searches in
 * standard well-known directory locations on Windows.
 * <p>
 * This implementation will attempt to set the VLC_PLUGIN_PATH environment variable
 * to the correct directory based on the discovery of the native libraries - it is
 * assumed to be a sub-directory named "plugins", which will be the case for a standard
 * VLC installation.
 */
public class DefaultWindowsNativeDiscoveryStrategy extends StandardNativeDiscoveryStrategy {

    /**
     * Filename patterns to search for.
     */
    private static final Pattern[] FILENAME_PATTERNS = new Pattern[] {
        Pattern.compile("libvlc\\.dll"),
        Pattern.compile("libvlccore\\.dll")
    };

    @Override
    protected Pattern[] getFilenamePatterns() {
        return FILENAME_PATTERNS;
    }

    @Override
    public final boolean supported() {
        return RuntimeUtil.isWindows();
    }

    @Override
    protected void onGetDirectoryNames(List<String> directoryNames) {
        // Try and find the location of the vlc installation directory from the registry
        String installDir = WindowsRuntimeUtil.getVlcInstallDir();
        if(installDir != null) {
            directoryNames.add(0, installDir);
        }
    }

    @Override
    public void onFound(String path) {
        if (LibVlcVersion.getVersion().atLeast(LibVlcVersion.LIBVLC_220)) {
            if (System.getenv(PLUGIN_ENV_NAME) == null) {
                String pluginPath = String.format("%s\\%s", path, "plugins");
                LibC.INSTANCE._putenv(String.format("%s=%s", PLUGIN_ENV_NAME, pluginPath));
            }
        }
    }
}
