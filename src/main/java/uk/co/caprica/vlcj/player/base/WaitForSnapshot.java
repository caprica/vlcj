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

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.waiter.mediaplayer.SnapshotTakenWaiter;

import java.io.File;

/**
 * Private helper to take a snapshot and wait until the corresponding snapshot taken event is received (or an error
 * occurs).
 */
final class WaitForSnapshot extends SnapshotTakenWaiter {

    /**
     * File to save the snapshot into.
     */
    private final File file;

    /**
     * Width for the snapshot, or zero for default.
     */
    private final int width;

    /**
     * Height for the snapshot, or zero for default.
     */
    private final int height;

    /**
     * Create a snapshot taken waiter.
     *
     * @param mediaPlayer media player
     * @param file file to save the snapshot into
     * @param width width, or zero for default
     * @param height height, or zero for default
     */
    WaitForSnapshot(MediaPlayer mediaPlayer, File file, int width, int height) {
        super(mediaPlayer);
        this.file = file;
        this.width = width;
        this.height = height;
    }

    @Override
    protected boolean onBefore(MediaPlayer mediaPlayer) {
        return mediaPlayer.snapshots().save(file, width, height);
    }

}
