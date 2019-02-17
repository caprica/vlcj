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

package uk.co.caprica.vlcj.player.renderer;

/**
 * Description of a renderer discoverer.
 */
public final class RendererDiscovererDescription {

    /**
     * Discoverer short name.
     */
    private final String name;

    /**
     * Discoverer long name.
     */
    private final String longName;

    /**
     * Create a renderer discoverer description.
     *
     * @param name short name
     * @param longName  long name
     */
    public RendererDiscovererDescription(String name, String longName) {
        this.name = name;
        this.longName = longName;
    }

    /**
     * Get the discoverer short name.
     *
     * @return short name
     */
    public String name() {
        return name;
    }

    /**
     * Get the discoverer long name.
     *
     * @return long name
     */
    public String longName() {
        return longName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(60);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("name=").append(name).append(',');
        sb.append("longName=").append(longName).append(']');
        return sb.toString();
    }

}
