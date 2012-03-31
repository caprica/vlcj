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
 * Specification for a component that responds to play-list events.
 * 
 * TODO add an event for play-list finished?
 */
public interface PlaylistEventListener {
    
    /**
     * The current play-list item changed.
     * 
     * @param source play-list
     * @param itemIndex new item index
     * @param current new item
     */
    void playlistItemChanged(PlaylistComponent source, int itemIndex, PlaylistEntry current);

    /**
     * The end of the play-list was reached.
     * <p>
     * This event is <em>not</em> fired if the play-list repeat mode is set to {@link RepeatMode#REPEAT_LIST}.
     * 
     * @param playlistComponent play-list
     */
    void playlistFinished(PlaylistComponent playlistComponent);
}
