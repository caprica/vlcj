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
 * Copyright 2009, 2010 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.xlib;

import uk.co.caprica.vlcj.binding.LibX11;

/**
 * Simple test to get a reference to the Xlib native library.
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
