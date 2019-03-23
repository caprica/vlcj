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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.State;

import javax.swing.*;

/**
 * Specification for a component that is interested in receiving event notifications from the media.
 * <p>
 * Events are <em>not</em> raised on the Swing Event Dispatch thread so if updating user interface components in
 * response to these events care must be taken to use {@link SwingUtilities#invokeLater(Runnable)}.
 * <p>
 * Equally, care must be taken not to call back into LibVLC from the event handling thread - if an event handler needs
 * to call back into LibVLC it should use the {@link MediaPlayerFactory#submit(Runnable)} method to submit a task for
 * asynchronous execution.
 *
 * @see MediaEventAdapter
 */
public interface MediaEventListener {

    /**
     * Current media meta data changed.
     *
     * @param media media that raised the event
     * @param metaType type of meta data that changed
     */
    void mediaMetaChanged(Media media, Meta metaType);

    /**
     * A new sub-item was added to the current media.
     *
     * @param media media that raised the event
     * @param newChild native sub-item handle
     */
    void mediaSubItemAdded(Media media, MediaRef newChild);

    /**
     * The current media duration changed.
     *
     * @param media media that raised the event
     * @param newDuration new duration (number of milliseconds)
     */
    void mediaDurationChanged(Media media, long newDuration);

    /**
     * The current media parsed status changed.
     *
     * @param media media that raised the event
     * @param newStatus new parsed status
     */
    void mediaParsedChanged(Media media, MediaParsedStatus newStatus);

    /**
     * The current media was freed.
     *
     * @param media media that raised the event
     * @param mediaFreed
     */
    void mediaFreed(Media media, MediaRef mediaFreed);

    /**
     * The current media state changed.
     *
     * @param media media that raised the event
     * @param newState new state
     */
    void mediaStateChanged(Media media, State newState);

    /**
     * A sub-item tree was added to the media.
     *
     * @param media media that raised the event
     * @param item media item
     */
    void mediaSubItemTreeAdded(Media media, MediaRef item);

    /**
     *
     * @param media
     * @param picture
     */
    void mediaThumbnailGenerated(Media media, Picture picture);

}
