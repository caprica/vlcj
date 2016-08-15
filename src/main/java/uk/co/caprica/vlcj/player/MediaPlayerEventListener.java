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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_parse_flag_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

/**
 * Specification for a component that is interested in receiving event notifications from the media
 * player.
 * <p>
 * In most cases any event that fires for a media item will also fire for any sub-item, so for
 * example mediaChanged() will fire for the "main" media item and each subsequent sub-item.
 * <p>
 * Events are likely <em>not</em> raised on the Swing Event Dispatch thread so if updating user
 * interface components in response to these events care must be taken to use
 * {@link SwingUtilities#invokeLater(Runnable)}.
 *
 * @see MediaPlayerEventAdapter
 */
public interface MediaPlayerEventListener {

    // === Events relating to the media player ==================================

    /**
     * The media changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param media new media instance
     * @param mrl media resource locator
     */
    void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl);

    /**
     * Opening the media.
     *
     * @param mediaPlayer media player that raised the event
     */
    void opening(MediaPlayer mediaPlayer);

    /**
     * Buffering media.
     *
     * @param mediaPlayer media player that raised the event
     * @param newCache percentage complete, ranging from 0.0 to 100.0
     */
    void buffering(MediaPlayer mediaPlayer, float newCache);

    /**
     * The media started playing.
     * <p>
     * There is no guarantee that a video output has been created at this point.
     *
     * @param mediaPlayer media player that raised the event
     */
    void playing(MediaPlayer mediaPlayer);

    /**
     * Media paused.
     *
     * @param mediaPlayer media player that raised the event
     */
    void paused(MediaPlayer mediaPlayer);

    /**
     * Media stopped.
     *
     * @param mediaPlayer media player that raised the event
     */
    void stopped(MediaPlayer mediaPlayer);

    /**
     * Media skipped forward.
     *
     * @param mediaPlayer media player that raised the event
     */
    void forward(MediaPlayer mediaPlayer);

    /**
     * Media skipped backward.
     *
     * @param mediaPlayer media player that raised the event
     */
    void backward(MediaPlayer mediaPlayer);

    /**
     * Media finished playing (i.e. the end was reached without being stopped).
     *
     * @param mediaPlayer media player that raised the event
     */
    void finished(MediaPlayer mediaPlayer);

    /**
     * Media play-back time changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newTime new time
     */
    void timeChanged(MediaPlayer mediaPlayer, long newTime);

    /**
     * Media play-back position changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newPosition percentage between 0.0 and 1.0
     */
    void positionChanged(MediaPlayer mediaPlayer, float newPosition);

    /**
     * Media seekable status changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newSeekable new seekable status
     */
    void seekableChanged(MediaPlayer mediaPlayer, int newSeekable);

    /**
     * Media pausable status changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newPausable new pausable status
     */
    void pausableChanged(MediaPlayer mediaPlayer, int newPausable);

    /**
     * Media title changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newTitle new title
     */
    void titleChanged(MediaPlayer mediaPlayer, int newTitle);

    /**
     * A snapshot was taken.
     *
     * @param mediaPlayer media player that raised the event
     * @param filename name of the file containing the snapshot
     */
    void snapshotTaken(MediaPlayer mediaPlayer, String filename);

    /**
     * Media length changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newLength new length (number of milliseconds)
     */
    void lengthChanged(MediaPlayer mediaPlayer, long newLength);

    /**
     * The number of video outputs changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newCount new number of video outputs
     */
    void videoOutput(MediaPlayer mediaPlayer, int newCount);

    /**
     * Program scrambled changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newScrambled new scrambled value
     */
    void scrambledChanged(MediaPlayer mediaPlayer, int newScrambled);

    /**
     * An elementary stream was added.
     *
     * @param mediaPlayer media player that raised the event
     * @param type type of stream
     * @param id identifier of stream
     */
    void elementaryStreamAdded(MediaPlayer mediaPlayer, int type, int id);

    /**
     * An elementary stream was deleted.
     *
     * @param mediaPlayer media player that raised the event
     * @param type type of stream
     * @param id identifier of stream
     */
    void elementaryStreamDeleted(MediaPlayer mediaPlayer, int type, int id);

    /**
     * An elementary stream was selected.
     *
     * @param mediaPlayer media player that raised the event
     * @param type type of stream
     * @param id identifier of stream
     */
    void elementaryStreamSelected(MediaPlayer mediaPlayer, int type, int id);

    /**
     * The media player was corked/un-corked.
     * <p>
     * Corking/un-corking can occur e.g. when another media player (or some
     * other application) starts/stops playing media.
     *
     * @param mediaPlayer media player that raised the event
     * @param corked <code>true</code> if corked; otherwise <code>false</code>
     */
    void corked(MediaPlayer mediaPlayer, boolean corked);

    /**
     * The audio was muted/un-muted.
     *
     * @param mediaPlayer media player that raised the event
     * @param muted <code>true</code> if muted; otherwise <code>false</code>
     */
    void muted(MediaPlayer mediaPlayer, boolean muted);

    /**
     * The volume changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param volume new volume
     */
    void volumeChanged(MediaPlayer mediaPlayer, float volume);

    /**
     * The audio device changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param audioDevice new audio device
     */
    void audioDeviceChanged(MediaPlayer mediaPlayer, String audioDevice);

    /**
     * The chapter changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newChapter new chapter
     */
    void chapterChanged(MediaPlayer mediaPlayer, int newChapter);

    /**
     * An error occurred.
     *
     * @param mediaPlayer media player that raised the event
     */
    void error(MediaPlayer mediaPlayer);

    // === Events relating to the current media =================================

    /**
     * Current media meta data changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param metaType type of meta data that changed
     */
    void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType);

    /**
     * A new sub-item was added to the current media.
     *
     * @param mediaPlayer media player that raised the event
     * @param subItem native sub-item handle
     */
    void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem);

    /**
     * The current media duration changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newDuration new duration (number of milliseconds)
     */
    void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration);

    /**
     * The current media parsed status changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newStatus new parsed status
     */
    void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus);

    /**
     * The current media parsed status changed.
     * <p>
     * This event is always sent after
     * {@link MediaPlayer#requestParseMediaWithOptions(libvlc_media_parse_flag_t...)}
     * is successfully called..
     *
     * @param mediaPlayer media player that raised the event
     * @param newStatus new parsed status
     */
    void mediaParsedStatus(MediaPlayer mediaPlayer, int newStatus);

    /**
     * The current media was freed.
     *
     * @param mediaPlayer media player that raised the event
     */
    void mediaFreed(MediaPlayer mediaPlayer);

    /**
     * The current media state changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newState new state
     */
    void mediaStateChanged(MediaPlayer mediaPlayer, int newState);

    /**
     * A sub-item tree was added to the media.
     *
     * @param mediaPlayer media player that raised the event
     * @param item media item
     */
    void mediaSubItemTreeAdded(MediaPlayer mediaPlayer, libvlc_media_t item);

    // === Synthetic/semantic events ============================================

    /**
     * New media was opened.
     * <p>
     * This is raised only for the "main" media item and not for any sub-items.
     *
     * @param mediaPlayer media player that raised the event
     */
    void newMedia(MediaPlayer mediaPlayer);

    /**
     * A sub-item was played.
     * <p>
     * There is no guarantee the sub-item actually started, but it was at least attempted to be
     * played.
     *
     * @param mediaPlayer media player that raised the event
     * @param subItemIndex index of the sub-item that was played
     */
    void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex);

    /**
     * A sub-item finished playing.
     *
     * @param mediaPlayer media player that raised the event
     * @param subItemIndex index of the sub-item that finished playing
     */
    void subItemFinished(MediaPlayer mediaPlayer, int subItemIndex);

    /**
     * The end of the media sub-items was reached.
     * <p>
     * This event will not be raised if the sub-items are being repeated.
     *
     * @param mediaPlayer media player that raised the event
     */
    void endOfSubItems(MediaPlayer mediaPlayer);
}
