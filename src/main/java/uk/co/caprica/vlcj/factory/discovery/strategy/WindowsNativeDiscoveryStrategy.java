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

package uk.co.caprica.vlcj.factory.discovery.strategy;

import uk.co.caprica.vlcj.binding.LibC;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.factory.discovery.provider.DirectoryProviderDiscoveryStrategy;

/**
 * Default implementation of a native discovery strategy that searches directories on the Windows operating system.
 */
public class WindowsNativeDiscoveryStrategy extends DirectoryProviderDiscoveryStrategy {

    private static final String[] FILENAME_PATTERNS = new String[] {
        "libvlc\\.dll",
        "libvlccore\\.dll"
    };

    private static final String[] PLUGIN_PATH_FORMATS = new String[] {
        "%s\\plugins",
        "%s\\vlc\\plugins"
    };

    public WindowsNativeDiscoveryStrategy() {
        super(FILENAME_PATTERNS, PLUGIN_PATH_FORMATS);
    }

    @Override
    public boolean supported() {
        return RuntimeUtil.isWindows();
    }

    @Override
    protected boolean setPluginPath(String pluginPath) {
        return LibC.INSTANCE._putenv(String.format("%s=%s", PLUGIN_ENV_NAME, pluginPath)) == 0;
    }

}
