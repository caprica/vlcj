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

package uk.co.caprica.vlcj.runtime.install;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import uk.co.caprica.vlcj.logger.Logger;

/**
 * Implementation of a native library manager.
 * <p>
 * This class provides assistance where it is often desirable to include native library resources in
 * a jar file that is provided with the application, enabling the application to execute properly
 * without requiring that the native library resources be installed beforehand.
 * <p>
 * The idea is to package the native library files (in the case of vlc this is the libvlc native
 * library and all of the required plug-in native libraries) into a single jar file, ship that jar
 * file along with the application, and have the application unpack those jars at run-time (or
 * perhaps installation-time).
 * <p>
 * This will not happen automatically, it is up to each individual client application to determine
 * how this class will be used - for example some clients may wish to use this class to unpack and
 * install the native libraries one time only when they install their application, other
 * applications may like to unpack the native libraries each time the application is executed.
 * <p>
 * It is often also often desirable to support multiple platforms and this can be achieved in two
 * ways: either include all native libraries for all platforms in a single jar file, or use a number
 * of platform-specific jar files. So long as the jar file is packaged as expected by this class it
 * does not matter which approach you take.
 * <p>
 * Whatever approach is taken, this class implements the heavy-lifting that is required.
 * <p>
 * The expected structure of the native library package jar files is described below (where
 * {os.name} and {os.arch} are replaced by system properties):
 *
 * <pre>
 *   vlc/{os.name}-{os.arch}/vlc
 *   vlc/{os.name}-{os.arch}/vlc/plugins
 * </pre>
 *
 * For example:
 *
 * <pre>
 *   vlc/linux-amd64/vlc
 *   vlc/linux-amd64/vlc/plugins
 * </pre>
 *
 * Underneath "plugins" is the regular vlc plug-ins directory structure with multiple nested
 * directories.
 * <p>
 * Note that these system properties are converted to lower case, so "Linux" becomes "linux".
 * <p>
 * When unpacked, the structure underneath the "install to" directory will be (in part):
 *
 * <pre>
 *   /libvlc.so
 *   /libvlccore.so
 *   /vlc/
 *   /vlc/plugins/
 * </pre>
 *
 * The prefix <code>vlc/{os.name}-{os.arch}</code> in the package file is stripped from each file
 * name before installing locally.
 * <p>
 * Also as stated above, underneath "plugins" will be the regular vlc plug-in directory structure.
 * <p>
 * The native library search path should therefore be set to the "install to" directory of this
 * native library manager. This is the responsibility of the <em>client</em> application, just
 * because this native library manager unpacks the native libraries does not ensure that they will
 * be picked up and used at run-time.
 * <p>
 * On Linux at least, this is still not yet quite enough - the "install to" directory must be made
 * known by one of the usual mechanisms for setting operating system shared library paths, this
 * includes either:
 * <ul>
 *   <li>Setting the LD_LIBRARY_PATH environment variable to the "install to" directory;</li>
 *   <li>Adding the "install to" directory to the "/etc/ld.so.conf" file;</li>
 *   <li>Adding a new file to "/etc/ld.so.conf.d" that specifies the "install to" directory;</li>
 *   <li>Install the libraries to an already well-known directory such as "/usr/lib".</li>
 * </ul>
 * Of these options, only the first one would not require root privileges.
 * <p>
 * As before, setting the operating system shared library path in this way would be the
 * responsibility of the <em>client</em> application.
 * <p>
 * It is possible to register an event listener to receive progress updates - this could be useful
 * to display during a splash-screen or a dialog box when starting the application.
 * <p>
 * The native library package jars must of course be added to the run-time class-path for the client
 * application.
 * <p>
 * Implementation notes:
 * <p>
 * It may be <em>much</em> faster to purge the installation directory each time (rather than
 * overwriting) before unpacking new resources.
 * <p>
 * Be very careful with {@link #purge()} since it will delete recursively the <em>entire</em>
 * contents of the native library installation directory no matter what it may contain.
 * <p>
 * Users of this class should expect that {@link RuntimeException} will be thrown if an error
 * occurs.
 * <p>
 * Here is a <em>partial</em> example showing the structure required for a native library package
 * jar file:
 *
 * <pre>
 *   vlc/
 *   vlc/linux-amd64/
 *   vlc/linux-amd64/libvlc.so
 *   vlc/linux-amd64/libvlc.so.5
 *   vlc/linux-amd64/libvlc.so.5.1.0
 *   vlc/linux-amd64/libvlccore.so.5
 *   vlc/linux-amd64/libvlccore.so
 *   vlc/linux-amd64/libvlccore.so.5.0.0
 *   vlc/linux-amd64/vlc/
 *   vlc/linux-amd64/vlc/lua/
 *   vlc/linux-amd64/vlc/lua/meta/
 *   vlc/linux-amd64/vlc/lua/meta/fetcher/
 *   vlc/linux-amd64/vlc/lua/meta/fetcher/tvrage.luac
 *   vlc/linux-amd64/vlc/lua/meta/reader/
 *   vlc/linux-amd64/vlc/lua/meta/reader/filename.luac
 *   vlc/linux-amd64/vlc/plugins/
 *   vlc/linux-amd64/vlc/plugins/access/
 *   vlc/linux-amd64/vlc/plugins/access/libaccess_imem_plugin.so
 *   vlc/linux-amd64/vlc/plugins/access/libaccess_rar_plugin.so
 *   ...
 * </pre>
 */
