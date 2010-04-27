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

/**
 * A heuristic to try and find the libvlc shared object file, to aid debugging.
 */
public abstract class EnvironmentChecker {

  /**
   * Check for the existence of the libvlc shared object file.
   */
  public void checkEnvironment() {
    System.out.println("============================================================");
    System.out.println("=== CHECK ENVIRONMENT ======================================");
    System.out.println("============================================================");
    
    System.out.println();
    
    File jnaLibraryPathResult = checkJnaLibraryPath();
    File currentDirectoryResult = checkCurrentDirectory();
    List<File> searchPathResults = checkSearchPaths();

    List<File> allResults = new ArrayList<File>();
    if(jnaLibraryPathResult != null) {
      allResults.add(jnaLibraryPathResult);
    }
    allResults.add(currentDirectoryResult);
    allResults.addAll(searchPathResults);
    int longest = 0;
    for(File file : allResults) {
      longest = Math.max(longest, file.getAbsolutePath().length());
    }
    
    String jnaLibraryPath = System.getProperty("jna.library.path");
    System.out.println("-Djna.library.path=" + (jnaLibraryPath != null ? jnaLibraryPath : "<not-specified>"));
    if(jnaLibraryPathResult != null) {
      checkExists(jnaLibraryPathResult, longest);
    }

    System.out.println();
    
    System.out.println("Current Directory...");
    checkExists(currentDirectoryResult, longest);
    
    System.out.println();
    
    System.out.println("Search Paths...");
    
    for(File searchPathResult : searchPathResults) {
      checkExists(searchPathResult, longest);
    }
    
    System.out.println();
    
    checkNativeEnvironment();
    
    System.out.println();

    System.out.println("============================================================");
    System.out.println();
  }

  /**
   * 
   */
  private File checkJnaLibraryPath() {
    File result = null;
    String jnaLibraryPath = System.getProperty("jna.library.path");
    if(jnaLibraryPath != null) {
      result = new File(new File(jnaLibraryPath), getSharedLibraryName());
    }
    return result;
  }
  
  /**
   * 
   * 
   * @return
   */
  private File checkCurrentDirectory() {
    return new File(getSharedLibraryName());
  }
  
  /**
   * 
   */
  private List<File> checkSearchPaths() {
    List<File> result = new ArrayList<File>();
    for(String path : getSharedLibraryPaths()) {
      result.add(new File(new File(path), getSharedLibraryName()));
    }
    return result;
  }

  /**
   * 
   * 
   * @param file
   */
  private void checkExists(File file, int size) {
    System.out.printf(" %" + size + "s -> %s", file.getAbsolutePath(), file.exists() ? "FOUND" : "NOT FOUND");
    System.out.println();
  }
  
  /**
   * 
   * 
   * @return
   */
  protected abstract String getSharedLibraryName();

  /**
   * 
   * 
   * @return
   */
  protected abstract List<String> getSharedLibraryPaths();

  /**
   * 
   */
  protected abstract void checkNativeEnvironment();
}
