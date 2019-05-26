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

package uk.co.caprica.vlcj.test.discoverer;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.discoverer.MediaDiscoverer;
import uk.co.caprica.vlcj.media.discoverer.MediaDiscovererCategory;
import uk.co.caprica.vlcj.media.discoverer.MediaDiscovererDescription;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListEventAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple test of the media discoverer.
 *
 * Is 3.0.4 bugged and return duplicate services?
 *
 * It does not seem to reflect any updates to the list, unfortunately, so adding/removing local files won't be
 * reflected.
 */
public class MediaDiscovererTest {

    private static List<MediaDiscoverer> allDiscoverers = new ArrayList<MediaDiscoverer>();

    private static List<MediaList> allLists = new ArrayList<MediaList>();

    public static void main(String[] args) throws Exception {
        MediaPlayerFactory factory = new MediaPlayerFactory();
        System.out.println(factory.application().version());

        dump(factory, MediaDiscovererCategory.DEVICES);
        dump(factory, MediaDiscovererCategory.LAN);
        dump(factory, MediaDiscovererCategory.PODCASTS);
        dump(factory, MediaDiscovererCategory.LOCAL_DIRS);

        List<MediaDiscovererDescription> discoverers = factory.mediaDiscoverers().discoverers(MediaDiscovererCategory.LOCAL_DIRS);
        System.out.println("discoverers=" + discoverers);

        if (discoverers.size() == 0) {
            return;
        }

        for (MediaDiscovererDescription description : discoverers) {
            final String name = description.name();
            System.out.println("name=" + name);
            MediaDiscoverer discoverer = factory.mediaDiscoverers().discoverer(name);
            allDiscoverers.add(discoverer);
            System.out.println("discoverer=" + discoverer);

            // The returned list would ordinarily be freed when it is no longer needed, not so for this test
            MediaList list = discoverer.newMediaList();
            allLists.add(list);
            System.out.println("read only = " + list.media().isReadOnly());

            list.events().addMediaListEventListener(new MediaListEventAdapter() {
                @Override
                public void mediaListItemAdded(MediaList mediaList, MediaRef item, int index) {
                    System.out.println(name + ": added " + item);
                }

                @Override
                public void mediaListItemDeleted(MediaList mediaList, MediaRef item, int index) {
                    System.out.println(name + ": deleted " + item);
                }
            });

            discoverer.start();
        }

        Thread.currentThread().join();
    }

    private static void dump(MediaPlayerFactory factory, MediaDiscovererCategory category) {
        List<MediaDiscovererDescription> discoverers = factory.mediaDiscoverers().discoverers(category);
        System.out.println("discoverers=" + discoverers);

        for (MediaDiscovererDescription description : discoverers) {
            String name = description.name();
            String longName = description.longName();
            MediaDiscovererCategory cat = description.category();
            System.out.printf("[%-12s] %-20s \"%s\"%n", cat, name, longName);
        }

        System.out.println();
    }

}
