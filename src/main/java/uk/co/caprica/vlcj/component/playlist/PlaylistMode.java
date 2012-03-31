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
 * Copyright 2009, 2010, 2011, 2012 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.component.playlist;

/**
 * Enumeration of play-list modes.
 */
public enum PlaylistMode {

    /**
     * The play-list is played in the order in which items were added.
     * <p>
     * The play-list will end after the last item has been played.
     */
    NORMAL,
    
    /**
     * The play-list is shuffled once, each track is played in turn.
     * <p>
     * The play-list will end after each item has been played once.
     */
    SHUFFLE,
    
    /**
     * A random item is picked each time (even if it has already been played).
     * <p>
     * The play-list will never end with this mode.
     */
    RANDOM
}
