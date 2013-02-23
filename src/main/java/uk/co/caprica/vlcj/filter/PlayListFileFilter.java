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

/**
 * File filter implementation for play-list files recognised by libvlc.
 */
public class PlayListFileFilter extends ExtensionFileFilter {

    /**
     * From the vlc_interfaces.h include file.
     */
    private static final String[] EXTENSIONS_PLAYLIST = {
        "asx",
        "b4s",
        "cue",
        "conf",
        "ifo",
        "m3u",
        "m3u8",
        "pls",
        "ram",
        "rar",
        "sdp",
        "vlc",
        "xspf",
        "wvx",
        "zip"
    };

    /**
     * Single instance.
     */
    public static final PlayListFileFilter INSTANCE = new PlayListFileFilter();

    /**
     * Create a new file filter.
     */
    public PlayListFileFilter() {
        super(EXTENSIONS_PLAYLIST);
    }
}
