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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.parser;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_parse_flag_t;
import uk.co.caprica.vlcj.factory.Dialogs;
import uk.co.caprica.vlcj.media.Media;

import java.util.EnumSet;

/**
 * Build for media parse flags.
 * <p>
 * See {@link Parser#newParseRequest(Media, MediaParseFlags)}.
 */
public class MediaParseFlags {

    private final EnumSet<libvlc_media_parse_flag_t> parseFlags;

    public static MediaParseFlags mediaParseFlags() {
        return new MediaParseFlags();
    }

    private MediaParseFlags() {
        parseFlags = EnumSet.noneOf(libvlc_media_parse_flag_t.class);
    }

    /**
     * Enable parsing.
     *
     * @return this
     */
    public MediaParseFlags withParse() {
        parseFlags.add(libvlc_media_parse_flag_t.libvlc_media_parse);
        return this;
    }

    /**
     * Fetch meta and cover art using local resources.
     *
     * @return this
     */
    public MediaParseFlags withFetchLocal() {
        parseFlags.add(libvlc_media_parse_flag_t.libvlc_media_fetch_local);
        return this;
    }

    /**
     * Fetch meta and cover art using network resources.
     *
     * @return this
     */
    public MediaParseFlags withFetchNetwork() {
        parseFlags.add(libvlc_media_parse_flag_t.libvlc_media_fetch_network);
        return this;
    }

    /**
     * Enable user interaction via dialogs, e.g. if the input being parsed asks for credentials.
     * <p>
     * See {@link Dialogs}.
     *
     * @return this
     */
    public MediaParseFlags withDoInteract() {
        parseFlags.add(libvlc_media_parse_flag_t.libvlc_media_do_interact);
        return this;
    }

    /**
     * Get the native media parse flags value.
     *
     * @return bitmask of native media parse flags
     */
    public int get() {
        return parseFlags.stream()
            .map(libvlc_media_parse_flag_t::intValue)
            .reduce(0, (acc, value) -> acc | value);
    }
}
