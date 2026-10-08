package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.media.Media;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_save_meta;

public final class MetaApi extends BaseApi {

    MetaApi(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Save the metadata to the underlying media.
     *
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean save(Media media) {
        return libvlc_media_save_meta(libvlcInstance, media.mediaInstance()) != 0;
    }
}
