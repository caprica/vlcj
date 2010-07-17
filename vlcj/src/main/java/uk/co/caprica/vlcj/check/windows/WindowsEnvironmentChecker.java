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

import org.apache.log4j.Logger;

import uk.co.caprica.vlcj.check.EnvironmentChecker;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;

/**
 *
 * @deprecated this will be removed in a future release
 */
public class WindowsEnvironmentChecker extends EnvironmentChecker {

  /**
   * Log.
   */
  private static final Logger LOG = Logger.getLogger(WindowsEnvironmentChecker.class);

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
    LOG.debug("checkNativeEnvironment()");
    
    String vlcInstallDir = WindowsRuntimeUtil.getVlcInstallDir();
    if(LOG.isDebugEnabled()) {LOG.debug("vlcInstallDir=" + vlcInstallDir);}
    
    if(vlcInstallDir != null) {
      File dir = new File(vlcInstallDir);
      if(LOG.isDebugEnabled()) {LOG.debug("dir=" + dir);}
      if(LOG.isDebugEnabled()) {LOG.debug("exists=" + dir.exists());}

      File pluginsDir = new File(vlcInstallDir, "plugins");
      if(LOG.isDebugEnabled()) {LOG.debug("pluginsDir=" + pluginsDir);}
      if(LOG.isDebugEnabled()) {LOG.debug("exists=" + pluginsDir.exists());}
    }
    else {
      LOG.warn("VLC installation directory is not known");
    }
  }
}
