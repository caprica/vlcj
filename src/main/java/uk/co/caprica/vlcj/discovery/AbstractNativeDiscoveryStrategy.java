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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Base implementation for a component that attempts to locate the libvlc native
 * resources.
 * <p>
 * This implementation looks for libvlc shared libraries by name in pre-defined
 * directory locations.
 * <p>
 * Extra directory names can be provided by implementations of the {@link #getDirectoryNames(List)}
 * template method in sub-classes.
 * <p>
 * If the named native libraries are found in a particular directory, then that
 * directory is registered as a native library search path for JNA.
 */
public abstract class AbstractNativeDiscoveryStrategy implements NativeDiscoveryStrategy {

    /**
     * Collection of filenames to search for.
     */
    private final List<String> fileNames = new ArrayList<String>(2);

    /**
     * Create a native library discovery strategy.
     */
    public AbstractNativeDiscoveryStrategy() {
        fileNames.add(RuntimeUtil.getLibVlcName());
        fileNames.add(RuntimeUtil.getLibVlcCoreName());
    }

    @Override
    public final String discover() {
        Logger.debug("discover()");
        String result = null;
        // Get the list of directories to search
        List<String> directoryNames = new ArrayList<String>();
        getDirectoryNames(directoryNames);
        Logger.debug("directoryNames={}", directoryNames);
        // Search the set of declared directories...
        if(!directoryNames.isEmpty()) {
            // Search for the fixed set of declared filenames...
            Logger.debug("fileNames={}", (Object)fileNames);
            // Process each declared directory name
            for(String directoryName : directoryNames) {
                Logger.debug("directoryName={}", directoryName);
                // Assume found...
                result = directoryName;
                File dir = new File(directoryName);
                // Look for each declared file in this directory...
                for(String fileName : fileNames) {
                    Logger.debug("fileName={}", fileName);
                    File file = new File(dir, fileName);
                    if(!file.exists()) {
                        Logger.debug("File does not exist");
                        result = null;
                        break;
                    }
                }
                // If all declared files found in this directory, break out
                if(result != null) {
                    break;
                }
            }
        }
        Logger.debug("result={}", result);
        return result;
    }

    /**
     * Get the system search path components.
     *
     * @return path components
     */
    protected final List<String> getSystemPath() {
        String path = System.getenv("PATH");
        if(path != null) {
            String[] paths = path.split(File.pathSeparator);
            return Arrays.asList(paths);
        }
        else {
            return Collections.emptyList();
        }
    }

    /**
     * Get the names of the directories that should be searched.
     *
     * @return collection of directories to search
     */
    protected abstract void getDirectoryNames(List<String> directoryNames);
}
