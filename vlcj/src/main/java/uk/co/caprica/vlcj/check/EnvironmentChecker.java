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
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.co.caprica.vlcj.binding.LibVlc;

/**
 * A heuristic to try and find the libvlc shared object file, to aid debugging.
 * 
 * @deprecated this will be removed in a future release
 */
public abstract class EnvironmentChecker {

  /**
   * Log.
   */
  private static final Logger LOG = Logger.getLogger(EnvironmentChecker.class);

  /**
   * Check for the existence of the libvlc shared object file.
   * <p>
   * This is not comprehensive.
   */
  public void checkEnvironment() {
    LOG.debug("checkEnvironment()");

    // First collect all of the information...
    
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
    
    // Now check and report...
    
    LOG.debug("JNA Library Path...");
    String jnaLibraryPath = System.getProperty("jna.library.path");
    if(LOG.isDebugEnabled()) {LOG.debug("-Djna.library.path=" + (jnaLibraryPath != null ? jnaLibraryPath : "<not-specified>"));}
    if(jnaLibraryPathResult != null) {
      checkExists(jnaLibraryPathResult, longest);
    }

    LOG.debug("Current Directory...");
    checkExists(currentDirectoryResult, longest);
    
    LOG.debug("Search Paths...");
    for(File searchPathResult : searchPathResults) {
      checkExists(searchPathResult, longest);
    }
    
    checkNativeEnvironment();
    
    // Some class-path checks...
    
    LOG.debug("Code Source...");
    try {
      CodeSource codeSource = LibVlc.class.getProtectionDomain().getCodeSource();
      if(codeSource != null) {
        URL url = codeSource.getLocation();
        LOG.debug("Loading VLCJ classes from " + url.toExternalForm());
      }
      else {
        LOG.debug("Unable to get the VLCJ code source");
      }
    }
    catch(SecurityException e) {
      LOG.debug("Failed to determine code source due to security manager constraint");
    }
    
    LOG.debug("Classpath...");
    URL[] urls = ((URLClassLoader)ClassLoader.getSystemClassLoader()).getURLs();
    for(URL url : urls) {
      LOG.debug(url.toExternalForm());
    }
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
    if(LOG.isDebugEnabled()) {LOG.debug("checkExists(file=" + file.getAbsolutePath() + ")");}
    if(LOG.isDebugEnabled()) {LOG.debug("exists=" + file.exists());}
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
