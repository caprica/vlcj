package uk.co.caprica.vlcj.factory;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.vlcj.binding.internal.*;
import uk.co.caprica.vlcj.binding.support.size_t;
import uk.co.caprica.vlcj.discoverer.MediaDiscoverer;
import uk.co.caprica.vlcj.discoverer.MediaDiscovererDescription;
import uk.co.caprica.vlcj.enums.MediaDiscovererCategory;

import java.util.ArrayList;
import java.util.List;

public final class DiscovererService extends BaseService {

    DiscovererService(MediaPlayerFactory factory) {
        super(factory);
    }

    public List<MediaDiscovererDescription> discoverers(MediaDiscovererCategory category) {
        PointerByReference ref = new PointerByReference();
        size_t size = libvlc.libvlc_media_discoverer_list_get(instance, category.intValue(), ref);
        try {
            int count = size.intValue();
            List<MediaDiscovererDescription> result = new ArrayList<MediaDiscovererDescription>(count);
            if (count > 0) {
                Pointer[] pointers = ref.getValue().getPointerArray(0, count);
                for (Pointer pointer : pointers) {
                    libvlc_media_discoverer_description_t description = Structure.newInstance(libvlc_media_discoverer_description_t.class, pointer);
                    description.read();
                    result.add(new MediaDiscovererDescription(description.psz_name, description.psz_longname, MediaDiscovererCategory.mediaDiscovererCategory(description.i_cat)));
                }
            }
            return result;
        }
        finally {
            libvlc.libvlc_renderer_discoverer_list_release(ref.getValue(), size);
        }
    }

    public MediaDiscoverer discoverer(String name) {
        libvlc_media_discoverer_t discoverer = libvlc.libvlc_media_discoverer_new(instance, name);
        if (discoverer != null) {
            return new MediaDiscoverer(libvlc, discoverer);
        } else {
            return null;
        }
    }

}
