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

/**
 * Title description.
 */
public class TitleDescription {

    /**
     * Chapter duration (milliseconds).
     */
    private final long duration;

    /**
     * Chapter name.
     */
    private final String name;

    /**
     * Does the title represent a menu.
     */
    private final boolean menu;

    /**
     * Create a new title description.
     *
     * @param duration chapter duration (milliseconds)
     * @param name chapter name
     * @param menu <code>true</code> if the title represents a menu; <code>false</code> otherwise
     */
    public TitleDescription(long duration, String name, boolean menu) {
        this.duration = duration;
        this.name = name;
        this.menu = menu;
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
     * Does the title represent a menu.
     *
     * @return <code>true</code> if the title represents a menu; <code>false</code> otherwise
     */
    public boolean isMenu() {
        return menu;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(60);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("duration=").append(duration).append(',');
        sb.append("name=").append(name).append(',');
        sb.append("menu=").append(menu).append(']');
        return sb.toString();
    }
}
