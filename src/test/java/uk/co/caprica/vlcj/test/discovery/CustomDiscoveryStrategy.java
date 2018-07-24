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
 * Copyright 2009-2018 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.discovery;

import java.util.List;

import java.io.IOException;
import java.io.File;

import uk.co.caprica.vlcj.discovery.linux.DefaultLinuxNativeDiscoveryStrategy;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class CustomDiscoveryStrategy extends DefaultLinuxNativeDiscoveryStrategy {

    private void touch(File file) {
        try {
            file.createNewFile();
        }
        catch(IOException ex) {
            throw new RuntimeException(ex);
        }
        if (!file.isFile()) {
            throw new RuntimeException("Not a file: " + file.getAbsolutePath());
        }
    }
    
    @Override
    protected void onGetDirectoryNames(List<String> directoryNames) {

        // linux would require two libs: libvlccore and libvlc. we're only providing
        // one here, so that library should be discovered
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File pseudoLibDir = new File(tempDir, "custom-discovery-strategy");
        pseudoLibDir.mkdirs();
        if (!pseudoLibDir.isDirectory()) {
            throw new RuntimeException("Not a file: " + pseudoLibDir.getAbsolutePath());
        }
        touch(new File(pseudoLibDir, "libvlc.so"));
        touch(new File(pseudoLibDir, "libvlc.so.5"));
        touch(new File(pseudoLibDir, "libvlc.so.5.6.0"));
        directoryNames.add(pseudoLibDir.getAbsolutePath());
        
    }
    
    public static void main(String[] args) throws Exception {
        CustomDiscoveryStrategy strategy   = new CustomDiscoveryStrategy();
        String                  discovered = strategy.discover();
        if (discovered != null) {
            // we shouldn't discover something here
            throw new RuntimeException("Detected library despite being  incomplete (" + discovered + ")");
        }
    }

} /* ENDCLASS */
