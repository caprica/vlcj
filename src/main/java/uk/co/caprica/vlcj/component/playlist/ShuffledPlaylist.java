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

import java.util.Arrays;
import java.util.Collections;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import uk.co.caprica.vlcj.logger.Logger;

/**
 * Simple implementation of a shuffled play-list.
 * <p>
 * This implementation works by mapping indices in the original play-list to
 * shuffled indices in the shuffled play-list. 
 */
class ShuffledPlaylist extends Playlist implements ListDataListener {

    /**
     * Original play-list.
     */
    private final Playlist delegate;
    
    /**
     * Mapped indices from the shuffled play-list to the original play-list.
     */
    private Integer[] indices;

    /**
     * Create a play-list.
     * 
     * @param delegate original play-list
     */
    ShuffledPlaylist(Playlist delegate) {
        this.delegate = delegate;
        this.delegate.addListDataListener(this);
        apply();
    }
    
    @Override
    public PlaylistEntry get(int index) {
        return delegate.get(translate(index));
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    /**
     * Get the original play-list.
     * 
     * @return play-list
     */
    public final Playlist getDelegate() {
        return delegate;
    }

    /**
     * Shuffle the list.
     */
    private void apply() {
        Logger.debug("apply()");
        reset();
        Collections.shuffle(Arrays.asList(indices));
    }

    /**
     * Reset the index. 
     */
    private void reset() {
        indices = new Integer[delegate.size()];
        for(int i = 0; i < delegate.size(); i++) {
            indices[i] = i;
        }
    }
    
    /**
     * Translate the item index to the shuffled index.
     * 
     * @param index index in the unshuffled play-list
     * @return corresponding index in the shuffled play-list
     */
    public int translate(int index) {
        return indices[index];
    }

    @Override
    public final void intervalAdded(ListDataEvent e) {
        // If the list changes, re-shuffle it
        apply();
    }

    @Override
    public final void intervalRemoved(ListDataEvent e) {
        // If the list changes, re-shuffle it
        apply();
    }

    @Override
    public final void contentsChanged(ListDataEvent e) {
        // If the list changes, re-shuffle it
        apply();
    }
}
