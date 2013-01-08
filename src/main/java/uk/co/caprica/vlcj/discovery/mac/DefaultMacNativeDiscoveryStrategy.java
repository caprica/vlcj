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

package uk.co.caprica.vlcj.discovery.mac;

import java.util.List;

import uk.co.caprica.vlcj.discovery.StandardNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Default implementation of a native library discovery strategy that searches in
 * standard well-known directory locations on MacOS.
 */
public class DefaultMacNativeDiscoveryStrategy extends StandardNativeDiscoveryStrategy {

    @Override
    public final boolean supported() {
        return RuntimeUtil.isMac();
    }

    @Override
    protected void onGetDirectoryNames(List<String> directoryNames) {
        directoryNames.add("/Applications/VLC.app/Contents/MacOS/lib");
    }
}
