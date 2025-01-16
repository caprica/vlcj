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
 * AB loop status.
 */
public final class ABLoop {

    private final boolean validA;

    private final long aTime;

    private final double aPos;

    private final boolean validB;

    private final long bTime;

    private final double bPos;

    public ABLoop(boolean validA, Long aTime, Double aPos, boolean validB, Long bTime, Double bPos) {
        this.validA = validA;
        this.aTime = aTime != null ? (long) aTime : 0;
        this.aPos = aPos != null ? (double) aPos : 0;
        this.validB = validB;
        this.bTime = bTime != null ? (long) bTime : 0;
        this.bPos = bPos != null ? (double) bPos : 0;
    }

    public boolean validA() {
        return validA;
    }

    public long aTime() {
        return aTime;
    }

    public double aPos() {
        return aPos;
    }

    public boolean validB() {
        return validB;
    }

    public long bTime() {
        return bTime;
    }

    public double bPos() {
        return bPos;
    }
}
