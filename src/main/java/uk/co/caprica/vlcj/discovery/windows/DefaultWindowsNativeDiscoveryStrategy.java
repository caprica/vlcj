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

package uk.co.caprica.vlcj.discovery.windows;

import java.util.List;

import uk.co.caprica.vlcj.discovery.StandardNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;

/**
 * Default implementation of a native library discovery strategy that searches in
 * standard well-known directory locations on Windows.
 */
public class DefaultWindowsNativeDiscoveryStrategy extends StandardNativeDiscoveryStrategy {

    @Override
    public final boolean supported() {
        return RuntimeUtil.isWindows();
    }

    @Override
    protected void onGetDirectoryNames(List<String> directoryNames) {
        // Try and find the location of the vlc installation directory from the registry
        String installDir = WindowsRuntimeUtil.getVlcInstallDir();
        if(installDir != null) {
            directoryNames.add(installDir);
        }
    }
}
