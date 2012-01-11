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
 * Copyright 2009, 2010, 2011, 2012 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.binding;

import uk.co.caprica.vlcj.logger.Logger;

/**
 * Application information banner.
 */
class Info {

  /**
   * Application name.
   */
  private static final String APP_MSG =
    "       _       _"    + "\n" +
    "__   _| | ___ (_)"   + "\n" +
    "\\ \\ / / |/ __|| |" + "\n" +
    " \\ V /| | (__ | |"  + "\n" +
    "  \\_/ |_|\\___|/ | 1.3.0-b1" + "\n" +
    "            |__/  www.capricasoftware.co.uk" + "\n";

  /**
   * Application license (GPL3) summary text.
   */
  private static final String LICENSE_MSG =
    "VLCJ is free software: you can redistribute it and/or modify"         + "\n" + 
    "it under the terms of the GNU General Public License as published by" + "\n" +
    "the Free Software Foundation, either version 3 of the License, or"    + "\n" +
    "(at your option) any later version."                                  + "\n" +    
    ""                                                                     + "\n" +
    "VLCJ is distributed in the hope that it will be useful,"              + "\n" +
    "but WITHOUT ANY WARRANTY; without even the implied warranty of"       + "\n" +
    "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the"        + "\n" +
    "GNU General Public License for more details."                         + "\n" +
    ""                                                                     + "\n" +
    "You should have received a copy of the GNU General Public License"    + "\n" +
    "along with VLCJ.  If not, see <http://www.gnu.org/licenses/>."        + "\n" +
    ""                                                                     + "\n" +
    "Copyright 2009, 2010, 2011, 2012 Caprica Software Limited."                 + "\n";
      
  /**
   * Package-private constructor.
   */
  Info() {
    System.err.println(APP_MSG);
    System.err.println(LICENSE_MSG);
    System.err.flush();
    Logger.info("vlcj: 1.3.0-b1");
    Logger.info("java: {} {}", System.getProperty("java.version"), System.getProperty("java.vendor"));
    Logger.info("java home: {}", System.getProperty("java.home"));
    Logger.info("os: {} {} {}", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
  }
}
