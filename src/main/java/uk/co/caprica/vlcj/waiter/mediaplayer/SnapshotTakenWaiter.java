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

package uk.co.caprica.vlcj.waiter.mediaplayer;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * Implementation of a condition that waits for the media player to report that it has finished taking a snapshot.
 */
public class SnapshotTakenWaiter extends MediaPlayerWaiter<String> {

    /**
     * Create a condition.
     *
     * @param mediaPlayer media player
     */
    public SnapshotTakenWaiter(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    @Override
    public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
        ready(filename);
    }

}
