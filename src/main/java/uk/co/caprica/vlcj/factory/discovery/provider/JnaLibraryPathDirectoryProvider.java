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

package uk.co.caprica.vlcj.factory.discovery.provider;

import java.io.File;

/**
 * Implementation of a directory provider that uses the "jna.library.path" system property.
 * <p>
 * If using "jna.library.path" correctly, the native library should be found directly by JNA, but this provider
 * implementation may still be useful as it will set the VLC_PLUGIN_PATH.
 */
public class JnaLibraryPathDirectoryProvider implements DiscoveryDirectoryProvider {

    private static final String SYSTEM_PROPERTY_NAME = "jna.library.path";

    @Override
    public int priority() {
        return DiscoveryProviderPriority.JNA_LIBRARY_PATH;
    }

    @Override
    public String[] directories() {
        // The null-check is taken care of in the supported() method
        return System.getProperty("jna.library.path").split(File.pathSeparator);
    }

    @Override
    public boolean supported() {
        return System.getProperty(SYSTEM_PROPERTY_NAME) != null;
    }

}
