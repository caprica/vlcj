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

package uk.co.caprica.vlcj.discovery.windows;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import uk.co.caprica.vlcj.discovery.StandardNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * This class downloads VLC Portable in the desired version automatically and extracts it into the
 * working directory.
 * 
 * @author MarcMil
 */
public class DownloadWindowsDiscoveryStrategy extends StandardNativeDiscoveryStrategy {
    private DownloadProgressCallBack callback = null;

    private String dlVersion = "2.0.4";

    /**
     * Default constructor
     */
    public DownloadWindowsDiscoveryStrategy() {
    }

    /**
     * Initializes a new instance of DownloadWindowsDiscoveryStrategy
     * 
     * @param callback the callback to call if the download progress changed (can be null)
     */
    public DownloadWindowsDiscoveryStrategy(DownloadProgressCallBack callback) {
        this.callback = callback;
    }

    /**
     * Initializes a new instance of DownloadWindowsDiscoveryStrategy
     * 
     * @param callback the callback to call if the download progress changed (can be null)
     * @param dlVersion the version of vlc to download
     */
    public DownloadWindowsDiscoveryStrategy(DownloadProgressCallBack callback, String dlVersion) {
        this.callback = callback;
        this.dlVersion = dlVersion;
    }

    /**
     * Downloads the data behind the given URL to a desired file
     * 
     * @param download the download URL
     * @param downloadTo the file to download to
     * @throws IOException
     */
    private void download(URL download, File downloadTo) throws IOException {
        URL url = download;
        URLConnection connection = url.openConnection();

        InputStream reader = connection.getInputStream();

        FileOutputStream writer = new FileOutputStream(downloadTo);
        byte[] buffer = new byte[153600];
        int totalBytesRead = 0;
        int bytesRead = 0;

        while((bytesRead = reader.read(buffer)) > 0) {
            writer.write(buffer, 0, bytesRead);
            buffer = new byte[153600];
            totalBytesRead += bytesRead;
            if(callback != null)
                callback.progressChanged(totalBytesRead, connection.getContentLength());
        }

        writer.close();
        reader.close();
        callback.downloadCompleted();
    }

    /**
     * Extracts a given .zip file to the desired path
     * 
     * @param zipFileName the zip file
     * @param path the path
     * @throws IOException
     */
    private static void extractZipFile(String zipFileName, File path) throws IOException {
        ZipFile zipFile = new ZipFile(zipFileName);
        Enumeration<? extends ZipEntry> e = zipFile.entries();

        while(e.hasMoreElements()) {
            ZipEntry entry = (ZipEntry)e.nextElement();
            File destinationFilePath = new File(path, entry.getName());

            destinationFilePath.getParentFile().mkdirs();

            if(entry.isDirectory())
                continue;
            else {
                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

                int bRead;
                byte buffer[] = new byte[1024];
                FileOutputStream fos = new FileOutputStream(destinationFilePath);
                BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

                while((bRead = bis.read(buffer, 0, 1024)) != -1) {
                    bos.write(buffer, 0, bRead);
                }

                bos.flush();
                bos.close();
                bis.close();
            }
        }
        zipFile.close();
    }

    @Override
    protected void onGetDirectoryNames(List<String> directoryNames) {
        try {
            URL downloadFrom = getDownloadURL();
            File file = new File("vlc.zip");
            File fileVlcDir = new File("vlc-" + dlVersion);
            if(!fileVlcDir.exists() || !fileVlcDir.isDirectory()) {
                try {
                    download(downloadFrom, file);
                    extractZipFile("vlc.zip", new File("."));
                    file.delete();
                }
                catch(IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if(fileVlcDir.exists())
                directoryNames.add(fileVlcDir.getAbsolutePath());
        }
        catch(Throwable t) {
        }
    }

    /**
     * Returns the VLC download url
     * 
     * @return the VLC download url
     */
    private URL getDownloadURL() {
        try {
            if(getIs64BitJVM())
                return new URL("http://download.videolan.org/pub/videolan/vlc/" + dlVersion + "/win64/vlc-" + dlVersion + "-win64.zip");
            else
                return new URL("http://download.videolan.org/pub/videolan/vlc/" + dlVersion + "/win32/vlc-" + dlVersion + "-win32.zip");
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean supported() {
        return RuntimeUtil.isWindows();
    }

    /**
     * Returns true if this is (possibly) a 64 bit process
     * 
     * @return true if this is (possibly) a 64 bit process
     */
    public static boolean getIs64BitJVM() {
        final java.lang.String keys[] = {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch",};

        for(java.lang.String key : keys) {
            java.lang.String property = System.getProperty(key);
            if(property != null && !property.isEmpty()) {
                System.out.println(property);
                if(property.indexOf("64") >= 0)
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

}
