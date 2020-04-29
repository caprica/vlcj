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

/**
 * Priority values used by the standard {@link DiscoveryDirectoryProvider} implementations.
 */
public interface DiscoveryProviderPriority {

    int CONFIG_FILE = 1;

    int JNA_LIBRARY_PATH = -1;

    int USER_DIR = -2;

    int INSTALL_DIR = -3;

    int WELL_KNOWN_DIRECTORY = -3;

    int SYSTEM_PATH = -4;

}
