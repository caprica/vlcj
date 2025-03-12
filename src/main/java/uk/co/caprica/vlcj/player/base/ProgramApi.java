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

import uk.co.caprica.vlcj.binding.internal.libvlc_player_program_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_player_programlist_t;
import uk.co.caprica.vlcj.binding.support.types.size_t;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_get_program_from_id;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_get_programlist;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_get_selected_program;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_select_program_id;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_player_program_delete;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_player_programlist_at;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_player_programlist_count;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_player_programlist_delete;

/**
 * Behaviour pertaining to media player programs.
 */
public final class ProgramApi extends BaseApi {

    ProgramApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * This returns a snapshot of the program list.
     * <p>
     * If any program changed events are received, this list should be released via {@link #release()} and a further
     * call to this method made to get the updated list.
     *
     * @return program list
     */
    public List<Program> list() {
        libvlc_player_programlist_t programList = libvlc_media_player_get_programlist(mediaPlayerInstance);
        if (programList != null) {
            int count = libvlc_player_programlist_count(programList).intValue();
            List<Program> result = new ArrayList<Program>(count);
            for (int i = 0; i < count; i++) {
                libvlc_player_program_t programInstance = libvlc_player_programlist_at(programList, new size_t(i));
                // This native instance must NOT be freed here
                result.add(new Program(programInstance));
            }
            libvlc_player_programlist_delete(programList);
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    public void select(int programId) {
        libvlc_media_player_select_program_id(mediaPlayerInstance, programId);
    }

    public Program selected() {
        libvlc_player_program_t programInstance = libvlc_media_player_get_selected_program(mediaPlayerInstance);
        return convertAndFree(programInstance);
    }

    public Program get(int programId) {
        libvlc_player_program_t programInstance = libvlc_media_player_get_program_from_id(mediaPlayerInstance, programId);
        return convertAndFree(programInstance);
    }

    private static Program convertAndFree(libvlc_player_program_t programInstance) {
        if (programInstance != null) {
            Program program = new Program(programInstance);
            libvlc_player_program_delete(programInstance);
            return program;
        } else {
            return null;
        }
    }
}
