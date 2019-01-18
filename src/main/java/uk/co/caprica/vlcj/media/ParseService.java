package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.enums.ParseFlag;
import uk.co.caprica.vlcj.enums.MediaParsedStatus;

/**
 *
 * <p>
 * Parsing is always asynchronous - you must wait for a parsed changed event before you can read meta data.
 * <p>
 * Once a media has been parsed, it can not be parsed again - so if for example parse fails due to a timeout and you
 * retry it, it will not parse again.
 * <p>
 * There are two special timeout values that can be used for parsing:
 * <ul>
 *     <li>0 means that the call will wait indefinitely;</li>
 *     <li>-1 means that the default value set by the "preparse-timeout" native option/argument will be used</li>
 * </ul>
 */
public class ParseService extends BaseService {

    ParseService(Media media) {
        super(media);
    }

    public boolean parse() {
        return parse(-1, (ParseFlag[]) null);
    }

    public boolean parse(int timeout) {
        return parse(timeout, (ParseFlag[]) null);
    }

    public boolean parse(ParseFlag... flags) {
        return parse(-1, flags);
    }

    public boolean parse(int timeout, ParseFlag... flags) {
        return libvlc.libvlc_media_parse_with_options(mediaInstance, flagsToInt(flags), timeout) == 0;
    }

    public void stop() {
        libvlc.libvlc_media_parse_stop(mediaInstance);
    }

    public MediaParsedStatus status() {
        return MediaParsedStatus.mediaParsedStatus(libvlc.libvlc_media_get_parsed_status(mediaInstance));
    }

    private int flagsToInt(ParseFlag... flags) {
        int result = 0;
        if (flags != null) {
            for (ParseFlag flag : flags) {
                result |= flag.intValue();
            }
        }
        return result;
    }

}
