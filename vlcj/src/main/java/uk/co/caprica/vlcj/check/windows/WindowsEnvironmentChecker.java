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

package uk.co.caprica.vlcj.check.windows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.caprica.vlcj.check.EnvironmentChecker;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;

/**
 *
 */
public class WindowsEnvironmentChecker extends EnvironmentChecker {

  private static final String LIBRARY_NAME = "libvlc.dll";

  private static final List<String> LIBRARY_PATHS = new ArrayList<String>();
  
  static {
    for(String path : System.getenv("PATH").split(";")) {
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
    System.out.println("Registry...");
    String vlcInstallDir = WindowsRuntimeUtil.getVlcInstallDir();
    if(vlcInstallDir != null) {
      File dir = new File(vlcInstallDir);
      System.out.println(" vlcInstallDir=" + vlcInstallDir + " -> " + (dir.exists() ? "FOUND" : "NOT FOUND"));
      File pluginsDir = new File(vlcInstallDir, "plugins");
      System.out.println("    pluginsDir=" + pluginsDir + " -> " + (pluginsDir.exists() ? "FOUND" : "NOT FOUND"));
    }
    else {
      System.out.println(" vlcInstallDir=<not-available>");
    }
  }
}
