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

package uk.co.caprica.vlcj.test.discovery;

import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.logger.Logger;

/**
 * A trivial test to demonstrate automatic discovery of the libvlc native shared
 * libraries.
 */
public class NativeDiscoveryTest {

    public static void main(String[] args) {
        System.setProperty("vlcj.log", "DEBUG");
        // Create a discovery component that uses the default provided discovery strategies
        boolean found = new NativeDiscovery().discover();
        Logger.debug("found={}", found);
        if(found) {
            Logger.debug("Version: {}" + LibVlcFactory.factory().create().libvlc_get_version());
        }
    }
}
