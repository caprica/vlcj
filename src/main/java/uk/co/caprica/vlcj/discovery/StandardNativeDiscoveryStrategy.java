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
 * Copyright 2009-2018 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.discovery;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of a native library discovery strategy that searches in standard
 * well-known directory locations.
 * <p>
 * This strategy should be supported across all platforms.
 */
public abstract class StandardNativeDiscoveryStrategy extends AbstractNativeDiscoveryStrategy {
    
    private final String[] extraPaths;
    
    public StandardNativeDiscoveryStrategy(String... extraPaths) {
        this.extraPaths = extraPaths;
    }
    
    @Override
    public boolean supported() {
        return true;
    }

    @Override
    protected final void getDirectoryNames(List<String> directoryNames) {
        // Look in the current directory
        directoryNames.add(System.getProperty("user.dir"));
        // Look in the directories on the system path
        directoryNames.addAll(getSystemPath());
        // Look in extra directories supplied in constructor
        directoryNames.addAll(Arrays.asList(extraPaths));
        // Look in the extra directories supplied by the sub-class
        onGetDirectoryNames(directoryNames);
    }

    /**
     * Template method to get extra, perhaps platform-specific, directory names
     * to search.
     *
     * @param directoryNames list of directories to add to
     */
    protected void onGetDirectoryNames(List<String> directoryNames) {
    }
}
