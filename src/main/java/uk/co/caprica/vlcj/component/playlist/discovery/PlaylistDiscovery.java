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
 * Copyright 2009, 2010, 2011, 2012 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.component.playlist.discovery;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.caprica.vlcj.component.playlist.Playlist;
import uk.co.caprica.vlcj.component.playlist.PlaylistEntry;

/**
 * Helper for discovering and creating play-list items.
 * 
 * TODO support media options
 * TODO support insert as well as append
 */
public class PlaylistDiscovery {

    /**
     * Filter used to find directories.
     */
    private static final FileFilter DIRECTORY_FILTER = new DirectoryFileFilter();

    /**
     * Add one or more media resource locators to the play-list.
     * 
     * @param playlist play-list to add MRLs to
     * @param media MRLs to add
     */
    public static final void addMedia(final Playlist playlist, final String... media) { 
        for(String mrl : media) {
            playlist.append(new PlaylistEntry(mrl));
        }
    }
    
    /**
     * Add one or more files to the play-list.
     * 
     * @param playlist play-list to add files to
     * @param files files to add, each item must be a file and not a directory
     */
    public static final void addFiles(final Playlist playlist, final File... files) {
        // Convert the parameter and delegate to add...
        addFiles(playlist, Arrays.asList(files));
    }
    
    /**
     * Add one or more files to the play-list.
     * 
     * @param playlist play-list to add files to
     * @param files files to add, each item must be a file and not a directory
     */
    public static final void addFiles(final Playlist playlist, final List<File> files) {
        // Validate...
        for(File file : files) {
            if(!file.isFile()) {
                throw new IllegalArgumentException("'" + file.getAbsolutePath() + "' is not a file");
            }
        }
        // Add the items
        for(File file : files) {
            playlist.append(new PlaylistEntry(file.getAbsolutePath(), file));
        }
    }

    /**
     * Add one or more files to the play-list.
     * <p>
     * The supplied media root must be a directory, and it will be scanned recursively
     * for files to add according to the supplied filter.
     * 
     * @param playlist play-list to add files to
     * @param mediaRoot root directory containing items to add
     * @param filter file filter to include/exclude files that should be added
     */
    public static final void addFiles(final Playlist playlist, final File mediaRoot, final FileFilter filter) {
        if(mediaRoot.isDirectory()) {
            // First collect all of the files...
            final List<File> result = new ArrayList<File>(100);
            collect(mediaRoot, filter, result);
            // ...then add them to the play-list inside the lock...
            if(!result.isEmpty()) {
                for(File file : result) {
                    playlist.append(new PlaylistEntry(file.getAbsolutePath(), file));
                }
            }
        }
        else {
            throw new IllegalArgumentException("'" + mediaRoot + "' is not a directory");
        }
    }

    /**
     * Collect files recursively.
     * 
     * @param mediaRoot root directory
     * @param filter file filter to include/exclude files
     * @param result collected files
     */
    private static void collect(File mediaRoot, FileFilter filter, List<File> result) {
        // First collect the files that match the filter
        File[] files = mediaRoot.listFiles(filter);
        if(files != null) {
            for(File file : files) {
                result.add(file);
            }
        }
        // Next collect directories recursively
        File[] dirs = mediaRoot.listFiles(DIRECTORY_FILTER);
        if(dirs != null) {
            for(File dir : dirs) {
                collect(dir, filter, result);
            }
        }
    }
}
