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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.caprica.vlcj.logger.Logger;

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

    @Override
    public final String discover() {
        Logger.debug("discover()");
        String result = null;
        // Get the list of directories to search
        List<String> directoryNames = new ArrayList<String>();
        getDirectoryNames(directoryNames);
        Logger.debug("directoryNames={}", directoryNames);
        // Process each declared directory name
        for(String directoryName : directoryNames) {
            Logger.debug("directoryName={}", directoryName);
            if(find(directoryName)) {
                result = directoryName;
                break;
            }
        }
        Logger.debug("result={}", result);
        return result;
    }

    /**
     * Attempt to match all required files in a particular directory.
     * <p>
     * The directory is <em>not</em> searched <em>recursively</em>.
     *
     * @param directoryName name of the directory to search
     * @return <code>true</code> if all required files were found; <code>false</code> otherwise
     */
    private boolean find(String directoryName) {
        File dir = new File(directoryName);
        File[] files = dir.listFiles();
        if(files != null) {
            Pattern[] patternsToMatch = getFilenamePatterns();
            int matchedCount = 0;
            for(File file : files) {
                for(Pattern pattern : patternsToMatch) {
                    Matcher matcher = pattern.matcher(file.getName());
                    if(matcher.matches()) {
                        Logger.debug("Matched '{}' in '{}'", file.getName(), directoryName);
                        matchedCount++ ;
                        if(matchedCount == patternsToMatch.length) {
                            Logger.debug("Matched all required files");
                            return true;
                        }
                    }
                }
            }
        }
        Logger.debug("Failed to matched all required files");
        return false;
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
     * Get the filename patterns to search for.
     *
     * @return filename patterns
     */
    protected abstract Pattern[] getFilenamePatterns();

    /**
     * Get the names of the directories that should be searched.
     *
     * @return collection of directories to search
     */
    protected abstract void getDirectoryNames(List<String> directoryNames);
}
