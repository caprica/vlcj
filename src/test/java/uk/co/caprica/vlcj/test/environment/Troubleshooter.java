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

package uk.co.caprica.vlcj.test.environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;

/**
 * Trouble-shooter to help diagnose problems trying to load the libvlc native library.
 * <p>
 * This is not exhaustive, but does cover the most common causes of failure.
 * <p>
 * Just run the trouble-shooter class directly to get a report.
 * <p>
 * If you want you can specify -Djna.library.path on the command-line or in your IDE run
 * configuration if you know where libvlc is located on your machine.
 */
public class Troubleshooter {

    private static final String libvlcLibraryName = RuntimeUtil.getLibVlcLibraryName();

    private static final String libvlcFileName = RuntimeUtil.getLibVlcName();
    private static final String libvlcCoreFileName = RuntimeUtil.getLibVlcCoreName();

    private static final List<File> libvlcFound = new ArrayList<File>();
    private static final List<File> libvlcCoreFound = new ArrayList<File>();

    private static String javaVmName;
    private static String osArch;
    private static String sunArchDataModel;

    private static boolean likely64bitOs;
    private static boolean likely64bitVm;

    private static LibVlc libvlc;

    public static void main(String[] args) {
        cut();

        dumpPreamble();

        dumpSystemProperties();

        dumpArch();

        dumpPath();
        dumpJnaPath();
        dumpJniPath();
        dumpLdPath();

        loadLibrary();

        cut();

        dumpConclusions();
    }

    private static void cut() {
        System.out.println("============================== 8< 8< 8< ==============================\n");
    }

    private static void dumpPreamble() {
        System.out.println("Library name: " + libvlcLibraryName);
        System.out.println("libvlc shared object file name: " + libvlcFileName);
        System.out.println("libvlc core shared object file name: " + libvlcCoreFileName);
        System.out.println();
    }

    private static void dumpSystemProperties() {
        System.out.println("SYSTEM PROPERTIES:");
        Properties properties = System.getProperties();
        TreeSet<Object> sortedSet = new TreeSet<Object>(properties.keySet());
        for(Object object : sortedSet) {
            if(!"line.separator".equals(object)) {
                System.out.println(" " + object + "=" + System.getProperty((String)object));
            }
        }
        System.out.println();
    }

    private static void dumpArch() {
        System.out.println("ARCHITECTURE TELLS:");

        javaVmName = System.getProperty("java.vm.name");
        osArch = System.getProperty("os.arch");
        sunArchDataModel = System.getProperty("sun.arch.data.model");

        System.out.println(" java.vm.name=" + (javaVmName != null ? javaVmName : "<not-specified>"));
        System.out.println(" os.arch=" + (osArch != null ? osArch : "<not-specified>"));
        System.out.println(" sun.arch.data.model=" + (sunArchDataModel != null ? sunArchDataModel : "<not-specified>"));

        likely64bitOs = osArch.contains("64");
        likely64bitVm = javaVmName.contains("64");

        System.out.println();
    }

    private static void dumpPath() {
        String value = System.getenv("PATH");
        System.out.println("PATH=" + (value != null ? value : "<not-specified>"));
        dumpPath(value);
        System.out.println();
    }

    private static void dumpJnaPath() {
        String value = System.getProperty("jna.library.path");
        System.out.println("jna.library.path=" + (value != null ? value : "<not-specified>"));
        dumpPath(value);
        System.out.println();
    }

    private static void dumpJniPath() {
        String value = System.getProperty("java.library.path");
        System.out.println("java.library.path=" + (value != null ? value : "<not-specified>"));
        dumpPath(value);
        System.out.println();
        System.out.println("Note that JNA does not use java.library.path, it is searched here for completeness.");
        System.out.println();
    }

    private static void dumpLdPath() {
        String value = System.getProperty("LD_LIBRARY_PATH");
        System.out.println("LD_LIBRARY_PATH=" + (value != null ? value : "<not-specified>"));
        dumpPath(value);
        System.out.println();
    }

    private static void loadLibrary() {
        System.out.println("Load Library:");
        try {
            libvlc = (LibVlc)Native.loadLibrary(libvlcLibraryName, LibVlc.class);
            System.out.println(" Successfully loaded libvlc: " + libvlc);
        }
        catch(Throwable t) {
            System.out.println(" Failed to load libvlc: " + t.getMessage());
        }
        System.out.println();
    }

