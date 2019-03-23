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

package uk.co.caprica.vlcj.test.meta;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.MediaEventListener;
import uk.co.caprica.vlcj.media.MediaParsedStatus;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.media.ParseFlag;
import uk.co.caprica.vlcj.test.VlcjTest;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Search one or more directories for audio files to extract mp3 tags (exposed as meta data).
 * <p>
 * Specify one or more directory names on the command line and they will be scanned for mp3 files
 * and the mp3 tags displayed.
 */
public class SearchMetaTest extends VlcjTest {

    private static CountDownLatch sync;

    private static List<MetaData> result;

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

    public static void main(String[] args) throws Exception {
        if(args.length == 0) {
            System.out.println("Specify at least one directory");
            System.exit(1);
        }

        MediaPlayerFactory factory = new MediaPlayerFactory();

        List<File> files = new ArrayList<File>(400);
        for(String arg : args) {
            files.addAll(scan(new File(arg)));
        }

        result = new ArrayList<MetaData>(files.size());

        sync = new CountDownLatch(files.size());

        MediaEventListener listener = new ParseListener();

        final List<Media> mediaList = new ArrayList<Media>(files.size());
        for (File file : files) {
            Media media = factory.media().newMedia(file.getAbsolutePath());
            mediaList.add(media);
            media.events().addMediaEventListener(listener);
            media.parsing().parse(ParseFlag.FETCH_LOCAL);
        }

        System.out.println("Waiting...");

        sync.await();

        System.out.println("Finished!");

        // Dump out the meta
        for(MetaData meta : result) {
            System.out.println(meta);
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

    private static class ParseListener extends MediaEventAdapter {

        @Override
        public void mediaParsedChanged(Media media, MediaParsedStatus newStatus) {
            MetaData metaData = media.meta().asMetaData();
            result.add(metaData);
            sync.countDown();
            media.events().removeMediaEventListener(this);
        }
    }

}
