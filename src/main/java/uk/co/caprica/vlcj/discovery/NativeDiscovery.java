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

package uk.co.caprica.vlcj.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.Info;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.discovery.linux.DefaultLinuxNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.discovery.mac.DefaultMacNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.discovery.windows.DefaultWindowsNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.NativeLibrary;

/**
 * A component that uses discovery strategies to locate the libvlc native
 * libraries and register their location with the JNA run-time.
 * <p>
 * To try and automatically locate the libvlc native libraries with the default
 * strategies, at the start of application code simply do:
 * <pre>
 *   new NativeDiscovery().discover();
 * </pre>
 * Custom discovery strategy implementations may of course be used instead of or
 * in addition to the defaults.
 * <p>
 * An alternate approach is to configure a {@link LibVlcFactory} via
 * {@link LibVlcFactory#discovery(NativeDiscovery)}.
 * <p>
 * <em>Discovery should only be executed once, and this should be done at the
 * start of the application - before referencing any other vlcj classes.</em>
 */
public class NativeDiscovery {

    /**
     * Application information.
     */
    static {
        Info.getInstance();
    }

    /**
     * Name of the system property that JNA uses to find native libraries.
     */
    private static final String JNA_SYSTEM_PROPERTY_NAME = "jna.library.path";

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(NativeDiscovery.class);

    /**
     * Strategy implementations.
     */
    private final NativeDiscoveryStrategy[] discoveryStrategies;

    /**
     * Create a discovery component with bespoke strategies.
     *
     * @param discoveryStrategies strategy implementations
     */
    public NativeDiscovery(NativeDiscoveryStrategy... discoveryStrategies) {
        this.discoveryStrategies = discoveryStrategies;
    }

    /**
     * Create a discovery component with the default platform strategies.
     */
    public NativeDiscovery() {
        this(
            new DefaultLinuxNativeDiscoveryStrategy(),
            new DefaultWindowsNativeDiscoveryStrategy(),
            new DefaultMacNativeDiscoveryStrategy()
        );
    }

    /**
     * Attempt to discover the location of the libvlc native libraries.
     * <p>
     * If the native libraries were found, the directory reported to be containing
     * those libraries is explicitly added to the JNA native library search path.
     *
     * @return <code>true</code> if the native libraries were found; otherwise <code>false</code>
     */
    public final boolean discover() {
        logger.debug("discover()");
        // Check if the JNA system property is set - if so, this trumps discovery
        String jnaLibraryPath = System.getProperty(JNA_SYSTEM_PROPERTY_NAME);
        logger.debug("jnaLibraryPath={}", jnaLibraryPath);
        // JNA system property not set...
        if(jnaLibraryPath == null) {
            // Try each strategy in turn...
            for(NativeDiscoveryStrategy discoveryStrategy : discoveryStrategies) {
                logger.debug("discoveryStrategy={}", discoveryStrategy);
                // Is this strategy supported for this run-time?
                boolean supported = discoveryStrategy.supported();
                logger.debug("supported={}", supported);
                if(supported) {
                    String path = discoveryStrategy.discover();
                    logger.debug("path={}", path);
                    if(path != null) {
                        logger.info("Discovery found libvlc at '{}'", path);
                        // Register the discovered library path with JNA
                        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), path);
                        // Chance for post processing
                        discoveryStrategy.onFound(path);
                        return true;
                    }
                }
            }
            logger.warn("Discovery did not find libvlc");
        }
        else {
            logger.info("Skipped discovery as system property '{}' already set to '{}'", JNA_SYSTEM_PROPERTY_NAME, jnaLibraryPath);
        }
        return false;
    }
}
