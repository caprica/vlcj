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

package uk.co.caprica.vlcj.component.playlist.swing;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import uk.co.caprica.vlcj.component.playlist.Playlist;

/**
 * Implementation of a play-list as a {@link ListModel}.
 */
public class PlaylistListModel extends AbstractListModel implements ListDataListener {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Play-list.
     */
    private final Playlist playlist;
    
    /**
     * Create a list model.
     * 
     * @param playlist play-list
     */
    public PlaylistListModel(Playlist playlist) {
        this.playlist = playlist;
        playlist.addListDataListener(this);
    }

    /**
     * Get the underlying play-list.
     * 
     * @return play-list
     */
    public final Playlist getPlaylist() {
        return playlist;
    }
    
    @Override
    public int getSize() {
        return playlist.size();
    }

    @Override
    public Object getElementAt(int index) {
        return playlist.get(index);
    }

    @Override
    public final void intervalAdded(ListDataEvent e) {
        // Propagate the event
        fireIntervalAdded(e.getSource(), e.getIndex0(), e.getIndex1());
    }

    @Override
    public final void intervalRemoved(ListDataEvent e) {
        // Propagate the event
        fireIntervalRemoved(e.getSource(), e.getIndex0(), e.getIndex1());
    }

    @Override
    public final void contentsChanged(ListDataEvent e) {
        // Propagate the event
        fireContentsChanged(e.getSource(), e.getIndex0(), e.getIndex1());
    }
}
