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
 * Copyright 2009, 2010, 2011 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.x;

import uk.co.caprica.vlcj.binding.LibX11;
import uk.co.caprica.vlcj.log.Logger;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * A helper class that provides a method to safely try and initialise threads
 * for the LibX11 library.
 * <p>
 * This can prevent some fatal native crashes on Linux and related operating
 * systems.
 * <p>
 * This class will fail <em>silently</em> if the initialisation fails, allowing
 * the application to continue normally where X is not available.
 */
public class LibXUtil {

  /**
   * Attempt to initialise LibX threads.
   * <p>
   * It is safe to invoke this on any operating system and it will silently
   * fail if X is not supported.
   * <p>
   * This <strong>should not</strong> be required, but in practice it may be
   * useful.
   */
  public static void initialise() {
    try {
      LibX11.INSTANCE.XInitThreads();
    }
    catch(Throwable t) {
      if(!RuntimeUtil.isWindows()) {
        Logger.trace("Did not initialise LibX11: {}", t.getMessage());
      }
    }
  }
}
