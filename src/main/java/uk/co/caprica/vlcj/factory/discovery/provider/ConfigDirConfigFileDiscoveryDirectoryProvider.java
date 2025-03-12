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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.factory.discovery.provider;

/**
 * Implementation of a {@link DiscoveryDirectoryProvider} that looks for an optional configuration file containing the
 * native discovery directory.
 * <p>
 * If a file named "~/.config/vlcj/vlcj.config" exists (under the user home directory) it will be loaded and if it
 * contains a "nativeDirectory" property, the value of that property will be used for the native discovery directory.
 */
public class ConfigDirConfigFileDiscoveryDirectoryProvider extends BaseConfigFileDiscoveryDirectoryProvider {

    private static final String CONFIG_DIR = System.getProperty("user.home") + "/.config/vlcj";

    @Override
    public int priority() {
        return DiscoveryProviderPriority.CONFIG_FILE;
    }

    @Override
    protected String configurationDirectory() {
        return CONFIG_DIR;
    }
}
