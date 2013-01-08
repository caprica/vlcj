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

/**
 * Composite file filter implementation for all media files recognised by libvlc.
 * <p>
 * A media file is one of:
 * <ul>
 *   <li>Video</li>
 *   <li>Audio</li>
 *   <li>Play-list</li>
 * </ul>
 */
public class MediaFileFilter implements FileFilter {

    /**
     * Filter for video files.
     */
    private final VideoFileFilter videoFileFilter = new VideoFileFilter();

    /**
     * Filter for audio files.
     */
    private final AudioFileFilter audioFileFilter = new AudioFileFilter();

    /**
     * Filter for play-list files.
     */
    private final PlayListFileFilter playlistFileFilter = new PlayListFileFilter();

    /**
     * Single instance.
     */
    public static final MediaFileFilter INSTANCE = new MediaFileFilter();

    @Override
    public boolean accept(File pathname) {
        return videoFileFilter.accept(pathname) || audioFileFilter.accept(pathname) || playlistFileFilter.accept(pathname);
    }
}
