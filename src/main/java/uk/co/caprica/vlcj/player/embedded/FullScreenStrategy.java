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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded;

/**
 * Specification for a full-screen strategy implementation.
 */
public interface FullScreenStrategy {

    /**
     * Attempt to enter full-screen mode.
     */
    void enterFullScreenMode();

    /**
     * Exit full-screen mode.
     */
    void exitFullScreenMode();

    /**
     * Test whether or not full-screen mode is currently active.
     *
     * @return <code>true</code> if full-screen mode is active; otherwise <code>false</code>
     */
    boolean isFullScreenMode();
}
