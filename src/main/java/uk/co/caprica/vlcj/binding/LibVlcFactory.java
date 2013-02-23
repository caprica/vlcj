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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.binding;

import java.lang.reflect.Proxy;
import java.text.MessageFormat;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.version.Version;

/**
 * A factory that creates interfaces to the libvlc native library.
 * <p>
 * For example:
 *
 * <pre>
 * LibVlc libvlc = LibVlcFactory.factory().create();
 * </pre>
 *
 * Or:
 *
 * <pre>
 * LibVlc libvlc = LibVlcFactory.factory().atLeast(&quot;2.0.0&quot;).log().create();
 * </pre>
 */
public class LibVlcFactory {

    /**
     * Help text if the native library failed to load.
     */
    private static final String NATIVE_LIBRARY_HELP =
        "Failed to load the native library.\n\n" +
        "The error was \"{0}\".\n\n" +
        "The required native libraries are named \"{1}\" and \"{2}\".\n\n" +
        "In the text below <libvlc-path> represents the name of the directory containing \"{1}\" and \"{2}\"...\n\n" +
        "There are a number of different ways to specify where to find the native libraries:\n" +
        " 1. Include NativeLibrary.addSearchPath(\"{3}\", \"<libvlc-path>\"); at the start of your application code.\n" +
        " 2. Include System.setProperty(\"jna.library.path\", \"<libvlc-path>\"); at the start of your application code.\n" +
        " 3. Specify -Djna.library.path=<libvlc-path> on the command-line when starting your application.\n" +
        " 4. Add <libvlc-path> to the system search path (and reboot).\n\n" +
        "If this still does not work, then it may be necessary to explicitly add the native library directory to the operating\n" +
        "system configuration - e.g. on Linux this might mean setting the LD_LIBRARY_PATH environment variable, or adding\n" +
        "configuration to the \"/etc/ld.so.conf\" file or the \"/etc/ld.so.conf.d\" directory. Of these options, setting\n" +
        "LD_LIBRARY_PATH is the only one that would not require root privileges.\n\n" +
        "Finally, it is not possible to mix CPU architectures - it is not possible for a 64-bit Java Virtual Machine to load\n" +
        "32-bit native libraries.\n\n" +
        "More information may be available in the log, specify -Dvlcj.log=DEBUG on the command-line when starting your application.\n";

    /**
     * True if the access to the native library should be synchronised.
     */
    private boolean synchronise;

    /**
     * True if the access to the native library should be logged.
     */
    private boolean log;

    /**
     * At least this native library version is required.
     */
    private Version requiredVersion;

    /**
     * Component used to search for libvlc native libraries.
     */
    private NativeDiscovery discovery;

    /**
     * Private constructor prevents direct instantiation by others.
     */
    private LibVlcFactory() {
    }

    /**
     * Create a new factory instance
     *
     * @return factory
     */
    public static LibVlcFactory factory() {
        return new LibVlcFactory();
    }

    /**
     * Request that the libvlc native library instance be synchronised.
     *
     * @return factory
     */
    public LibVlcFactory synchronise() {
        this.synchronise = true;
        return this;
    }

    /**
     * Request that the libvlc native library instance be logged.
     *
     * @return factory
     */
    public LibVlcFactory log() {
        this.log = true;
        return this;
    }

    /**
     * Request that the libvlc native library be of at least a particular version.
     *
     * @param version required version
     * @return factory
     */
    public LibVlcFactory atLeast(String version) {
        this.requiredVersion = new Version(version);
        return this;
    }

    /**
     * Request that automatic discovery of the native libraries be tried.
     *
     * @param discovery discovery
     * @return factory
     */
    public LibVlcFactory discovery(NativeDiscovery discovery) {
        this.discovery = discovery;
        return this;
    }

    /**
     * Create a new libvlc native library instance.
     *
     * @return native library instance
     * @throws RuntimeException if a minimum version check was specified and failed
     */
    public LibVlc create() {
        // Invoke discovery?
        if(discovery != null) {
            discovery.discover();
        }
        // Synchronised or not...
        try {
            LibVlc instance = synchronise ? LibVlc.SYNC_INSTANCE : LibVlc.INSTANCE;
            // Logged...
            if(log) {
                instance = (LibVlc)Proxy.newProxyInstance(LibVlc.class.getClassLoader(), new Class<?>[] {LibVlc.class}, new LoggingProxy(instance));
            }
            String nativeVersion = instance.libvlc_get_version();
            Logger.info("vlc: {}, changeset {}", nativeVersion, LibVlc.INSTANCE.libvlc_get_changeset());
            Logger.info("libvlc: {}", getNativeLibraryPath(instance));
            if(requiredVersion != null) {
                Version actualVersion;
                try {
                    actualVersion = new Version(nativeVersion);
                }
                catch(Exception e) {
                    Logger.error("Unable to parse native library version {} because of {}", nativeVersion, e);
                    actualVersion = null;
                }
                if(actualVersion != null) {
                    if(!actualVersion.atLeast(requiredVersion)) {
                        Logger.fatal("This version of vlcj requires version {} or later of libvlc, found too old version {}", requiredVersion, actualVersion);
                        throw new RuntimeException("This version of vlcj requires version " + requiredVersion + " or later of libvlc, found too old version " + actualVersion + ".");
                    }
                }
                else {
                    Logger.fatal("Unable to check the native library version '{}'", nativeVersion);
                    throw new RuntimeException("Unable to check the native library version " + nativeVersion);
                }
            }
            return instance;
        }
        catch(UnsatisfiedLinkError e) {
            Logger.error("Failed to load native library");
            String msg = MessageFormat.format(NATIVE_LIBRARY_HELP, new Object[] {e.getMessage(), RuntimeUtil.getLibVlcName(), RuntimeUtil.getLibVlcCoreName(), RuntimeUtil.getLibVlcLibraryName()});
            throw new RuntimeException(msg);
        }
    }

    /**
     * Parse out the complete file path of the native library.
     * <p>
     * This depends on the format of the toString() of the JNA implementation class.
     *
     * @param library native library instance
     * @return native library path, or simply the toString of the instance if the path could not be parsed out
     */
    private static String getNativeLibraryPath(Object library) {
        String s = library.toString();
        int start = s.indexOf('<');
        if(start != -1) {
            start ++ ;
            int end = s.indexOf('@', start);
            if(end != -1) {
                s = s.substring(start, end);
                return s;
            }
        }
        return s;
    }
}
