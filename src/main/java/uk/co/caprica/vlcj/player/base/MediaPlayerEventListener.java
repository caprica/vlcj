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

import org.jspecify.annotations.Nullable;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.parser.Pictures;

import javax.swing.*;

/**
 * Specification for a component that is interested in receiving event notifications from the media player.
 * <p>
 * Events are <em>not</em> raised on the Swing Event Dispatch thread so if updating user interface components in
 * response to these events care must be taken to use {@link SwingUtilities#invokeLater(Runnable)}.
 * <p>
 * Equally, care must be taken not to call back into LibVLC from the event handling thread - if an event handler needs
 * to call back into LibVLC it should use the {@link MediaPlayer#submit(Runnable)} method to submit a task for
 * asynchronous execution.
 * <p>
 * In the listener callback methods that pass a {@link MediaRef}, that reference is valid <em>only for the duration of
 * the listener call</em>, if a permanent reference is needed then one of the following must be used:
 * <ul>
 *   <li>{@link MediaRef#newMediaRef()}</li>
 *   <li>{@link MediaRef#duplicateMediaRef()}</li>
 *   <li>{@link MediaRef#newMedia()}</li>
 * </ul>
 * <p>
 * Similarly for listener callback methods that pass a {@link MediaList}, that too is valid <em>only for the duration of
 * the listener call</em>, if a permanent reference is needed then one of the following must be used:
 * <ul>
 *     <li>{@link MediaList#newMediaListRef()}</li>
 *     <li>{@link MediaList#newMediaList()}</li>
 * </ul>
 *
 * @see MediaPlayerEventAdapter
 */
public interface MediaPlayerEventListener {

    // === Events relating to the media player ==================================

