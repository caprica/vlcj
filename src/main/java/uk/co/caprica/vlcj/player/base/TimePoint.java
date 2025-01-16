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

/**
 * A time-point value from a media player timer event.
 */
public final class TimePoint {

    private final double rate;
    private final long length;
    private final long systemDate;
    private long timestamp;
    private double position;

    public TimePoint(double rate, long length, long systemDate, long timestamp, double position) {
        this.rate = rate;
        this.length = length;
        this.systemDate = systemDate;
        this.timestamp = timestamp;
        this.position = position;
    }

    public double rate() {
        return rate;
    }

    public long length() {
        return length;
    }

    public long systemDate() {
        return systemDate;
    }

    public long timestamp() {
        return timestamp;
    }

    public double position() {
        return position;
    }

    void update(long timestamp, double position) {
        this.timestamp = timestamp;
        this.position = position;
    }
}
