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

package uk.co.caprica.vlcj.player.base;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_get_teletext;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_get_teletext_transparency;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_teletext;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_teletext_transparency;

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

    /**
     * Set whether the teletext background should be transparent.
     *
     * @param transparent <code>true</code> for transparent background, otherwise <code>false</code>
     */
    public void setTransparency(boolean transparent) {
        libvlc_video_set_teletext_transparency(mediaPlayerInstance, transparent ? 1 : 0);
    }

    /**
     * Get whether the teletext background is transparent.
     *
     * @return <code>true</code> if background transparent, otherwise <code>false</code>
     */
    public boolean getTransparency() {
        return libvlc_video_get_teletext_transparency(mediaPlayerInstance) == 1;
    }
}
