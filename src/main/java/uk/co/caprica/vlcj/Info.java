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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.version.Version;

/**
 * Application information banner.
 */
public final class Info {

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(Info.class);

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
     * @return singleton instance
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
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("/uk/co/caprica/vlcj/build.properties"));
            version = new Version(properties.getProperty("build.version"));
        }
        catch(Exception e) {
            // This can only happen if something went wrong with the build
            version = null;
        }
        logger.info("vlcj: {}", version != null ? version : "<version not available>");
        logger.info("java: {} {}", System.getProperty("java.version"), System.getProperty("java.vendor"));
        logger.info("java home: {}", System.getProperty("java.home"));
        logger.info("os: {} {} {}", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
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
