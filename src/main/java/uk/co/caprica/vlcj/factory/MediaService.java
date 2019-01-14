package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_type_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_track_type_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.DefaultMediaMeta;
import uk.co.caprica.vlcj.player.MediaMeta;

public final class MediaService extends BaseService {

    MediaService(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Create a new media list for a play-list media player.
     *
     * @return media list instance
     */
    public MediaList newMediaList() {
        return new MediaList(libvlc, instance);
    }

    /**
     * Get local media meta data.
     * <p>
     * Note that requesting meta data may cause one or more HTTP connections to
     * be made to external web-sites to attempt download of album art.
     * <p>
     * This method should <strong>not</strong> be invoked for non-local MRL's
     * like streaming network addresses.
     *
     * @param mediaPath path to the local media
     * @param parse <code>true</code> if the media should be parsed immediately; otherwise <code>false</code>
     * @return media meta data, or <code>null</code> if the media could not be located
     */
    public MediaMeta getMediaMeta(String mediaPath, boolean parse) {
        libvlc_media_t media = libvlc.libvlc_media_new_path(instance, mediaPath);
        if (media != null) {
            if(parse) {
                // FIXME, options come from libvlc_parse_flag_t, timeout  0 means indefinite
                libvlc.libvlc_media_parse_with_options(media, 0, 0);
            }
            MediaMeta mediaMeta = new DefaultMediaMeta(libvlc, media);
            // Release this native reference, the media meta instance retains its own native reference
            libvlc.libvlc_media_release(media);
            return mediaMeta;
        } else {
            return null;
        }
    }

    /**
     * Get the media type for the media.
     * <p>
     * This is a medium type rather than e.g. a specific file type.
     * <p>
     * Requires LibVLC 3.0.0 or later.
     *
     * @param media media
     * @return media type, or <code>null</code> if <code>media</code> is <code>null</code>
     */
    public libvlc_media_type_e getMediaType(libvlc_media_t media) {
        if (media != null) {
            return libvlc_media_type_e.mediaType(libvlc.libvlc_media_get_type(media));
        }
        else {
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
    public String getCodecDescription(libvlc_track_type_t type, int codec) {
        return libvlc.libvlc_media_get_codec_description(type.intValue(), codec);
    }

}
