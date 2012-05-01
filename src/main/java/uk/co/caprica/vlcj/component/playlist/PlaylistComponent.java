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

import static uk.co.caprica.vlcj.component.playlist.PlaylistMode.NORMAL;
import static uk.co.caprica.vlcj.component.playlist.PlaylistMode.SHUFFLE;
import static uk.co.caprica.vlcj.component.playlist.RepeatMode.NO_REPEAT;
import static uk.co.caprica.vlcj.component.playlist.RepeatMode.REPEAT_CURRENT;

import java.util.ArrayList;
import java.util.List;

import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

/**
 * Implementation of a high-level component to manage a play-list for a media player.
 * <p>
 * No guarantees of thread-safety are made, it is the responsibility of the client
 * application to manage concurrent access to this component correctly.
 *  
 * TODO record errors on the play-list item so we don't churn if every item in the play-list is in error
 */
public class PlaylistComponent implements PlaylistEventListener {
    
    /**
     * Media player linked with the play-list.
     */
    private final MediaPlayer mediaPlayer;

    /**
     * List of event listeners. 
     */
    private final List<PlaylistEventListener> eventListenerList = new ArrayList<PlaylistEventListener>(); 
    
    /**
     * Play-list.
     */
    private Playlist playlist;
    
    /**
     * Index of the current item, or -1 if no current item.
     */
    private int current = -1;
    
    /**
     * Play-list mode.
     */
    private PlaylistMode playlistMode = NORMAL;
    
    /**
     * Repeat mode.
     */
    private RepeatMode repeatMode = NO_REPEAT;
    
    /**
     * Create a play-list component.
     * 
     * @param mediaPlayer media player to associate with the play-list
     */
    public PlaylistComponent(MediaPlayer mediaPlayer) {
        this(mediaPlayer, new Playlist());
    }
    
    /**
     * Create a play-list component.
     * 
     * @param mediaPlayer media player to associate with the play-list
     * @param playlist initial play-list
     */
    public PlaylistComponent(MediaPlayer mediaPlayer, Playlist playlist) {
        this.mediaPlayer = mediaPlayer;
        this.playlist = playlist;
        mediaPlayer.addMediaPlayerEventListener(new PlaylistMediaPlayerEventHandler());
        addPlaylistEventListener(this);
    }
    
    /**
     * Add a component to be notified of play-list events.
     * 
     * @param listener component to add
     */
    public final void addPlaylistEventListener(PlaylistEventListener listener) {
        Logger.debug("addPlaylistEventListener(listener={})", listener);
        eventListenerList.add(listener);
    }
    
    /**
     * Remove a component that was previously interested in notifications of play-list events.
     * 
     * @param listener component to remove
     */
    public final void removePlaylistEventListener(PlaylistEventListener listener) {
        Logger.debug("removePlaylistEventListener(listener={})", listener);
        eventListenerList.remove(listener);
    }
    
    /**
     * Set the play-list play-back mode.
     * 
     * @param playlistMode play-list mode
     */
    public final void setMode(PlaylistMode playlistMode) {
        this.playlistMode = playlistMode;
        // Shuffle if needed
        shuffle();
    }
    
    /**
     * Get the play-list play-back mode.
     * 
     * @return play-list mode
     */
    public final PlaylistMode getPlaylistMode() {
        return playlistMode;
    }
    
