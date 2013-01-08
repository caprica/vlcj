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

import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListItem;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.discoverer.MediaDiscoverer;

/**
 * Simple capture devices test.
 */
public class CaptureDevicesTest {

    public static void main(String[] args) {
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

        MediaDiscoverer audioMediaDiscoverer = mediaPlayerFactory.newAudioMediaDiscoverer();
        MediaList audioDeviceList = audioMediaDiscoverer.getMediaList();
        List<MediaListItem> audioDevices = audioDeviceList.items();
        System.out.println("Audio Devices:");
        dumpItems(audioDevices, 1);

        System.out.println();

        MediaDiscoverer videoMediaDiscoverer = mediaPlayerFactory.newVideoMediaDiscoverer();
        MediaList videoDeviceList = videoMediaDiscoverer.getMediaList();
        List<MediaListItem> videoDevices = videoDeviceList.items();
        System.out.println("Video Devices:");
        dumpItems(videoDevices, 1);
    }

    private static void dumpItems(List<MediaListItem> items, int indent) {
        for(MediaListItem item : items) {
            System.out.printf("%" + indent + "s%s%n", " ", item);
            dumpItems(item.subItems(), indent + 1);
        }
    }
}
