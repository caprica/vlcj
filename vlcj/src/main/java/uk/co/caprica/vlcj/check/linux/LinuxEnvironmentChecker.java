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

package uk.co.caprica.vlcj.check.linux;

import java.util.ArrayList;
import java.util.List;

import uk.co.caprica.vlcj.check.EnvironmentChecker;

/**
 *
 */
public class LinuxEnvironmentChecker extends EnvironmentChecker {

  private static final String LIBRARY_NAME = "libvlc.so";

  private static final List<String> LIBRARY_PATHS = new ArrayList<String>();
  
  static {
    LIBRARY_PATHS.add("/usr/lib");
    LIBRARY_PATHS.add("/usr/local/lib");
    LIBRARY_PATHS.add("/lib");

    for(String path : System.getenv("LD_LIBRARY_PATH").split(":")) {
      LIBRARY_PATHS.add(path);
    }
  }
  
  @Override
  protected String getSharedLibraryName() {
    return LIBRARY_NAME;
  }
  
  @Override
  protected List<String> getSharedLibraryPaths() {
    return LIBRARY_PATHS;
  }

  @Override
  protected void checkNativeEnvironment() {
  }
}
