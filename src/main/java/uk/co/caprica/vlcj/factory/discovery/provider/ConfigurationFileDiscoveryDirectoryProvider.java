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
 * Copyright 2009-2020 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.factory.discovery.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

/**
 * Implementation of a {@link DiscoveryDirectoryProvider} that looks for an optional configuration file containing the
 * native discovery directory.
 * <p>
 * If a file named "~/.config/vlcj/vlcj.config" exists (under the user home directory) it will be loaded and if it
 * contains a "nativeDirectory" property, the value of that property will be used for the native discovery directory.
 */
public class ConfigurationFileDiscoveryDirectoryProvider implements DiscoveryDirectoryProvider {

    private static final String CONFIG_DIR = System.getProperty("user.home") + "/.config/vlcj";

    private static final String CONFIG_FILE_NAME = "vlcj.config";

    private static final String PROPERTY_NAME = "nativeDirectory";

    @Override
    public int priority() {
        return DiscoveryProviderPriority.CONFIG_FILE;
    }

    @Override
    public String[] directories() {
        Reader reader = null;
        try {
            File configurationFile = getConfigurationFile();
            Properties properties = new Properties();
            reader = new FileReader(configurationFile);
            properties.load(reader);
            String directory = properties.getProperty(PROPERTY_NAME);
            if (directory != null) {
                return new String[] {directory};
            }
        } catch (FileNotFoundException e) {
            // Nothing
        } catch (IOException e) {
            System.err.printf("Failed to load configuration file: %s%n", e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return new String[0];
    }

    @Override
    public boolean supported() {
        File configurationFile = getConfigurationFile();
        return configurationFile.exists() && configurationFile.isFile() && configurationFile.canRead();
    }

    private static final File getConfigurationFile() {
        return new File(CONFIG_DIR, CONFIG_FILE_NAME);
    }
}
