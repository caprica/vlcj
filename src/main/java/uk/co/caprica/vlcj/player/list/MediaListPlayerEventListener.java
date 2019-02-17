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

package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.media.MediaRef;

import javax.swing.*;

/**
 * Specification for a component that is interested in receiving event notifications from the media list player.
 * <p>
 * Events are <em>not</em> raised on the Swing Event Dispatch thread so if updating user interface components in
 * response to these events care must be taken to use {@link SwingUtilities#invokeLater(Runnable)}.
 * <p>
 * Equally, care must be taken not to call back into LibVLC from the event handling thread - if an event handler needs
 * to call back into LibVLC it should use the {@link MediaListPlayer#submit(Runnable)} method to submit a task for
 * asynchronous execution.
 *
 * @see MediaListPlayerEventAdapter
 */
public interface MediaListPlayerEventListener {

    /**
     * The media list player finished playing the last item in the list.
     * <p>
     * When the media list player mode is {@link PlaybackMode#DEFAULT}, this event will fire
     * after the last item has been played.
     * <p>
     * When the mode is {@link PlaybackMode#LOOP}, the event will not fire at all.
     * <p>
     * When the mode is {@link PlaybackMode#REPEAT}, the event will fire immediately if the
     * media list player is played (nothing plays in this case); the event will not fire at all if a particular item is
     * played (by index).
     *
     * @param mediaListPlayer media list player that raised the event
     */
    void mediaListPlayerFinished(MediaListPlayer mediaListPlayer);

    /**
     * The media list player started playing the next item in the list.
     *
     * @param mediaListPlayer media list player that raised the event
     * @param item next item instance
     */
    void nextItem(MediaListPlayer mediaListPlayer, MediaRef item);

    /**
     * The media list player stopped.
     *
     * @param mediaListPlayer media list player that raised the event
     */
    void stopped(MediaListPlayer mediaListPlayer);

}
