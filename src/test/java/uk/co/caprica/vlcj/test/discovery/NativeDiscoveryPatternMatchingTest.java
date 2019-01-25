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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.discovery;

import java.io.File;

public class NativeDiscoveryPatternMatchingTest {

    private static final File testDirectory = new File(System.getProperty("java.io.tmpdir") + "/vlcj-test");

    private static void setUp() {
        testDirectory.mkdirs();
        deleteTestFiles();
    }

    private static void tearDown() {
        deleteTestFiles();
        testDirectory.delete();
    }

    private static void deleteTestFiles() {
        if (testDirectory != null) {
            File[] toDelete = testDirectory.listFiles();
            if (toDelete != null) {
                for (File file : toDelete) {
                    file.delete();
                }
            }
        }
    }
/*
    private static class TestStrategy extends LinuxNativeDiscoveryStrategy {

        private final List<File> files = new ArrayList<File>();



        private TestStrategy(String... fileNames) throws IOException {
            for (String fileName : fileNames) {
                files.add(new File(testDirectory, fileName));
            }
        }

        @Override
        protected void onGetDirectoryNames(List<String> directoryNames) {
            directoryNames.add(testDirectory.getAbsolutePath());
        }
    }

    public static void main(String[] args) throws Exception {
        if (RuntimeUtil.isNix()) {
            testSuccess();
            testFailure();
        }
    }

    private static void testSuccess() throws Exception {
        setUp();
        try {
            // There is no libvlccore, but multiple libvlc, this test must fail
            if (new NativeDiscovery(new TestStrategy("libvlc.so", "libvlc.so.5", "libvlc.so.5.6.0")).discover() != false) {
                System.out.println("Discovery must fail [FAIL]");
            }
            else {
                System.out.println("Discovery failed [OK]");
            }
        }
        finally {
            tearDown();
        }
    }

    private static void testFailure() throws Exception {
        setUp();
        try {
            // There is at least one libvlc and at least one libvlccore, this test must pass
            if (new NativeDiscovery(new TestStrategy("libvlc.so", "libvlc.so.5", "libvlc.so.5.6.0", "libvlccore.so")).discover() != false) {
                System.out.println("Discovery must succeed [FAIL]");
            }
            else {
                System.out.println("Discovery succeeded [OK]");
            }
        }
        finally {
            tearDown();
        }
    }*/
}
