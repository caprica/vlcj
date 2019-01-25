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

package uk.co.caprica.vlcj.test.discovery;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.discovery.strategy.NativeDiscoveryStrategy;

/**
 * A trivial test to demonstrate automatic discovery of the libvlc native shared libraries.
 */
public class NativeDiscoveryTest {

    public static void main(String[] args) {
        NativeDiscovery discovery = new NativeDiscovery() {
            @Override
            protected void onFound(String path, NativeDiscoveryStrategy strategy) {
                System.out.println("Found");
                System.out.println(path);
                System.out.println(strategy);
            }

            @Override
            protected void onNotFound() {
                System.out.println("Not found");
            }
        };
        boolean found = discovery.discover();
        System.out.println(found);
    }

}
