package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.enums.TrackType;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaResourceLocator;
import uk.co.caprica.vlcj.player.media.callback.CallbackMedia;

// FIXME getcodecdescription has a weak argument for being in here

public final class MediaService extends BaseService {

    MediaService(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     *
     *
     * @param mrl
     * @return
     */
    // FIXME rename fromMrl or forMrl? or just from(...)
    public Media newMedia(String mrl) {
        mrl = MediaResourceLocator.encodeMrl(mrl);
        libvlc_media_t media = MediaResourceLocator.isLocation(mrl) ? libvlc.libvlc_media_new_location(instance, mrl) : libvlc.libvlc_media_new_path(instance, mrl);
        if (media != null) {
            return new Media(libvlc, media);
        } else {
            return null;
        }
    }

    /**
     *
     *
     * <em>The calling application must make sure to keep hard references to the callback implementation objects to
     * prevent them from being garbage collected, otherwise a fatal JVM crash may occur.</em>
     *
     * @param callbackMedia
     * @return
     */
    // FIXME rename forCallbacks? or just from(...)
    public Media newMedia(CallbackMedia callbackMedia) {
        libvlc_media_t media = libvlc.libvlc_media_new_callbacks(instance,
            callbackMedia.getOpen(),
            callbackMedia.getRead(),
            callbackMedia.getSeek(),
            callbackMedia.getClose(),
            callbackMedia.getOpaque()
        );
        if (media != null) {
            return new Media(libvlc, media);
        } else {
            return null;
        }
    }

    /**
     * Create a new media list for a play-list media player.
     *
     * @return media list instance
     */
    public MediaList newMediaList() {
        libvlc_media_list_t mediaList = libvlc.libvlc_media_list_new(instance);
        if (mediaList != null) {
            return new MediaList(libvlc, mediaList);
        } else {
            return null;
        }
    }

    /**
     * Get a description for a particular codec value.
     *
     * @param type type of track
     * @param codec codec value (or codec FourCC)
     * @return codec description
     *
     * @since libvlc 3.0.0
     */
    public String getCodecDescription(TrackType type, int codec) {
        return libvlc.libvlc_media_get_codec_description(type.intValue(), codec);
    }

}