    /**
     * Set the play-list repeat mode. 
     * 
     * @param repeatMode repeat mode
     */
    public final void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }
    
    /**
     * Get the play-list repeat mode.
     * 
     * @return repeat mode
     */
    public final RepeatMode getRepeatMode() {
        return repeatMode;
    }
    
    /**
     * Get the current play-list.
     * 
     * @return play-list
     */
    public final Playlist getPlaylist() {
        if(playlistMode != SHUFFLE) {
            return playlist;
        }
        else {
            return ((ShuffledPlaylist) playlist).getDelegate();
        }
    }
    
    /**
     * Set a new play-list.
     * <p>
     * The current item will keep playing (even if it is removed from the play-list)
     * until {@link #playNext()} is called.
     * 
     * @param playlist new play-list
     */
    public final void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        // Re-shuffle if needed
        shuffle();
    }
    
    /**
     * Play the next item.
     * <p>
     * When this method is called, the {@link #onGetNext(int)} and {@link #onGetMediaOptions(int)}
     * template methods will be invoked.
     */
    public final void playNext() {
        Logger.debug("playNext()");
        if(!playlist.isEmpty()) {
            int previous = current;
            current = onGetNext(current);
            Logger.debug("current={}", current);
            if(current != -1) {
                playMedia(current, onGetMediaOptions(current));
            }
            else if(previous != -1) {
                stop();
                notifyPlaylistFinished();
            }
        }
    }

    /**
     * Play a specific item.
     * <p>
     * Using this method will <em>not</em> cause the {@link #onGetMediaOptions(int)}
     * template method to be invoked.
     * 
     * @param itemIndex index of the item to play
     * @param mediaOptions zero or media options to set when playing the item
     */
    public final void playItem(final int itemIndex, final String... mediaOptions) {
        Logger.debug("playItem(itemIndex={},mediaOptions={})", itemIndex, mediaOptions);
        playMedia(itemIndex, mediaOptions);
    }
    
    /**
     * Get the current item.
     * 
     * @return current item, or <code>null</code> if there is no current item
     */
    public final PlaylistEntry current() {
        Logger.debug("current()");
        return current != -1 ? playlist.get(current) : null;
    }

    /**
     * Get the index of the current item.
     * 
     * @return current item, or <code>-1</code> if there is no current item
     */
    public final int currentIndex() {
        Logger.debug("currentIndex()");
        if(playlistMode != SHUFFLE) { 
            return current;
        }
        else {
            return ((ShuffledPlaylist)playlist).translate(current);
        }
    }

    /**
     * Stop play-back.
     * <p>
     * The currently playing item, if any, will be stopped.
     * <p>
     * The play-list will be stopped.
     */
    public final void stop() {
       Logger.debug("stop()");
       getMediaPlayer().stop();
    }
    
    /**
     * Get the media player associated with the play-list.
     * 
     * @return media player
     */
    public final MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    
    /**
     * Re-shuffle the play-list if the {@link #playlistMode} is {@link PlaylistMode#SHUFFLE}.
     */
    public final void shuffle() {
        if(playlistMode == SHUFFLE && playlist != null) {
            this.playlist = new ShuffledPlaylist(playlist);
        }
    }
    
    /**
     * Template method to get the index of the next item to play.
     * <p>
     * A sub-class may override this method to implement a different strategy for
     * stepping through the play-list (unlikely).
     * 
     * @param current current index
     * @return index of the next item to play
     */
    protected int onGetNext(int current) {
        Logger.debug("onGetNext(current={})", current);
        int result;
        Logger.debug("repeatMode={}", repeatMode);
        // If the current item should not be repeated, or if there is no current item...
        if(repeatMode != REPEAT_CURRENT || current == -1) {
            // ...set the next item to play depending on the play-list mode
            Logger.debug("playlistMode={}", playlistMode);
            switch(playlistMode) {
                case NORMAL:
                case SHUFFLE:
                    // If there is no current item (e.g. first play)...
                    if(current == -1) {
                        // ...simply set the first item
                        result = 0;
                    }
                    else {
                        // If the play-list should not repeat...
                        if(repeatMode == NO_REPEAT) {
                            // ...advance to the next item until the end of the list is reached
                            result = current+1 < playlist.size() ? current+1 : -1;
                        }
                        // Otherwise the play-list mode must be repeat-list...
                        else {
                            // ...so reset back to the start of the list
                            result = current+1 < playlist.size() ? current+1 : 0;
                        }
                    }
                    break;
                    
                case RANDOM:
                    // For random mode, the play-list never ends so always pick a new random item
                    result = (int)Math.round(Math.random() * playlist.size()); 
                    break;
                    
                default:
                    throw new IllegalStateException("Unexpected play-list mode " + playlistMode);
            }            
        }
        // If the current item should be repeated...
        else {
            // ...simply return the current item
            result = current;
        }
        Logger.debug("result={}", result);
        return result;
    }

    /**
     * Template method to get the media options to set when playing a particular
     * item from the play-list.
     * 
     * @param itemIndex item index
     * @return media options, or <code>null</code>
     */
    protected String[] onGetMediaOptions(int itemIndex) {
        // The default implementation does not set any media options
        return null;
    }

    /**
     * Play an item in the play-list.
     *
     * The play-list lock <em>must</em> be held on entering this method.
     * 
     * @param index index of the item to play
     * @param mediaOptions zero or more media options to set when playing the item
     */
    private void playMedia(int index, String... mediaOptions) {
        Logger.debug("playItem(index={},mediaOptions={})", index, mediaOptions);
        if(index >= 0 && index < playlist.size()) { 
            mediaPlayer.playMedia(playlist.get(index).getMrl(), mediaOptions);
        }
    }
    
    /**
     * Media player event handler implementation.
     * <p>
     * Note that vlcj serializes the native events - only one event handler is firing
     * at any one time.
     * <p>
     * This means that an event handler can query the event source for the current item
     * in a thread-safe manner and it is guaranteed to be correct.
     * <p>
     * Event handlers <em>must</em> run quickly.
     * <p>
     * Events are <em>not</em> delivered on the Event Dispatch Thread. 
     */
    private class PlaylistMediaPlayerEventHandler extends MediaPlayerEventAdapter {
        @Override
        public void mediaChanged(MediaPlayer mediaPlayer) {
            Logger.debug("mediaChanged()");
            notifyPlaylistItemChanged();
        }

        @Override
        public void playing(MediaPlayer mediaPlayer) {
            // Clear any error on the current item
            current().error();
        }

        @Override
        public void finished(MediaPlayer mediaPlayer) {
            Logger.debug("finished()");
            playNext();
        }

        @Override
        public void error(MediaPlayer mediaPlayer) {
            Logger.debug("error()");
            // Make the current play-list item as error
            current().error();
            // Play the next item
            playNext();
        }
    }
    
    /**
     * Notify all registered event listeners that the current item changed.
     */
    private void notifyPlaylistItemChanged() {
        PlaylistEntry current = current();
        int currentIndex = currentIndex();
        for(int i = eventListenerList.size()-1; i >= 0; i--) {
            eventListenerList.get(i).playlistItemChanged(PlaylistComponent.this, currentIndex, current);
        }
    }

    /**
     * 
     */
    private void notifyPlaylistFinished() {
        for(int i = eventListenerList.size()-1; i >= 0; i--) {
            eventListenerList.get(i).playlistFinished(PlaylistComponent.this);
        }
    }
    
    @Override
    public void playlistItemChanged(PlaylistComponent source, int itemIndex, PlaylistEntry item) {
    }

    @Override
    public void playlistFinished(PlaylistComponent playlistComponent) {
    }
}
