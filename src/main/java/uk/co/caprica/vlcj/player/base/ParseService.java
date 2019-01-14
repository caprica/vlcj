package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_parse_flag_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

public final class ParseService extends BaseService {

    ParseService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Parse local meta data from the current media.
     * <p>
     * This method is synchronous.
     * <p>
     * Parsing media may cause an HTTP request to be made to search for cover- art.
     * <p>
     * <strong>Invoking this method on a stream or DVB channel may cause a hang.</strong>
     */
    // FIXME maybe return boolean
    public void parseMedia() {
        libvlc_media_t media = media();
        if (media != null) {
            // FIXME, options come from libvlc_parse_flag_t, timeout  0 means indefinite
            libvlc.libvlc_media_parse_with_options(media, 0, 0);
        }
        else {
            throw new IllegalStateException("No media"); // FIXME don't like throwing
        }
    }

    /**
     * Parse local meta data from the current media.
     * <p>
     * This method is asynchronous and a media player event will be raised when the parsed status
     * changes.
     * <p>
     * Parsing media may cause an HTTP request to be made to search for cover-art.
     * <p>
     * If the media has already been parsed when this function is called then <em>no</em> event will be
     * raised.
     * <p>
     * <strong>Invoking this method on a stream or DVB channel may cause a hang.</strong>
     */
    // FIXME maybe return boolean
    public void requestParseMedia() {
        libvlc_media_t media = media();
        if (media != null) {
            libvlc.libvlc_media_parse_async(media);
        }
        else {
            throw new IllegalStateException("No media"); // FIXME don't like throwing
        }
    }

    /**
     * Parse meta data from the current media, with options.
     * <p>
     * This method is asynchronous and a media player event will be raised when the parsed status
     * changes.
     * <p>
     * Parsing media may cause an HTTP request to be made to search for cover-art.
     * <p>
     * If the media has already been parsed when this function is called, or this function returns an
     * error, then <em>no</em> event will be raised.
     * <p>
     * If no options are specified, then the file is parsed if it is a local file.
     * <p>
     * This method, while always asynchronous, will cause the media parsing to wait indefinitely, in contrast with
     * {@link #requestParseMediaWithOptions(int, libvlc_media_parse_flag_t...)}
     *
     * @param options optional options
     * @return <code>true</code> if successful; <code>false</code> on error (or e.g. requires LibVLC 3.0.0)
     */
    public boolean requestParseMediaWithOptions(libvlc_media_parse_flag_t... options) {
        return requestParseMediaWithOptions(0, options);
    }

    /**
     * Parse meta data from the current media, with options and a timeout.
     * <p>
     * This method is asynchronous and a media player event will be raised when the parsed status
     * changes.
     * <p>
     * Parsing media may cause an HTTP request to be made to search for cover-art.
     * <p>
     * If the media has already been parsed when this function is called, or this function returns an
     * error, then <em>no</em> event will be raised.
     * <p>
     * If no options are specified, then the file is parsed if it is a local file.
     *
     * @param timeout -1 to use the default preparse timeout, 0 to wait indefinitely, otherwise number of milliseconds
     * @param options optional options
     * @return <code>true</code> if successful; <code>false</code> on error (or e.g. requires LibVLC 3.0.0)
     */
    public boolean requestParseMediaWithOptions(int timeout, libvlc_media_parse_flag_t... options) {
        int flags = 0;
        if (options != null) {
            for (libvlc_media_parse_flag_t option : options) {
                flags |= option.intValue();
            }
        }
        return libvlc.libvlc_media_parse_with_options(media(), flags, timeout) == 0;
    }

    /**
     * Test whether or not the current media has been parsed.
     *
     * @return <code>true</code> if the current media has been parsed, otherwise <code>false</code>
     */
    public boolean isMediaParsed() {
        libvlc_media_t media = media();
        if (media != null) {
            return 0 != libvlc.libvlc_media_is_parsed(media);
        }
        else {
            throw new IllegalStateException("No media"); // FIXME don't like throwing
        }
    }

    private libvlc_media_t media() {
        return mediaPlayer.media().media();
    }

}
