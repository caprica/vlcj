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

package uk.co.caprica.vlcj.test.discovery;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

/**
 * A trivial test to demonstrate automatic discovery of the libvlc native shared
 * libraries.
 */
public class NativeDiscoveryTest {

    /**
     * Log.
     */
    private static final Logger logger = LoggerFactory.getLogger(NativeDiscoveryTest.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        // Create a discovery component that uses the default provided discovery strategies
        boolean found = new NativeDiscovery().discover();
        logger.debug("found={}", found);
        if(found) {
            logger.debug("Version: {}", LibVlcFactory.factory().create().libvlc_get_version());
        }
    }
}
