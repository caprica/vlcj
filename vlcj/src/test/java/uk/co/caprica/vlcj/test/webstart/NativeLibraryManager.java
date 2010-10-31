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
 * Copyright 2009, 2010 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.webstart;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

// TODO maybe smarter to support multiple architectures etc, archive url would depend on architecture
// TODO support caching somehow, and per architecture
// TODO need progress feedback e.g. for splash-screen or loading screen

/**
 * Ordinarily with Java WebStart you would simply use the regular WebStart 
 * resource mechanism. The problem is that in the case of libvlc this would
 * fail due to the way libvlc locates it's plugin libraries.
 * <p>
 * For this reason, the only available solution is to download the native
 * libraries and unpack them to a known local directory. The client application
 * then uses the "--plugin-path" argument to tell libvlc where the plugins are.
 * <p>
 * The native libraries are expected to be contained entirely within the single
 * root directory of a regular Java jar file.
 */
public class NativeLibraryManager {

  /**
   * Local directory to store the native libraries in.
   */
  private File localDirectory;
  
  /**
   * IO buffer.
   */
  private byte[] buffer;
  
  /**
   * Construct a new native library manager.
   * 
   * @param localDirectory local directory to store the native libraries in
   * @param bufferSize IO buffer size
   */
  public NativeLibraryManager(File localDirectory, int bufferSize) {
    this.localDirectory = localDirectory;
    this.buffer = new byte[bufferSize];
    if(!this.localDirectory.exists()) {
      this.localDirectory.mkdirs();
    }
  }
  
  /**
   * Fetch, and unpack, the native library resource at the specified URL.
   * <p>
   * The URL should point to a regular Java jar file containing all of the
   * needed native libraries in the root directory of that jar file.
   * 
   * @param resourceUrl URL of the remote resource
   * @throws Exception if an error occurs
   */
  public void fetch(String resourceUrl) throws Exception {
    File resourceArchiveFile = downloadResourceArchive(resourceUrl);
    unpackResources(resourceArchiveFile);
  }
  
  /**
   * Download the remote native library container archive.
   * 
   * @param resourceUrl URL of the remote resource
   * @return temporary local downloaded file, this will be automatically deleted when the VM exits 
   * @throws Exception if an error occurs
   */
  private File downloadResourceArchive(String resourceUrl) throws Exception {
    URL url = new URL(resourceUrl);
    BufferedInputStream in = new BufferedInputStream(url.openStream());
    File tempFile = File.createTempFile("vlcj", "jar", localDirectory);
    tempFile.deleteOnExit();
    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));
    copyStream(in, out, buffer);
    out.close();
    in.close();
    return tempFile;
  }
   
  /**
   * Unpack the downloaded native library archive.
   * 
   * @param resourceArchiveFile temporary local download file
   * @throws Exception if an error occurs
   */
  private void unpackResources(File resourceArchiveFile) throws Exception {
    JarFile jarFile = new JarFile(resourceArchiveFile);
    Enumeration<JarEntry> en = jarFile.entries();
    BufferedInputStream in;
    BufferedOutputStream out;
    while(en.hasMoreElements()) {
      JarEntry jarEntry = en.nextElement();
      // Exlpicitly exclude the META-INF entry
      if(!"META-INF".equals(jarEntry.getName())) {
        File file = new File(localDirectory, jarEntry.getName());
        if(jarEntry.isDirectory()) {
          // Nothing for any embedded directories
        }
        else {
          if(jarEntry.getName().endsWith(".so")) {
            in = new BufferedInputStream(jarFile.getInputStream(jarEntry));
            out = new BufferedOutputStream(new FileOutputStream(file));
            copyStream(in, out, buffer);
            out.close();
            in.close();
          }
        }
      }
    }
  }
  
  /**
   * Copy a complete stream.
   * 
   * @param in input stream
   * @param out output stream
   * @param buffer IO buffer
   * @throws IOException if an error occurs
   */
  private void copyStream(BufferedInputStream in, BufferedOutputStream out, byte[] buffer) throws IOException {
    int read;
    for(;;) {
      read = in.read(buffer);
      if(read > 0) {
        out.write(buffer, 0, read);
        continue;
      }
      break;
    }
  }
}