    private static void dumpPath(String value) {
        if(value != null) {
            String[] paths = value.split(File.pathSeparator);
            for(String path : paths) {
                File dir = new File(path);
                File libvlcFile = new File(dir, libvlcFileName);
                File libvlcCoreFile = new File(dir, libvlcCoreFileName);
                System.out.printf(" %s   %s   %s\n", path, libvlcFile.exists() ? "found libvlc" : "", libvlcCoreFile.exists() ? "found libvlccore" : "");
                if(libvlcFile.exists()) {
                    libvlcFound.add(libvlcFile);
                }
                if(libvlcCoreFile.exists()) {
                    libvlcCoreFound.add(libvlcCoreFile);
                }
            }
        }
    }

    private static void dumpConclusions() {
        System.out.println("Note that CPU architecture detection is an informed *guess*, it may not be accurate.");
        System.out.println();

        if(likely64bitOs) {
            System.out.println("It looks like you have a 64-bit OS because os.arch=" + osArch + ".");
        }
        else {
            System.out.println("It looks like you have a 32-bit OS because os.arch=" + osArch + ".");
        }
        System.out.println();

        if(likely64bitVm) {
            System.out.println("It looks like you have a 64-bit JVM because java.vm.name=" + javaVmName + ".");
        }
        else {
            System.out.println("It looks like you have a 32-bit JVM because java.vm.name=" + javaVmName + ".");
        }
        System.out.println();

        if(likely64bitVm) {
            System.out.println("If you are running a 64-bit JVM, then you need a 64-bit build of vlc.");
        }
        else {
            System.out.println("If you are running a 32-bit JVM, then you need a 32-bit build of vlc.");
        }
        System.out.println();

        if(likely64bitOs && likely64bitVm) {
            System.out.println("If you only have a 32-bit build of vlc, then you can use a 32-bit JVM on your 64-bit OS.");
            System.out.println();
        }

        if(!libvlcFound.isEmpty()) {
            System.out.println("libvlc was found in the following locations:");
            for(File file : libvlcFound) {
                System.out.println(" " + file.getAbsolutePath());
            }
        }
        else {
            System.out.println("libvlc was not found anywhere.");
        }
        System.out.println();
        if(!libvlcFound.isEmpty()) {
            System.out.println("libvlccore was found in the following locations:");
            for(File file : libvlcCoreFound) {
                System.out.println(" " + file.getAbsolutePath());
            }
        }
        else {
            System.out.println("libvlccore was not found anywhere.");
        }
        System.out.println();

        if(libvlc != null) {
            System.out.println("libvlc was successfully loaded here " + libvlc + ", you should be good to go!");
        }
        else {
            System.out.println("libvlc could not be loaded.");
            System.out.println();

            cut();

            if(!libvlcFound.isEmpty() && !libvlcCoreFound.isEmpty()) {
                System.out.println("libvlc and libvlccore were found, so if the JVM CPU architecture matches your vlc build CPU architecture, you should be able to resolve this with configuration...");
                System.out.println();

                System.out.println("Some things to try:");
                System.out.println();

                for(File file : libvlcFound) {
                    System.out.println("Set this system property on the command-line      : -Djna.library.path=" + file.getParent());
                    System.out.println("Set this system property in your Java code        : System.setProperty(\"jna.library.path\", \"" + file.getParent() + "\");");
                    System.out.println("Add this statement to your Java code              : NativeLibrary.addSearchPath(\"" + RuntimeUtil.getLibVlcLibraryName() + "\", \"" + file.getParent() + "\");");
                    if(RuntimeUtil.isNix()) {
                        System.out.println("Set this environment variable on the command-line : LD_LIBRARY_PATH=" + file.getParent());
                        System.out.println("Create a new file in /etc/ld.so.conf.d containing : " + file.getParent());
                    }
                    System.out.println("Add this directory to your operating system path  : " + file.getParent());
                    System.out.println();

                    System.out.println("I will try setting the jna.library.path system property to '" + file.getParent() + "' for you...");

                    System.setProperty("jna.library.path", file.getParent());
                    try {
                        libvlc = (LibVlc)Native.loadLibrary(libvlcLibraryName, LibVlc.class);
                        System.out.println(" Successfully loaded libvlc: " + libvlc);
                        System.out.println();
                        System.out.println(" This WORKED! This means you can load libvlc with the described configuration.");
                    }
                    catch(Throwable t) {
                        System.out.println(" Failed to load libvlc: " + t.getMessage());
                    }
                    System.out.println();
                }
            }
            else {
                System.out.println("Try installing vlc or fixing your OS configuration so that libvlc can be found.");
                System.out.println();
            }
        }
        cut();
    }
}
