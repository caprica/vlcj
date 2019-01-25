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

package uk.co.caprica.vlcj.discovery.strategy;

public final class JnaLibraryPathNativeDiscoveryStrategy implements NativeDiscoveryStrategy {

    /**
     * Name of the system property that JNA uses to find native libraries.
     */
    private static final String JNA_SYSTEM_PROPERTY_NAME = "jna.library.path";

    @Override
    public boolean supported() {
        return System.getProperty(JNA_SYSTEM_PROPERTY_NAME) != null;
    }

    @Override
    public String discover() {
        return null;
    }

    @Override
    public boolean onFound(String path) {
        return false;
    }

    @Override
    public boolean onSetPluginPath(String path) {
        // When using jna.library.path, we don't set VLC_PLUGIN_PATH, we assume the client application does it
        return false;
    }

}
