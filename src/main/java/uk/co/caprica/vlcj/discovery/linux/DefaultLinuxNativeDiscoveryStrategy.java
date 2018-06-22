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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.discovery.linux;

import java.util.List;
import java.util.regex.Pattern;

import uk.co.caprica.vlcj.discovery.StandardNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Default implementation of a native library discovery strategy that searches in
 * standard well-known directory locations on Linux.
 */
public class DefaultLinuxNativeDiscoveryStrategy extends StandardNativeDiscoveryStrategy {

    /**
     * Filename patterns to search for.
     * <p>
     * The intent is to match one of (for example):
     * <ul>
     *   <li>libvlc.so</li>
     *   <li>libvlc.so.5</li>
     *   <li>libvlc.so.5.3.0</li>
     * </ul>
     */
    private static final Pattern[] FILENAME_PATTERNS = new Pattern[] {
        Pattern.compile("libvlc\\.so(?:\\.\\d)*"),
        Pattern.compile("libvlccore\\.so(?:\\.\\d)*")
    };

    @Override
    protected Pattern[] getFilenamePatterns() {
        return FILENAME_PATTERNS;
    }

    @Override
    public final boolean supported() {
        return RuntimeUtil.isNix();
    }

    @Override
    protected void onGetDirectoryNames(List<String> directoryNames) {
            directoryNames.add("/usr/lib");
            directoryNames.add("/usr/lib64");
            directoryNames.add("/usr/local/lib");
            directoryNames.add("/usr/local/lib64");
            directoryNames.add("/usr/lib/x86_64-linux-gnu");
            directoryNames.add("/usr/lib/i386-linux-gnu");
    }
}
