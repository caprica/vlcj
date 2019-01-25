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

package uk.co.caprica.vlcj.discovery.provider;

import uk.co.caprica.vlcj.discovery.strategy.BaseNativeDiscoveryStrategy;

import java.util.*;

abstract public class DirectoryProviderDiscoveryStrategy extends BaseNativeDiscoveryStrategy {

    private final ServiceLoader<DiscoveryDirectoryProvider> directoryProviders = ServiceLoader.load(DiscoveryDirectoryProvider.class);

    public DirectoryProviderDiscoveryStrategy(String[] filenamePatterns, String[] pluginPathFormats) {
        super(filenamePatterns, pluginPathFormats);
    }

    @Override
    public final List<String> discoveryDirectories() {
        List<String> directories = new ArrayList<String>();
        for (DiscoveryDirectoryProvider provider : getSupportedProviders()) {
            directories.addAll(Arrays.asList(provider.directories()));
        }
        return directories;
    }

    private List<DiscoveryDirectoryProvider> getSupportedProviders() {
        List<DiscoveryDirectoryProvider> result = new ArrayList<DiscoveryDirectoryProvider>();
        for (DiscoveryDirectoryProvider service : directoryProviders) {
            if (service.supported()) {
                result.add(service);
            }
        }
        return sort(result);
    }

    private List<DiscoveryDirectoryProvider> sort(List<DiscoveryDirectoryProvider> providers) {
        Collections.sort(providers, new Comparator<DiscoveryDirectoryProvider>() {
            @Override
            public int compare(DiscoveryDirectoryProvider p1, DiscoveryDirectoryProvider p2) {
                return p1.priority() - p2.priority();
            }
        });
        return providers;
    }

}
