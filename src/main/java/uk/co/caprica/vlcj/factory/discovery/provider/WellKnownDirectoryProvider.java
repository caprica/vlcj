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
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.factory.discovery.provider;

/**
 * Base implementation for directory provider that searches well-known directories.
 * <p>
 * This base class is used simply to set the appropriate priority value.
 */
abstract public class WellKnownDirectoryProvider implements DiscoveryDirectoryProvider {

    @Override
    public int priority() {
        return DiscoveryProviderPriority.WELL_KNOWN_DIRECTORY;
    }

}