    /**
     * The media changed.
     * <p>
     * The media reference is valid only for the duration of the listener call, see class documentation.
     *
     * @param mediaPlayer media player that raised the event
     * @param media new media instance
     */
    void mediaChanged(MediaPlayer mediaPlayer, MediaRef media);

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
    void bufferingChanged(MediaPlayer mediaPlayer, float newCache);

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
     * <p>
     * A stopped event may be raised under certain circumstances even if the media player is not playing (e.g. as part
     * of the associated media list player sub-item handling). Client applications must therefore be prepared to handle
     * such a situation.
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
     * Media is stopping.
     * <p>
     * The media is not fully stopped until the {@link #stopped(MediaPlayer)} event is raised.
     *
     * @param mediaPlayer media player that raised the event
     */
    void stopping(MediaPlayer mediaPlayer);

    /**
     * Media finished playing, i.e. the end was reached.
     *
     * @param mediaPlayer media player that raised the event
     */
    void finished(MediaPlayer mediaPlayer);

    /**
     * Media play-back position changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newTime time, in <strong>microseconds</strong>
     * @param newPosition percentage between 0.0 and 1.0
     */
    void positionChanged(MediaPlayer mediaPlayer, long newTime, double newPosition);

    /**
     * Media title list changed.
     *
     * @param mediaPlayer media player that raised the event
     */
    void titleListChanged(MediaPlayer mediaPlayer);

    /**
     * Media title selection changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param titleDescription details of the title that changed
     * @param index new title
     */
    void titleSelectionChanged(MediaPlayer mediaPlayer, TitleDescription titleDescription, int index);

    /**
     * A snapshot was taken.
     *
     * @param mediaPlayer media player that raised the event
     * @param filename name of the file containing the snapshot
     */
    void screenshotTaken(MediaPlayer mediaPlayer, String filename);

    /**
     * Media parsed status changed.
     * <p>
     * The media reference is valid only for the duration of the listener call, see class documentation.
     *
     * @param mediaPlayer media player that raised the event
     * @param mediaRef media that changed parsed status
     */
    void mediaParsed(MediaPlayer mediaPlayer, MediaRef mediaRef);

    /**
     * Media metadata changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param metaData new metadata
     */
    void mediaMetaChanged(MediaPlayer mediaPlayer, MetaData metaData);

    /**
     * Media parsed status changed.
     * <p>
     * The media list reference is valid only for the duration of the listener call, see class documentation.
     *
     * @param mediaPlayer media player that raised the event
     * @param mediaList new list of subitems
     */
    void mediaSubitemsChanged(MediaPlayer mediaPlayer, MediaList mediaList);

    /**
     * Media parsed status changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param pictures pictures that were added
     */
    void mediaAttachmentsAdded(MediaPlayer mediaPlayer, Pictures pictures);

    /**
     * Media length changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newLength new length (number of <strong>microseconds</strong>)
     */
    void lengthChanged(MediaPlayer mediaPlayer, long newLength);

    /**
     * A track was added to the current track list.
     *
     * @param mediaPlayer media player that raised the event
     * @param trackType type of track that was added
     * @param trackId identifier of the added track, see {@link TrackApi#track(String)}
     */
    void trackAdded(MediaPlayer mediaPlayer, TrackType trackType, String trackId);

    /**
     * A track was removed from the current track list.
     *
     * @param mediaPlayer media player that raised the event
     * @param trackType type of track that was removed
     * @param trackId identifier of the removed track, see {@link TrackApi#track(String)}
     */
    void trackRemoved(MediaPlayer mediaPlayer, TrackType trackType, String trackId);

    /**
     * A track in the current track list was updated.
     *
     * @param mediaPlayer media player that raised the event
     * @param trackType type of track that was updated
     * @param trackId identifier of the updated track, see {@link TrackApi#track(String)}
     */
    void trackUpdated(MediaPlayer mediaPlayer, TrackType trackType, String trackId);

    /**
     * The current track selection changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param trackType type of track that was selected
     * @param unselectedTrackId identifier of the unselected track, see {@link TrackApi#track(String)}
     * @param selectedTrackId identifier of the newly selected track, see {@link TrackApi#track(String)}
     */
    void trackSelectionChanged(MediaPlayer mediaPlayer, TrackType trackType, @Nullable String unselectedTrackId, @Nullable String selectedTrackId);

    /**
     * Next frame returned a new status.
     *
     * @param mediaPlayer media player that raised the event
     * @param newStatus new status
     */
    void nextFrameStatus(MediaPlayer mediaPlayer, FrameStatus newStatus);

    /**
     * Previous frame returned a new status.
     *
     * @param mediaPlayer media player that raised the event
     * @param newStatus new status
     */
    void previousFrameStatus(MediaPlayer mediaPlayer, FrameStatus newStatus);

    /**
     * The number of video outputs changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newCount new number of video outputs
     */
    void videoOutput(MediaPlayer mediaPlayer, int newCount);

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
     * The audio was muted/unmuted.
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
     * @param titleDescription description of the title containing the chapter that changed
     * @param titleIndex new chapter
     * @param chapterDescription description of the chapter that changed
     * @param chapterIndex new chapter index
     */
    void chapterSelectionChanged(MediaPlayer mediaPlayer, TitleDescription titleDescription, int titleIndex, ChapterDescription chapterDescription, int chapterIndex);

    /**
     * The recording status changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param recording <code>true</code> if recording started; <code>false</code> if recording stopped
     * @param recordedFilePath path to the file that contains the recording
     */
    void recordChanged(MediaPlayer mediaPlayer, boolean recording, String recordedFilePath);

    /**
     * A program was added.
     *
     * @param mediaPlayer media player that raised the event
     * @param id program identifier, see {@link ProgramApi#get(int)}
     */
    void programAdded(MediaPlayer mediaPlayer, int id);

    /**
     * A program was deleted.
     *
     * @param mediaPlayer media player that raised the event
     * @param id program identifier, see {@link ProgramApi#get(int)}
     */
    void programRemoved(MediaPlayer mediaPlayer, int id);

    /**
     * A program was updated.
     *
     * @param mediaPlayer media player that raised the event
     * @param id program identifier, see {@link ProgramApi#get(int)}
     */
    void programUpdated(MediaPlayer mediaPlayer, int id);

    /**
     * A program was selected.
     *
     * @param mediaPlayer media player that raised the event
     * @param unselectedId identifier of the program that was unselected, see {@link ProgramApi#get(int)}
     * @param selectedId identifier of the program that was selected, see {@link ProgramApi#get(int)}
     */
    void programSelected(MediaPlayer mediaPlayer, int unselectedId, int selectedId);

    /**
     * Media player capabilities changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param oldCapabilties old capabilities
     * @param newCapabilties new capabilities
     */
    void capabilitiesChanged(MediaPlayer mediaPlayer, Capabilties oldCapabilties, Capabilties newCapabilties);

    /**
     * Playback rate changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newRate new playback rate, 1.0 is normal speed
     */
    void rateChanged(MediaPlayer mediaPlayer, float newRate);

    /**
     * An error occurred.
     *
     * @param mediaPlayer media player that raised the event
     */
    void error(MediaPlayer mediaPlayer);

    // === Synthetic/semantic events ============================================

    /**
     * Media player is ready (to enable features like logo and marquee) after
     * the media has started playing.
     * <p>
     * The implementation will fire this event once on receipt of the first
     * native position-changed event with a position value greater than zero.
     * <p>
     * The event will be fired again if the media is played again after a native
     * stopped or finished event is received.
     * <p>
     * Waiting for this event may be more reliable than using {@link #playing(MediaPlayer)}
     * or {@link #videoOutput(MediaPlayer, int)} in some cases (logo and marquee
     * already mentioned, also setting audio tracks, subtitle tracks and so on).
     *
     * @param mediaPlayer media player that raised the event
     */
    void mediaPlayerReady(MediaPlayer mediaPlayer);
}
