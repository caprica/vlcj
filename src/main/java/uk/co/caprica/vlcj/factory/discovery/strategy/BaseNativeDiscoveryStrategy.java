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

package uk.co.caprica.vlcj.factory.discovery.strategy;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.support.runtime.RuntimeUtil;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base implementation of a native discovery strategy that searches a list of directories for a list of files.
 */
public abstract class BaseNativeDiscoveryStrategy implements NativeDiscoveryStrategy {

    /**
     * Name of the system environment variable containing the VLC plugin path location.
     * <p>
     * This is optional, and might not be set.
     */
    protected static final String PLUGIN_ENV_NAME = "VLC_PLUGIN_PATH";

    /**
     * Filename patterns that must all be matched successfully.
     */
    private final Pattern[] patternsToMatch;

    /**
     * Directory name templates that will be tried to locate the VLC plugin directory, relative to the successfully
     * discovered native library directory.
     */
    private final String[] pluginPathFormats;

    /**
     * Create a new native discovery strategy.
     *
     * @param filenamePatterns filename patterns to search for, as regular expressions
     * @param pluginPathFormats directory name templates used to find the VLC plugin directory, printf style.
     */
    public BaseNativeDiscoveryStrategy(String[] filenamePatterns, String[] pluginPathFormats) {
        this.patternsToMatch = new Pattern[filenamePatterns.length];
        for (int i = 0; i < filenamePatterns.length; i++) {
            this.patternsToMatch[i] = Pattern.compile(filenamePatterns[i]);
        }
        this.pluginPathFormats = pluginPathFormats;
    }

    @Override
    public final String discover() {
        for (String discoveryDirectory : discoveryDirectories()) {
            String directoryResult = discover(new File(discoveryDirectory));
            if (directoryResult != null) {
                return directoryResult;
            }
        }
        return null;
    }

    /**
     * Provide the list of directories to search.
     *
     * @return list of directories to search
     */
    protected abstract List<String> discoveryDirectories();

    /**
     * Search a discovery directory, recursively, for the native shared libraries.
     *
     * @param discoveryDirectory name of the directory to search
     * @return name of the matching directory if successful, null if unsuccessful
     */
    private String discover(File discoveryDirectory) {
        if (!discoveryDirectory.exists() || !discoveryDirectory.isDirectory()) {
            return null;
        }
        if (find(discoveryDirectory)) {
            return discoveryDirectory.getAbsolutePath();
        }
        File[] subDirs = discoveryDirectory.listFiles(File::isDirectory);
        if (subDirs != null) {
            for (File subDir : subDirs) {
                String result = discover(subDir);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * Attempt to match all required files in a particular directory.
     * <p>
     * The directory is <em>not</em> searched <em>recursively</em>.
     *
     * @param dir directory to search
     * @return <code>true</code> if all required files were found; <code>false</code> otherwise
     */
    private boolean find(File dir) {
        File[] files = dir.listFiles(File::isFile);
        if (files != null) {
            Set<String> matches = new HashSet<>(patternsToMatch.length);
            for (File file : files) {
                for (Pattern pattern : patternsToMatch) {
                    Matcher matcher = pattern.matcher(file.getName());
                    if (matcher.matches()) {
                        // A match was found for this pattern (note that it may be possible to match multiple times, any
                        // one of those matches will do so a Set is used to ignore duplicates)
                        matches.add(pattern.pattern());
                        if (matches.size() == patternsToMatch.length) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public final boolean onFound(String path) {
        // This library is never invoked directly, but it must be loaded first
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcCoreLibraryName(), path);
        NativeLibrary.getInstance(RuntimeUtil.getLibVlcCoreLibraryName());
        return true;
    }

    @Override
    public final boolean onSetPluginPath(String path) {
        for (String pathFormat : pluginPathFormats) {
            String pluginPath = String.format(pathFormat, path);
            if (new File(pluginPath).exists()) {
                return setPluginPath(pluginPath);
            }
        }
        return false;
    }

    /**
     * Set the VLC_PLUGIN_PATH environment variable.
     *
     * @param pluginPath value to set
     * @return <code>true</code> if the environment variable was successfully set; <code>false</code> on error
     */
    protected abstract boolean setPluginPath(String pluginPath);

}
