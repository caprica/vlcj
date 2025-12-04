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

import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * Implementation of a directory provider that returns the directory the application is installed in.
 */
public class AppDirDirectoryProvider implements DiscoveryDirectoryProvider {

    @Override
    public int priority() {
        return DiscoveryProviderPriority.APP_DIR;
    }

    @Override
    public String[] directories() {
        try {
            Class<?> clazz = DiscoveryDirectoryProvider.class;
            ProtectionDomain protectionDomain = clazz.getProtectionDomain();
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource != null) {
                URL location = codeSource.getLocation();
                if (location != null) {
                    File file = new File(location.toURI());
                    if (!file.isDirectory()) {
                        file = file.getParentFile();
                    }
                    return new String[] { file.getAbsolutePath() };
                }
            }
            return new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }

    @Override
    public boolean supported() {
        return true;
    }
}
