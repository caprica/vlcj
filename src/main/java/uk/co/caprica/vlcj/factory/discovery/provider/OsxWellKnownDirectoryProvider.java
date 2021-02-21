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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.factory.discovery.provider;

import uk.co.caprica.vlcj.binding.RuntimeUtil;

/**
 * Implementation of a directory provider that returns a list of well-known directory locations to search on OSX.
 */
public class OsxWellKnownDirectoryProvider extends WellKnownDirectoryProvider {

    private static final String[] DIRECTORIES = {
        "/Applications/VLC.app/Contents/Frameworks",
        "/Applications/VLC.app/Contents/MacOS/lib"
    };

    @Override
    public String[] directories() {
        return DIRECTORIES;
    }

    @Override
    public boolean supported() {
        return RuntimeUtil.isMac();
    }

}
