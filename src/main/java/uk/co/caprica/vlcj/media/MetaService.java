package uk.co.caprica.vlcj.media;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.enums.Meta;
import uk.co.caprica.vlcj.binding.NativeString;

// FIXME need to add some comments regarding fetching remote artwork url (it might happen during parse)
// FIXME need to add about maybe only parsing local files, see parseflags

public class MetaService extends BaseService {

    MetaService(Media media) {
        super(media);
    }

    public String get(Meta meta) {
        return getMetaValue(libvlc.libvlc_media_get_meta(mediaInstance, meta.intValue()));
    }

    public void set(Meta meta, String value) {
        libvlc.libvlc_media_set_meta(mediaInstance, meta.intValue(), value);
    }

    public boolean save() {
        return libvlc.libvlc_media_save_meta(mediaInstance) != 0;
    }

    private String getMetaValue(Pointer pointer) {
        return NativeString.copyAndFreeNativeString(libvlc, pointer);
    }

}
