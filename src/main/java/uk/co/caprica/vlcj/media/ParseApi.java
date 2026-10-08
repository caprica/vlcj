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

package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.parser.Parser;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_is_parsed;

/**
 * Behaviour pertaining to media parsing.
 * <p>
 * For media parsing, see {@link Parser}.
 */
public final class ParseApi extends BaseApi {

    ParseApi(Media media) {
        super(media);
    }

    /**
     * Is this media parsed.
     *
     * @return <code>true</code> if parsed; otherwise <code>false</code>
     */
    public boolean isParsed() {
        return libvlc_media_is_parsed(mediaInstance) == 1;
    }
}
