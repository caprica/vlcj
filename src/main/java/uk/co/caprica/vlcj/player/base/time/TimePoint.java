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

package uk.co.caprica.vlcj.player.base.time;

/**
 * A time-point value from a media player timer event.
 */
public final class TimePoint {

    private final double rate;
    private final long length;
    private final long systemDate;
    private final long timestamp;
    private final double position;

    TimePoint(double rate, long length, long systemDate, long timestamp, double position) {
        this.rate = rate;
        this.length = length;
        this.systemDate = systemDate;
        this.timestamp = timestamp;
        this.position = position;
    }

    double rate() {
        return rate;
    }

    long length() {
        return length;
    }

    long systemDate() {
        return systemDate;
    }

    long timestamp() {
        return timestamp;
    }

    double position() {
        return position;
    }
}
