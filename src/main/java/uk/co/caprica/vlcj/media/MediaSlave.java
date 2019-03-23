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

package uk.co.caprica.vlcj.media;

/**
 * Media slave.
 */
public final class MediaSlave {

    private final String uri;

    private final MediaSlaveType type;

    private final int priority;

    /**
     * Create a media slave.
     *
     * @param uri URI
     * @param type type
     * @param priority priority
     */
    public MediaSlave(String uri, MediaSlaveType type, int priority) {
        this.uri = uri;
        this.type = type;
        this.priority = priority;
    }

    /**
     * Get the media slave URI.
     *
     * @return URI
     */
    public String uri() {
        return uri;
    }

    /**
     * Get the media slave type.
     *
     * @return type
     */
    public MediaSlaveType type() {
        return type;
    }

    /**
     * Get the media slave priority.
     *
     * @return priority
     */
    public int priority() {
        return priority;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("uri=").append(uri).append(',');
        sb.append("type=").append(type).append(',');
        sb.append("priority=").append(priority).append(']');
        return sb.toString();
    }

}
