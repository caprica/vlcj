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
 * File filter implementation for audio files recognised by libvlc.
 */
public class AudioFileFilter extends ExtensionFileFilter {

    /**
     * From the vlc_interfaces.h include file.
     */
    private static final String[] EXTENSIONS_AUDIO = {
        "3ga",
        "669",
        "a52",
        "aac",
        "ac3",
        "adt",
        "adts",
        "aif",
        "aifc",
        "aiff",
        "amr",
        "aob",
        "ape",
        "awb",
        "caf",
        "dts",
        "flac",
        "it",
        "kar",
        "m4a",
        "m4p",
        "m5p",
        "mid",
        "mka",
        "mlp",
        "mod",
        "mp1",
        "mp2",
        "mp3",
        "mpc",
        "mpga",
        "oga",
        "ogg",
        "oma",
        "opus",
        "qcp",
        "ra",
        "rmi",
        "s3m",
        "spx",
        "thd",
        "tta",
        "voc",
        "vqf",
        "w64",
        "wav",
        "wma",
        "wv",
        "xa",
        "xm"
    };

    /**
     * Single instance.
     */
    public static final AudioFileFilter INSTANCE = new AudioFileFilter();

    /**
     * Create a new file filter.
     */
    public AudioFileFilter() {
        super(EXTENSIONS_AUDIO);
    }
}
