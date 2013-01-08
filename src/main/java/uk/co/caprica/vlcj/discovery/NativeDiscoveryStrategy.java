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

package uk.co.caprica.vlcj.discovery;

/**
 * Specification for a component that can locate the libvlc native libraries at
 * run-time.
 */
public interface NativeDiscoveryStrategy {

    /**
     * Is this strategy supported?
     * <p>
     * Some strategies may, for example, only be applicable to one particular
     * operating system or another.
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
}
