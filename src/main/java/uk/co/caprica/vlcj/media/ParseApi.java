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

package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_get_parsed_status;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_parse_stop;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_parse_with_options;

/**
 * Behaviour pertaining to parsing of media.
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
 * Client applications should add a {@link MediaEventListener} to be notified of changes to the parsed status - an event
 * will fire if the parsing succeeded, timed-out, or failed for any other reason as per {@link MediaParsedStatus}.
 * <p>
 * Parsing may trigger the remote downloading of data, including for example performing a search for cover art for the
 * media. This has clear privacy implications.
 * <p>
 * To affirmatively prevent all network access for meta data, consider trying the "--no-metadata-network-access"
 * argument when creating a {@link MediaPlayerFactory}.
 * <p>
 * It should also be possible to prevent such network accesses by using appropriate {@link ParseFlag} values, see
 * {@link ParseFlag#FETCH_LOCAL} vs {@link ParseFlag#FETCH_NETWORK}.
 * <p>
 * Even with network access disabled, some media covert art may still appear locally (e.g. ~/.cache/vlc) - this does not
 * necessarily mean that a remote network request was made for the cover art, rather the art was embedded in the media
 * file and extracted to this temporary cache directory.
 */
public final class ParseApi extends BaseApi {

    ParseApi(Media media) {
        super(media);
    }

    /**
     * Parse the media, asynchronously, with a timeout set by the "preparse-timeout" native option value.
     *
     * @return <code>true</code> if the parse request was successful; <code>false</code> on error
     */
    public boolean parse() {
        return parse(-1, (ParseFlag[]) null);
    }

    /**
     * Parse the media, asynchronously, with a specific timeout.
     *
     * @param timeout timeout, milliseconds
     * @return <code>true</code> if the parse request was successful; <code>false</code> on error
     */
    public boolean parse(int timeout) {
        return parse(timeout, (ParseFlag[]) null);
    }

    /**
     * Parse the media, asynchronously, with specific parse flags, and a timeout set by the "preparse-timeout" native
     * option value.
     *
     * @param flags parse flags
     * @return <code>true</code> if the parse request was successful; <code>false</code> on error
     */
    public boolean parse(ParseFlag... flags) {
        return parse(-1, flags);
    }

    /**
     * Parse the media, asynchronously, with a specific timeout and specific parse flags.
     *
     * @param timeout timeout, milliseconds
     * @param flags parse flags
     * @return <code>true</code> if the parse request was successful; <code>false</code> on error
     */
    public boolean parse(int timeout, ParseFlag... flags) {
        return libvlc_media_parse_with_options(mediaInstance, flagsToInt(flags), timeout) == 0;
    }

    /**
     * Stop the asynchronous parsing.
     */
    public void stop() {
        libvlc_media_parse_stop(mediaInstance);
    }

    /**
     * Get the media parsed status.
     * <p>
     * It is recommended to use media events to determine the parse status instead of this.
     *
     * @return parsed status
     */
    public MediaParsedStatus status() {
        return MediaParsedStatus.mediaParsedStatus(libvlc_media_get_parsed_status(mediaInstance));
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
