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

package uk.co.caprica.vlcj.player.base;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_teletext;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_teletext;

/**
 * Behaviour pertaining to teletext.
 */
public final class TeletextApi extends BaseApi {

    TeletextApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the current teletext page.
     *
     * @return page number
     */
    public int page() {
        return libvlc_video_get_teletext(mediaPlayerInstance);
    }

    /**
     * Set the new teletext page to retrieve.
     *
     * @param pageNumber page number
     */
    public void setPage(int pageNumber) {
        libvlc_video_set_teletext(mediaPlayerInstance, pageNumber);
    }

    /**
     * Set ("press") a teletext key.
     *
     * @param key teletext key
     */
    public void setKey(TeletextKey key) {
        libvlc_video_set_teletext(mediaPlayerInstance, key.intValue());
    }

}
