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

package uk.co.caprica.vlcj;

import java.util.Properties;

import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.version.Version;

/**
 * Application information banner.
 */
public class Info {

    /**
     * Application name.
     */
    private static final String APP_MSG =
        "       _       _"    + "\n" +
        "__   _| | ___ (_)"   + "\n" +
        "\\ \\ / / |/ __|| |" + "\n" +
        " \\ V /| | (__ | |"  + "\n" +
        "  \\_/ |_|\\___|/ | 2.1.0" + "\n" +
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
        "Copyright 2009, 2010, 2011, 2012 Caprica Software Limited."           + "\n";
      
    /**
     * Singleton holder.
     */
    private static class InfoHolder {

        /**
         * Singleton instance.
         */
        public static final Info INSTANCE = new Info();
    }

    /**
     * Get application information.
     * 
     * @return
     */
    public static Info getInstance() {
        return InfoHolder.INSTANCE;
    }

    /**
     * vlcj version.
     */
    private Version version;

    /**
     * Private constructor.
     */
    private Info() {
        System.err.println(APP_MSG);
        System.err.println(LICENSE_MSG);
        System.err.flush();
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("/build.properties"));
            version = new Version(properties.getProperty("build.version"));
        }
        catch(Throwable t) {
            // This can only happen if something went wrong with the build
            version = null;
        }
        Logger.info("vlcj: {}", version != null ? version : "<version not available>");
        Logger.info("java: {} {}", System.getProperty("java.version"), System.getProperty("java.vendor"));
        Logger.info("java home: {}", System.getProperty("java.home"));
        Logger.info("os: {} {} {}", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
    }
    
    /**
     * Get the vlcj version.
     * 
     * @return version
     */
    public final Version version() {
        return version;
    }
}