public class NativeLibraryManager {

    /**
     * Top level directory name within the package archive containing the resources to unpack.
     * <p>
     * The trailing slash is required.
     */
    private static final String PACKAGE_RESOURCE_DIRECTORY = "vlc/";

    /**
     * Default file input/output buffer size.
     */
    private static final int IO_BUFFER_SIZE = 1024 * 2;

    /**
     * Name of the installation directory.
     */
    private final String installTo;

    /**
     * File input/output buffer size.
     */
    private final int ioBufferSize;

    /**
     * Collection of registered event listeners.
     */
    private final List<NativeLibraryManagerEventListener> listeners = new ArrayList<NativeLibraryManagerEventListener>(1);

    /**
     * Create a new native library manager.
     *
     * @param installTo name of the directory to install the files to
     */
    public NativeLibraryManager(String installTo) {
        this(installTo, IO_BUFFER_SIZE);
    }

    /**
     * Create a new native library manager.
     *
     * @param installTo name of the directory to install the files to
     * @param ioBufferSize size of the file input/output buffer
     */
    public NativeLibraryManager(String installTo, int ioBufferSize) {
        this.installTo = installTo;
        this.ioBufferSize = ioBufferSize;
    }

    /**
     * Add a listener to be notified of native library manager events.
     *
     * @param listener listener to add
     */
    public void addEventListener(NativeLibraryManagerEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener being notified of native library manager events.
     *
     * @param listener listener to remove
     */
    public void removeEventListener(NativeLibraryManagerEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Get the name of the installation directory.
     *
     * @return installation directory name
     */
    public String getInstallTo() {
        return installTo;
    }

    /**
     * Unpack a package containing native libraries.
     *
     * @throws RuntimeException if an error occurs, such as no native resource package found
     */
    public void unpackNativePackage() {
        Logger.debug("unpackNativePackage()");
        File installDirectory = new File(installTo);
        Logger.debug("installDirectory={}", installDirectory);
        // Get the jar file containing the native resources, the resources are
        // platform-dependent
        String osName = System.getProperty("os.name");
        Logger.debug("osName={}", osName);
        String osArch = System.getProperty("os.arch");
        Logger.debug("osArch={}", osArch);
        String platformResources = String.format("%s%s-%s/", PACKAGE_RESOURCE_DIRECTORY, osName, osArch).toLowerCase();
        Logger.debug("platformResources={}", platformResources);
        JarFile jarFile = getNativePackageJarFile(platformResources);
        Logger.debug("jarFile={}", jarFile);
        // Get the total number of files to install (for reporting progress)
        int installCount = getInstallCount(jarFile);
        Logger.debug("installCount={}", installCount);
        // Notify listeners
        fireStartEvent(installCount);
        // Create a re-usable buffer for file input/output
        byte[] ioBuffer = new byte[ioBufferSize];
        // Process each entry in the jar file
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        jarEntries = jarFile.entries();
        int n = 0;
        while(jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            // Only install files contained in the run-time operating-system name/
            // architecture directory inside the PACKAGE_RESOURCE_DIRECTORY folder
            // inside the jar - this will also ensure that directories like META-INF
            // and files like MANIFEST.MF are excluded...
            if(!jarEntry.isDirectory() && jarEntry.getName().startsWith(platformResources)) {
                // Trim off the un-needed part of the file path
                String entryName = jarEntry.getName().substring(platformResources.length());
                // Notify listeners
                fireInstallEvent( ++ n, entryName);
                // Process this file
                processNativeResource(installDirectory, entryName, jarFile, jarEntry, ioBuffer);
            }
        }
        // Notify listeners
        fireEndEvent();
    }

    /**
     * Purge the native library installation directory.
     * <p>
     * The installation directory itself and all of it's contents (including all nested directories
     * and their own contents) will be deleted.
     * <p>
     * Even if this method returns <code>false</code>, <em>some</em> of the files and directories
     * may have been deleted.
     * <p>
     * <strong>Warning: this will without discrimination delete the entire contents of the native
     * library installation directory.</strong>
     *
     * @return <code>true</code> if all files and directories were deleted, otherwise <code>false</code>
     */
    public boolean purge() {
        Logger.debug("purge()");
        firePurgeEvent();
        boolean result = purge(new File(installTo), true);
        firePurgedEvent(result);
        return result;
    }

    /**
     * Get a jar file containing a vlc native library package from the class-path.
     *
     * @param resourcePath resource path inside the jar file
     * @return jar file
     * @throws RuntimeException if an error occurs, such as no native resource package found
     */
    private JarFile getNativePackageJarFile(String resourcePath) {
        Logger.debug("getNativePackageJarFile()");
        // Try and locate the package for this operating system
        URL vlcPluginsUrl = NativeLibraryManager.class.getClassLoader().getResource(resourcePath);
        Logger.debug("vlcPluginsUrl={}", vlcPluginsUrl);
        if(vlcPluginsUrl == null) {
            throw new RuntimeException("Failed to find a native library resource on the class-path");
        }
        URLConnection urlConnection;
        try {
            urlConnection = vlcPluginsUrl.openConnection();
        }
        catch(IOException e) {
            throw new RuntimeException("Failed to open a the native library class-path resource", e);
        }
        if(!(urlConnection instanceof JarURLConnection)) {
            throw new RuntimeException("Expected the native library resources to be contained in a jar file");
        }
        try {
            return ((JarURLConnection)urlConnection).getJarFile();
        }
        catch(IOException e) {
            throw new RuntimeException("Unable to get the native library class-path resource jar file", e);
        }
    }

    /**
     * Count the number of files to be installed.
     *
     * @param jarFile installation package
     * @return number of files to be installed
     */
    private int getInstallCount(JarFile jarFile) {
        Logger.debug("getInstallCount()");
        int count = 0;
        Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if(!entry.isDirectory() && entry.getName().startsWith(PACKAGE_RESOURCE_DIRECTORY)) {
                count ++ ;
            }
        }
        return count;
    }

    /**
     * Install a file from the installation package.
     *
     * @param installDirectory directory to install the file to
     * @param entryName local file name
     * @param jarFile installation package
     * @param jarEntry entry to install from the install package
     * @param ioBuffer file input/output buffer
     * @return <code>true</code> if a file was installed, otherwise <code>false</code> (e.g. it was a directory)
     * @throws RuntimeException if an error occurs
     */
    private void processNativeResource(File installDirectory, String entryName, JarFile jarFile, JarEntry jarEntry, byte[] ioBuffer) {
        Logger.trace("processNativeResource(entryName={})", entryName);
        // Ensure that the proper directory structure is created first
        File installFile = new File(installDirectory, entryName);
        File parentDirectory = installFile.getParentFile();
        if(parentDirectory != null && !parentDirectory.exists() && !parentDirectory.mkdirs()) {
            Logger.error("Failed to create installation directory '{}'", installFile.getAbsolutePath());
            throw new RuntimeException("Failed to create installation directory '" + installFile.getAbsolutePath() + "'");
        }
        storeResource(installFile, jarFile, jarEntry, ioBuffer);
    }

    /**
     * Save a file from the installation package to the installation directory.
     *
     * @param installFile local installation file
     * @param jarFile installation package
     * @param jarEntry entry to install from the install package
     * @param ioBuffer file input/output buffer
     * @throws RuntimeException if an error occurs
     */
    private void storeResource(File installFile, JarFile jarFile, JarEntry jarEntry, byte[] ioBuffer) {
        Logger.trace("storeResource(installFile={},jarEntry={}", installFile, jarEntry);
        InputStream in = null;
        OutputStream out = null;
        try {
            in = jarFile.getInputStream(jarEntry);
            out = new FileOutputStream(installFile);
            int read;
            for(;;) {
                read = in.read(ioBuffer);
                if(read != -1) {
                    out.write(ioBuffer, 0, read);
                }
                else {
                    break;
                }
            }
        }
        catch(IOException e) {
            Logger.error("Failed to install file '{}' because: {}", installFile, e.getMessage());
            throw new RuntimeException("Failed to install file '" + installFile + "'", e);
        }
        finally {
            if(out != null) {
                try {
                    out.close();
                }
                catch(IOException e) {
                }
            }
            if(in != null) {
                try {
                    in.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * Recursively delete the contents of a directory and the directory itself.
     *
     * @param file file/directory to delete
     * @param result used to keep track of whether all files have been successfully deleted or not
     * @return <code>true</code> if all files and directories successfully deleted, <code>false</code> otherwise
     */
    private boolean purge(File file, boolean result) {
        Logger.trace("purge(file={})", file);
        if(file.isDirectory()) {
            for(File child : file.listFiles()) {
                result = purge(child, result);
            }
        }
        return file.delete() && result;
    }

    /**
     * Notify all registered listeners that installation has started.
     *
     * @param installCount number of files to install
     */
    private void fireStartEvent(int installCount) {
        for(int i = listeners.size() - 1; i >= 0; i -- ) {
            listeners.get(i).start(installTo, installCount);
        }
    }

    /**
     * Notify all registered listeners that a file is being installed.
     *
     * @param number number (not index) of the file
     * @param name name of the file
     */
    private void fireInstallEvent(int number, String name) {
        for(int i = listeners.size() - 1; i >= 0; i -- ) {
            listeners.get(i).install(number, name);
        }
    }

    /**
     * Notify all registered listeners that installation has completed.
     */
    private void fireEndEvent() {
        for(int i = listeners.size() - 1; i >= 0; i -- ) {
            listeners.get(i).end();
        }
    }

    /**
     * Notify all registered listeners that purging has started.
     */
    private void firePurgeEvent() {
        for(int i = listeners.size() - 1; i >= 0; i -- ) {
            listeners.get(i).purge(installTo);
        }
    }

    /**
     * Notify all registered listeners that purging has completed.
     *
     * @param result <code>true</code> if all files and directories were deleted, otherwise <code>false</code>
     */
    private void firePurgedEvent(boolean result) {
        for(int i = listeners.size() - 1; i >= 0; i -- ) {
            listeners.get(i).purged(result);
        }
    }
}
