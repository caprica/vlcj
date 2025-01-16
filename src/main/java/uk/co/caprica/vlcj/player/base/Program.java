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

import static uk.co.caprica.vlcj.binding.support.strings.NativeString.copyNativeString;

/**
 * A program (e.g. from a digital video broadcast).
 */
public class Program {

    private final int groupId;

    private final String name;

    private final boolean selected;

    private final boolean scrambled;

    Program(libvlc_player_program_t instance) {
        this.groupId = instance.i_group_id;
        this.name = copyNativeString(instance.psz_name);
        this.selected = instance.b_selected != 0;
        this.scrambled = instance.b_scrambled != 0;
    }

    /**
     * Get the group identifier for this program.
     * <p>
     * This can be used to select a program via {@link ProgramApi#select(int)}.
     *
     * @return group identifier
     */
    public int groupId() {
        return groupId;
    }

    /**
     * Get the program name.
     *
     * @return name of the program
     */
    public String name() {
        return name;
    }

    /**
     * Is the program selected?
     *
     * @return <code>true</code> if selected; <code>false</code> otherwise
     */
    public boolean selected() {
        return selected;
    }

    /**
     * Is the program scrambled?
     *
     * @return <code>true</code> if scrambled; <code>false</code> otherwise
     */
    public boolean scrambled() {
        return scrambled;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("groupId=").append(groupId).append(',');
        sb.append("name=").append(name).append(',');
        sb.append("selected=").append(selected).append(',');
        sb.append("scrambled=").append(scrambled).append(']');
        return sb.toString();
    }
}
