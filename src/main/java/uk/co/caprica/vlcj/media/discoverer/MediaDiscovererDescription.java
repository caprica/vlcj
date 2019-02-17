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

package uk.co.caprica.vlcj.media.discoverer;

/**
 * Media discoverer description.
 */
public final class MediaDiscovererDescription {

    /**
     * Short name of the media discoverer.
     */
    private final String name;

    /**
     * Long name of the media discoverer.
     */
    private final String longName;

    /**
     * Category of the media discoverer.
     */
    private final MediaDiscovererCategory category;

    public MediaDiscovererDescription(String name, String longName, MediaDiscovererCategory category) {
        this.name = name;
        this.longName = longName;
        this.category = category;
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

    /**
     * Get the discoverer category.
     *
     * @return category
     */
    public MediaDiscovererCategory category() {
        return category;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(60);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("name=").append(name).append(',');
        sb.append("longName=").append(longName).append(',');
        sb.append("category=").append(category).append(']');
        return sb.toString();
    }

}
