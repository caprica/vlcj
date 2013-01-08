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
 * File filter implementation for video files recognised by libvlc.
 */
public class VideoFileFilter extends ExtensionFileFilter {

    /**
     * From the vlc_interface.h include file.
     */
    private static final String[] EXTENSIONS_VIDEO = {
        "3g2",
        "3gp",
        "3gp2",
        "3gpp",
        "amv",
        "asf",
        "avi",
        "bin",
        "divx",
        "drc",
        "dv",
        "f4v",
        "flv",
        "gvi",
        "gxf",
        "iso",
        "m1v",
        "m2v",
        "m2t",
        "m2ts",
        "m4v",
        "mkv",
        "mov",
        "mp2",
        "mp2v",
        "mp4",
        "mp4v",
        "mpe",
        "mpeg",
        "mpeg1",
        "mpeg2",
        "mpeg4",
        "mpg",
        "mpv2",
        "mts",
        "mtv",
        "mxf",
        "mxg",
        "nsv",
        "nuv",
        "ogg",
        "ogm",
        "ogv",
        "ogx",
        "ps",
        "rec",
        "rm",
        "rmvb",
        "tod",
        "ts",
        "tts",
        "vob",
        "vro",
        "webm",
        "wm",
        "wmv",
        "wtv",
        "xesc"
    };

    /**
     * Single instance.
     */
    public static final VideoFileFilter INSTANCE = new VideoFileFilter();

    /**
     * Create a new file filter.
     */
    public VideoFileFilter() {
        super(EXTENSIONS_VIDEO);
    }
}
