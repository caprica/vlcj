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

package uk.co.caprica.vlcj.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Base implementation for file filters that are based on file name extensions.
 */
public abstract class ExtensionFileFilter implements FileFilter {

    /**
     * The recognised file extensions.
     */
    private final String[] extensions;

    /**
     * Set of recognised file extensions.
     */
    private final Set<String> extensionsSet = new HashSet<String>();

    /**
     * Create a new file filter.
     *
     * @param extensions file extensions to accept
     */
    protected ExtensionFileFilter(String[] extensions) {
        this.extensions = Arrays.copyOf(extensions, extensions.length);
        Arrays.sort(this.extensions);
        // Make a hash-set for faster look-up
        for(String extension : extensions) {
            extensionsSet.add(extension);
        }
    }

    /**
     * Get the recognised file extensions.
     * <p>
     * A sorted copy of the array of file extensions is returned.
     *
     * @return file extensions accepted by the filter
     */
    public String[] getExtensions() {
        // The array is already sorted
        return Arrays.copyOf(extensions, extensions.length);
    }

    /**
     * Get the set of recognised file extensions.
     * <p>
     * A new (copy) sorted set of file extensions is returned.
     *
     * @return set of file extensions accepted by the filter
     */
    public Set<String> getExtensionSet() {
        return new TreeSet<String>(extensionsSet);
    }

    @Override
    public boolean accept(File pathname) {
        if(pathname.isFile()) {
            String name = pathname.getName();
            int dot = name.lastIndexOf('.');
            if(dot != -1 && dot + 1 < name.length()) {
                String extension = name.substring(dot + 1);
                return extensionsSet.contains(extension);
            }
        }
        return false;
    }
}
