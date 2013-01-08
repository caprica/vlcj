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
 * Simple test for versions.
 */
public class VersionTest {

    /**
     * Execute the test.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        test("2.0.0", "2.0.0");
        test("1.2.0", "2.0.0");
        test("1.2.0", "1.2.1");
        test("1.2.0", "1.2.1-b1");
        test("1.2.0", "1.2.0");
        test("1.2.0", "1.1.9");
        test("1.2.0", "0.9.8");
        test("1.2.0", "1.1.10 The Luggage");
        test("1.1.11", "1.1.10 The Luggage");
        test("1.1.10", "1.1.10 The Luggage");
        test("1.1.9", "1.1.10 The Luggage");
        test("0.9.9", "0.9.9a Grishenko");
        test("1.0.0", "0.9.9a Grishenko");
        test("0.9.9a", "0.9.9b");
        test("0.9.9b", "0.9.9a");
    }

    /**
     * Execute a test case.
     *
     * @param required required version
     * @param actual actual version
     */
    private static void test(String required, String actual) {
        Version requiredVersion = new Version(required);
        Version actualVersion = new Version(actual);
        String result = actualVersion.atLeast(requiredVersion) ? "OK" : "Too Old!";
        System.out.println("Required: " + requiredVersion + ", Actual: " + actualVersion + ", Result: " + result);
    }
}
