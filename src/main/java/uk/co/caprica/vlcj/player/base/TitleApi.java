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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.vlcj.binding.internal.libvlc_title_description_t;
import uk.co.caprica.vlcj.binding.support.strings.NativeString;

import java.util.ArrayList;
import java.util.List;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_get_full_title_descriptions;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_get_title;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_get_title_count;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_set_title;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_title_descriptions_release;

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
        List<TitleDescription> result;
        PointerByReference titles = new PointerByReference();
        int titleCount = libvlc_media_player_get_full_title_descriptions(mediaPlayerInstance, titles);
        if (titleCount != -1) {
            result = new ArrayList<TitleDescription>(titleCount);
            Pointer[] pointers = titles.getValue().getPointerArray(0, titleCount);
            for (Pointer pointer : pointers) {
                libvlc_title_description_t titleDescription = Structure.newInstance(libvlc_title_description_t.class, pointer);
                titleDescription.read();
                result.add(new TitleDescription(titleDescription.i_duration, NativeString.copyNativeString(titleDescription.psz_name), titleDescription.i_flags));
            }
            libvlc_title_descriptions_release(titles.getValue(), titleCount);
        } else {
            result = new ArrayList<TitleDescription>(0);
        }
        return result;
    }
}
