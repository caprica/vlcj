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

package uk.co.caprica.vlcj.test.xlib;

import uk.co.caprica.vlcj.binding.LibX11;

/**
 * Simple test to get a reference to the Xlib native library and initialise threads.
 * <p>
 * Applications can be made more reliable (i.e. reduce the opportunity for fatal crashes in native
 * libraries) by explicitly invoking the LibX11 XInitThreads() method.
 * <p>
 * Only useful on platforms that use X of course.
 */
public class LibX11Test {

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        LibX11 x = LibX11.INSTANCE;
        System.out.println(x);
        int result = x.XInitThreads();
        System.out.println("XInitThreadsResult=" + result + " => " + (result != 0 ? "OK" : "FAILED"));
    }
}
