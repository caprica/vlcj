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

package uk.co.caprica.vlcj.test.meta;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Search one or more directories for audio files to extract mp3 tags (exposed as meta data).
 * <p>
 * Specify one or more directory names on the command line and they will be scanned for mp3 files
 * and the mp3 tags displayed.
 */
public class SearchMetaTest extends VlcjTest {

    private static final FileFilter AUDIO_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isFile() && pathname.getName().endsWith(".mp3");
        }
    };

    private static final FileFilter DIR_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    };

    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("Specify at least one directory");
            System.exit(1);
        }

        Logger.setLevel(Logger.Level.INFO);

        MediaPlayerFactory factory = new MediaPlayerFactory();

        List<File> files = new ArrayList<File>(400);
        for(String arg : args) {
            files.addAll(scan(new File(arg)));
        }

        // Dump out the meta
        for(File file : files) {
            String mrl = file.getAbsolutePath();
            MediaMeta meta = factory.getMediaMeta(mrl, true);
            Logger.info("{} -> {}", mrl, meta);
            meta.release();
        }

        // Dump out only the title and the length
        for(File file : files) {
            String mrl = file.getAbsolutePath();
            MediaMeta meta = factory.getMediaMeta(mrl, true);
            Logger.info("{} -> {}ms", meta.getTitle(), meta.getLength());
            meta.release();
        }

        factory.release();
    }

    private static List<File> scan(File root) {
        List<File> result = new ArrayList<File>(200);
        scan(root, result);
        return result;
    }

    private static void scan(File root, List<File> result) {
        File[] files = root.listFiles(AUDIO_FILE_FILTER);
        if(files != null) {
            for(File file : files) {
                result.add(file);
            }
            for(File dir : root.listFiles(DIR_FILTER)) {
                scan(dir, result);
            }
        }
    }
}
