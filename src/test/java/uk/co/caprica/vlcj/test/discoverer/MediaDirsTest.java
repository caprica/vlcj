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

package uk.co.caprica.vlcj.test.discoverer;

import java.util.List;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListEventListener;
import uk.co.caprica.vlcj.medialist.MediaListItem;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.discoverer.MediaDiscoverer;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple media directories discovery test.
 */
public class MediaDirsTest extends VlcjTest implements MediaListEventListener {

    public static void main(String[] args) throws Exception {
        new MediaDirsTest();
    }

    public MediaDirsTest() throws Exception {
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

        MediaDiscoverer videoMediaDiscoverer = mediaPlayerFactory.newMediaDiscoverer("video_dir");
        Thread.sleep(500); // FIXME not acceptable
        MediaList videoFileList = videoMediaDiscoverer.getMediaList();
//        videoFileList.addMediaListEventListener(this);
        List<MediaListItem> videoFiles = videoFileList.items();
        System.out.println("Video Files:");
        dumpItems(videoFiles, 1);

        System.out.println();

        MediaDiscoverer audioMediaDiscoverer = mediaPlayerFactory.newMediaDiscoverer("audio_dir");
        Thread.sleep(500); // FIXME not acceptable
        MediaList audioFileList = audioMediaDiscoverer.getMediaList();
//        audioFileList.addMediaListEventListener(this);
        List<MediaListItem> audioFiles = audioFileList.items();
        System.out.println("Audio Files:");
        dumpItems(audioFiles, 1);

        System.out.println();

        MediaDiscoverer pictureMediaDiscoverer = mediaPlayerFactory.newMediaDiscoverer("picture_dir");
        Thread.sleep(500); // FIXME not acceptable
        MediaList pictureFileList = pictureMediaDiscoverer.getMediaList();
//        pictureFileList.addMediaListEventListener(this);
        List<MediaListItem> pictureFiles = pictureFileList.items();
        System.out.println("Picture Files:");
        dumpItems(pictureFiles, 1);

        System.out.println("DONE!");

//        Thread.currentThread().join();
        System.exit(0);
    }

    private static void dumpItems(List<MediaListItem> items, int indent) {
        for(MediaListItem item : items) {
            System.out.printf("%" + indent + "s%s%n", " ", item);
            dumpItems(item.subItems(), indent + 1);
        }
    }

    @Override
    public void mediaListWillAddItem(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
    }

    @Override
    public void mediaListItemAdded(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
        System.out.println("ITEM ADDED: " + index + " -> " + mediaList.items());
    }

    @Override
    public void mediaListWillDeleteItem(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
    }

    @Override
    public void mediaListItemDeleted(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
        System.out.println("ITEM DELETED: " + index + " -> " + mediaList.items());
    }
}
