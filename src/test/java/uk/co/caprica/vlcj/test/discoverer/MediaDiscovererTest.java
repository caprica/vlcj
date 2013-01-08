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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListItem;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.discoverer.MediaDiscoverer;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Test of basic media discoverer functionality.
 * <p>
 * This test simply dumps out the (possibly nested) list of audio/video devices.
 * <p>
 * There are other native media discoverers available (depending on available vlc
 * service discovery plugins) but audio and video at least seem reliable - the
 * others may not be as reliable.
 */
public class MediaDiscovererTest extends VlcjTest {

    private static final String[] NAMES = {
        "audio",
        "video",
    };

    private final MediaPlayerFactory mediaPlayerFactory;

    private final Map<String, MediaDiscoverer> discoverers = new LinkedHashMap<String, MediaDiscoverer>();

    public static void main(String[] args) throws Exception {
        new MediaDiscovererTest().run();
    }

    public MediaDiscovererTest() {
        this.mediaPlayerFactory = new MediaPlayerFactory();
    }

    private void run() {
        for(String name : NAMES) {
            System.out.println("Creating discoverer for '" + name + "'");
            MediaDiscoverer discoverer = mediaPlayerFactory.newMediaDiscoverer(name);
            discoverers.put(name, discoverer);
        }
        System.out.println();

        refresh();

        for(String name : NAMES) {
            System.out.println("Releasing '" + name + "'");
            MediaDiscoverer discoverer = discoverers.get(name);
            discoverer.release();
        }
    }

    private void refresh() {
        for(String name : NAMES) {
            System.out.println("Testing '" + name + "'");
            MediaDiscoverer discoverer = discoverers.get(name);
            MediaList mediaList = discoverer.getMediaList();
            List<MediaListItem> items = mediaList.items();
            dumpItems(items, 1);
            System.out.println();
        }
    }

    private void dumpItems(List<MediaListItem> items, int indent) {
        for(MediaListItem item : items) {
            System.out.printf("%" + indent + "s%s%n", " ", item);
            dumpItems(item.subItems(), indent+1);
        }
    }
}
