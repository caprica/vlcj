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

/**
 * Specification for a component that can locate the LibVLC native libraries at run-time.
 */
public interface NativeDiscoveryStrategy {

    /**
     * Is this strategy supported?
     * <p>
     * Some strategies may, for example, only be applicable to one particular operating system or another.
     *
     * @return <code>true</code> if this strategy is supported; <code>false</code> otherwise
     */
    boolean supported();

    /**
     * Attempt to discover the location of the libvlc native shared libraries.
     *
     * @return path containing the shared libraries, or <code>null</code> if this strategy did not find them
     */
    String discover();

    /**
     * Invoked when native shared libraries found.
     * <p>
     * This serves two purposes: the first is to enable the strategy implementation to carry out bespoke work if needed;
     * te second is to indicate whether or not the discovered path should be added to the JNA native library search
     * path.
     *
     * @param path directory containing the shared libraries
     * @return <code>true</code> if the path should be added to the JNA native search path; <code>false</code> if not
     */
    boolean onFound(String path);

    /**
     * Invoked after discovery has completed and found the native shared libraries.
     * <p>
     * This method will <em>not</em> be invoked if there is already a VLC_PLUGIN_PATH environment variable set.
     *
     * @param path directory containing the shared libraries
     * @return <code>true</code> if the plugin path was set successfully; <code>false</code> on error
     */
    boolean onSetPluginPath(String path);

}
