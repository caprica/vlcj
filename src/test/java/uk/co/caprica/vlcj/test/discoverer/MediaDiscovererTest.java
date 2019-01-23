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

import uk.co.caprica.vlcj.enums.MediaDiscovererCategory;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.discoverer.MediaDiscoverer;
import uk.co.caprica.vlcj.discoverer.MediaDiscovererDescription;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.medialist.MediaListEventAdapter;

import java.util.List;

/**
 * A simple test of the media discoverer.
 */
public class MediaDiscovererTest {

    public static void main(String[] args) throws Exception {
        MediaPlayerFactory factory = new MediaPlayerFactory();

        List<MediaDiscovererDescription> discoverers = factory.discoverers().discoverers(MediaDiscovererCategory.LOCAL_DIRS);
        System.out.println("discoverers=" + discoverers);

        if (discoverers.size() == 0) {
            return;
        }

        for (MediaDiscovererDescription description : discoverers) {
            String name = description.name();
            System.out.println("name=" + name);
            MediaDiscoverer discoverer = factory.discoverers().discoverer(name);
            System.out.println("discoverer=" + discoverer);

            MediaList list = discoverer.mediaList();
            System.out.println("read only = " + list.items().isReadOnly());

            list.events().addMediaListEventListener(new MediaListEventAdapter() {
                @Override
                public void mediaListWillAddItem(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
                    System.out.println("will add " + mediaInstance);
                }

                @Override
                public void mediaListItemAdded(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
                    System.out.println("added " + mediaInstance);
                }

                @Override
                public void mediaListWillDeleteItem(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
                    System.out.println("will delete " + mediaInstance);
                }

                @Override
                public void mediaListItemDeleted(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
                    System.out.println("deleted " + mediaInstance);
                }
            });

            boolean started = discoverer.start();
            System.out.println("started=" + started);
        }

        Thread.currentThread().join();
    }

}
