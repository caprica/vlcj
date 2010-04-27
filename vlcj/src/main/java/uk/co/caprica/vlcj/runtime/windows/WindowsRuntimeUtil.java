package uk.co.caprica.vlcj.runtime.windows;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

/**
 * Windows specific run-time utilities.
 */
public class WindowsRuntimeUtil {

  /**
   * The VLC registry key, under HKLM.
   */
  public static final String VLC_REGISTRY_KEY = "SOFTWARE\\VideoLAN\\VLC";
  
  /**
   * The VLC registry key for the installaton directory.
   */
  public static final String VLC_INSTALL_DIR_KEY = "InstallDir";
  
  /**
   * Get the VLC installation directory.
   * 
   * @return fully-qualified directory name
   */
  public static String getVlcInstallDir() {
    return Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, VLC_REGISTRY_KEY, VLC_INSTALL_DIR_KEY);
  }
}
