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
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

/**
 * Enumeration of sub-item handling modes.
 * <p>
 * By default, the media player will automatically play the first sub-item with {@link #PLAY_FIRST_SUB_ITEM}.
 */
public enum SubItemMode {

    /**
     * Do nothing with sub-items.
     */
    NO_ACTION,

    /**
     * Automatically play the first sub-item.
     */
    PLAY_FIRST_SUB_ITEM
}
