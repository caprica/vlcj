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

package uk.co.caprica.vlcj.check;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Platform;

/**
 * A heuristic to try and find the libvlc shared object file, to aid debugging.
 */
public class EnvironmentChecker {

  /**
   * Check for the existence of the libvlc shared object file.
   */
  public void checkEnvironment() {
    System.out.println("============================================================");
    System.out.println("=== CHECK ENVIRONMENT ======================================");
    System.out.println("============================================================");
    
    System.out.println();

    if(Platform.isWindows() || Platform.isLinux()) {
      String nativeFileName = Platform.isWindows() ? "libvlc.dll" : "libvlc.so";
  
      File firstMatchingFile = null;
      
      // Check jna.library.path first...
      String jnaLibraryPath = System.getProperty("jna.library.path");
      if(jnaLibraryPath != null) {
        File searchDirectory = new File(jnaLibraryPath);
        File searchFile = new File(searchDirectory, nativeFileName);
        boolean exists = searchFile.exists();
        System.out.println("-Djna.library.path=" + jnaLibraryPath + " -> " + searchFile.getAbsolutePath() + " -> " + (exists ? "FOUND" : "NOT FOUND"));
        if(exists) {
          firstMatchingFile = searchFile;
        }
      }
      else {
        System.out.println("-Djna.library.path=<path> not set");
      }
  
      System.out.println();
      
      // Check operating system search paths...
      List<String> searchLocations = Platform.isWindows() ? getWindowsSearchPaths() : getLinuxSearchPaths();
      for(String searchLocation : searchLocations) {
        File searchDirectory = new File(searchLocation);
        File searchFile = new File(searchDirectory, nativeFileName);
        boolean exists = searchFile.exists();
        System.out.println(searchFile.getAbsolutePath() + " -> " + (exists ? "FOUND" : "NOT FOUND"));
        
        if(firstMatchingFile == null && exists) {
          firstMatchingFile = searchFile;
        }
      }
  
      System.out.println();
      
      if(firstMatchingFile != null) {
        System.out.println("Found libvlc at '" + firstMatchingFile.getAbsolutePath() + "'");
      }
      else {
        System.out.println("WARNING: Could not find libvlc shared library in any of the usual places");
      }
    }
    else {
      System.out.println("Checking the environment not supported on your platform");
    }

    System.out.println();
    
    System.out.println("======================================================================");
    System.out.println();
  }
  
  /**
   * Get the list of potential shared library search paths on Linux.
   * 
   * @return list of search paths
   */
  private List<String> getLinuxSearchPaths() {
    List<String> paths = new ArrayList<String>();
    
    paths.add("/usr/lib");
    paths.add("/usr/local/lib");
    paths.add("/lib");

    String ldLibraryPath = System.getenv("LD_LIBRARY_PATH");
    addPaths(paths, ldLibraryPath.split(":"));
    
    return paths;
  }
  
  /**
   * Get the list of potential shared library search paths on Windows.
   * 
   * @return list of search paths
   */
  private List<String> getWindowsSearchPaths() {
    List<String> paths = new ArrayList<String>();

    String path = System.getenv("PATH");
    addPaths(paths, path.split(";"));
    
    return paths;
  }

  /**
   * Add a set of paths.
   * 
   * @param paths collection of paths
   * @param parts paths to add
   */
  private void addPaths(List<String> paths, String[] parts) {
    for(String part : parts) {
      paths.add(part);
    }
  }
}
