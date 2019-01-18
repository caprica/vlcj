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
