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

package uk.co.caprica.vlcj.test.version;

import uk.co.caprica.vlcj.version.Version;

/**
 * Simple test for vlc version string formats.
 */
public class VersionFormatTest {

    /**
     * Execute the test.
     * <p>
     * This will throw a RuntimeException if the libvlc native library version is too old.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        test("2.0.0");
        test("1.2.0");
        test("1.2.0-b1");
        test("1.1.10 The Luggage");
    }

    /**
     * Execute a test case.
     *
     * @param v version string
     */
    private static void test(String v) {
        System.out.print(v);
        System.out.print(" -> ");
        try {
            new Version(v);
            System.out.println("OK!");
        }
        catch(Throwable t) {
            System.out.println("ERROR: " + t.getMessage());
        }
    }
}
