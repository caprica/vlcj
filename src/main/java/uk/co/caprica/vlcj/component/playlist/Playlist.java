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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import uk.co.caprica.vlcj.logger.Logger;

/**
 * Implementation of a play-list.
 */
public class Playlist implements Iterable<PlaylistEntry> {

    /**
     * Initial size for the play-list.
     */
    private static final int DEFAULT_PLAYLIST_SIZE = 100;

    /**
     * Collection of registered list data event listeners.
     */
    private final List<ListDataListener> listeners = new ArrayList<ListDataListener>();

    /**
     * Play-list entries.
     */
    private final List<PlaylistEntry> entries = new ArrayList<PlaylistEntry>(DEFAULT_PLAYLIST_SIZE);
    
    /**
     * Add a component to be notified of changes to the play-list data.
     * 
     * @param listener component to add
     */
    public final void addListDataListener(ListDataListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove a component previously registered to be notified of changes to the play-list data.
     * 
     * @param listener component to remove
     */
    public final void removeListDataListener(ListDataListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Append one or more items to the play-list.
     * 
     * @param media items to add
     */
    public final void append(final PlaylistEntry... media) {
        Logger.debug("append(media={})", (Object[])media);
        // Convert the parameter and delegate...
        append(Arrays.asList(media));
    }

    /**
     * Append one or more items to the play-list.
     * 
     * @param media items to add
     */
    public final void append(final List<PlaylistEntry> media) {
        Logger.debug("append(media={})", media);
        int fromIndex = entries.size();
        entries.addAll(media);
        int toIndex = entries.size() - 1;
        fireItemsAdded(fromIndex, toIndex);
    }

    /**
     * Insert one or more items into the play-list.
     * 
     * @param atIndex index at which to insert the items
     * @param media items to add
     */
    public final void insert(final int atIndex, final PlaylistEntry... media) {
        Logger.debug("insert(atIndex={},media={})", atIndex, (Object[])media);
        // Convert the parameter and delegate...
        insert(atIndex, Arrays.asList(media));
    }

    /**
     * Insert one or more items into the play-list.
     * 
     * @param atIndex index at which to insert the items
     * @param media items to add
     */
    public final void insert(final int atIndex, final List<PlaylistEntry> media) {
        Logger.debug("insert(atIndex={},media={})", atIndex, media);
        entries.addAll(atIndex, media);
    }

    /**
     * Remove a play-list item.
     * 
     * @param index index of the item to remove
     */
    public final void remove(int index) {
        Logger.debug("remove(index={})", index);
        entries.remove(index);
        fireItemsRemoved(index, index);
    }
    
    /**
     * Remove one or more items from the play-list.
     * 
     * @param playlistEntry items to remove
     */
    public final void remove(PlaylistEntry... playlistEntry) {
        Logger.debug("remove(playlistEntry={})", (Object[])playlistEntry);
        for(PlaylistEntry entry : playlistEntry) {
            int index = entries.indexOf(entry);
            if(index != -1) {
                entries.remove(index);
                fireItemsRemoved(index, index);
            }
        }
    }
    
    /**
     * Remove a range of items from the play-list.
     * 
     * @param fromIndex starting index (inclusive)
     * @param toIndex ending index (inclusive)
     */
    public final void remove(int fromIndex, int toIndex) {
        Logger.debug("remove(fromIndex={},toIndex={})", fromIndex, toIndex);
        // Must remove in reverse order...
        for(int i = toIndex; i >= fromIndex; i-- ) {
            entries.remove(i);
        }
        fireItemsRemoved(fromIndex, toIndex);
    }
    
    /**
     * Clear the play-list.
     */
    public final void clear() {
        Logger.debug("clear()");
        int fromIndex = 0;
        int toIndex = entries.size() - 1;
        entries.clear();
        fireItemsRemoved(fromIndex, toIndex);
    }

    /**
     * Get the play-list size.
     * 
     * @return size
     */
    public int size() {
        return entries.size();
    }
    
    /**
     * Is the play-list empty?
     * 
     * @return <code>true</code> if the play-list is empty; otherwise <code>false</code> 
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }
    
    /**
     * Get the item at the specified index.
     * 
     * @param index required index
     * @return corresponding item
     */
    public PlaylistEntry get(int index) {
        return entries.get(index);
    }

    @Override
    public final Iterator<PlaylistEntry> iterator() {
        return entries.iterator();
    }

    /**
     * Notify listeners that one or more items were added to the play-list.
     * 
     * @param fromIndex index of the first item that was added (inclusive)
     * @param toIndex index of the last item that was added (inclusive)
     */
    private void fireItemsAdded(int fromIndex, int toIndex) {
        if(!listeners.isEmpty()) {
            ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, fromIndex, toIndex);
            for(int i = listeners.size() - 1; i >= 0; i--) {
                listeners.get(i).intervalAdded(event);
            }
        }
    }

    /**
     * Notify listeners that one or more items were removed from the play-list.
     * 
     * @param fromIndex index of the first item that was removed (inclusive)
     * @param toIndex index of the last item that was removed (inclusive)
     */
    private void fireItemsRemoved(int fromIndex, int toIndex) {
        if(!listeners.isEmpty()) {
            ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, fromIndex, toIndex);
            for(int i = listeners.size() - 1; i >= 0; i--) {
                listeners.get(i).intervalAdded(event);
            }
        }
    }
}
