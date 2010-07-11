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

package uk.co.caprica.vlcj.runtime;

/**
 * Crude heuristics to determine the current Operating System.
 * <p>
 * The com.sun.jna.Platform class provides similar functionality.
 */
public class RuntimeUtil {

  /**
   * Operating System Name system property.
   */
  private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

  /**
   * Test whether the runtime operating system is "unix-like".
   * 
   * @return true if the runtime OS is unix-like, Linux, Unix, FreeBSD etc
   */
  public static boolean isNix() {
    return OS_NAME.indexOf("nux") != -1 || OS_NAME.indexOf("nix") != -1 || OS_NAME.indexOf("freebsd") != -1;
  }
  
  /**
   * Test whether the runtime operating system is a Windows variant. 
   * 
   * @return true if the runtime OS is Windows
   */
  public static boolean isWindows() {
    return OS_NAME.indexOf("win") != -1;
  }

  /**
   * Test whether the runtime operating system is a Mac variant.
   * 
   * @return true if the runtime OS is Mac
   */
  public static boolean isMac() {
    return OS_NAME.indexOf("mac") != -1;
  }
}