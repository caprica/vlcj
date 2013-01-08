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

package uk.co.caprica.vlcj.test.install;

import uk.co.caprica.vlcj.runtime.install.NativeLibraryManager;
import uk.co.caprica.vlcj.runtime.install.NativeLibraryManagerEventListener;

/**
 * Test for the native library manager.
 * <p>
 * You need to add a jar file containing the package of native libraries to the application
 * class-path before running this - you make this package yourself.
 */
public class NativeLibraryManagerTest {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        NativeLibraryManager m = new NativeLibraryManager("/var/tmp/jartemp");
        m.addEventListener(new NativeLibraryManagerEventListener() {
            @Override
            public void start(String installTo, int installCount) {
                System.out.printf("start %s %d\n", installTo, installCount);
            }

            @Override
            public void install(int number, String name) {
                System.out.printf("install %3d %s\n", number, name);
            }

            @Override
            public void end() {
                System.out.println("end");
            }

            @Override
            public void purge(String installTo) {
                System.out.printf("purge %s\n", installTo);
            }

            @Override
            public void purged(boolean result) {
                System.out.printf("purged %s\n", result ? "true" : "false");
            }
        });
        m.unpackNativePackage();
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        // Be very careful invoking purge, it will delete everything recursively!
        // m.purge();
    }
}
