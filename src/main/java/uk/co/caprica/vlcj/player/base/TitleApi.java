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

import java.util.List;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_get_title;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_get_title_count;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_set_title;

/**
 * Behaviour pertaining to media titles (e.g. DVD and Bluray titles).
 */
public final class TitleApi extends BaseApi {

    TitleApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the number of titles.
     *
     * @return number of titles, or -1 if none
     */
    public int titleCount() {
        return libvlc_media_player_get_title_count(mediaPlayerInstance);
    }

    /**
     * Get the current title.
     *
     * @return title number
     */
    public int title() {
        return libvlc_media_player_get_title(mediaPlayerInstance);
    }

    /**
     * Set a new title to play.
     *
     * @param title title number
     */
    public void setTitle(int title) {
        libvlc_media_player_set_title(mediaPlayerInstance, title);
    }

    /**
     * Get the title descriptions.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return list of descriptions, may be empty but will never be <code>null</code>
     */
    public List<TitleDescription> titleDescriptions() {
        return Descriptions.titleDescriptions(mediaPlayerInstance);
    }

}
