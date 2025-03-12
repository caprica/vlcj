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

import uk.co.caprica.vlcj.binding.internal.libvlc_title_flags_e;

/**
 * Title description.
 */
public class TitleDescription {

    /**
     * Chapter duration (milliseconds).
     */
    private final long duration;

    /**
     * Title name.
     */
    private final String name;

    /**
     * Does the title represent a menu, interactive or plain content.
     */
    private final int flags;

    /**
     * Create a new title description.
     *
     * @param duration chapter duration (milliseconds)
     * @param name title name
     * @param flags title flags
     */
    public TitleDescription(long duration, String name, int flags) {
        this.duration = duration;
        this.name = name;
        this.flags = flags;
    }

    /**
     * Get the duration.
     *
     * @return duration (milliseconds)
     */
    public long duration() {
        return duration;
    }

    /**
     * Get the name.
     *
     * @return name
     */
    public String name() {
        return name;
    }

    /**
     * Get the title flags (menu, interactive, plain content).
     *
     * @return title flags
     */
    public int flags() {
        return flags;
    }

    /**
     * Is this title a menu?
     *
     * @return <code>true</code> if this title is a menu; <code>false</code> if it is not
     */
    public boolean menu() {
        return (flags & libvlc_title_flags_e.libvlc_title_menu) != 0;
    }

    /**
     * Is this title interactive?
     *
     * @return <code>true</code> if this title is interactive; <code>false</code> if it is not
     */
    public boolean interactive() {
        return (flags & libvlc_title_flags_e.libvlc_title_interactive) != 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(60);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("duration=").append(duration).append(',');
        sb.append("name=").append(name).append(',');
        sb.append("flags=").append(flags).append(',');
        sb.append("menu=").append(menu()).append(',');
        sb.append("interactive=").append(interactive()).append(']');
        return sb.toString();
    }
}
