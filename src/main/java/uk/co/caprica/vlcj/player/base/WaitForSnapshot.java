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

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Private helper to take a snapshot and wait until the corresponding snapshot taken event is received (or an error
 * occurs).
 */
final class WaitForSnapshot extends MediaPlayerEventAdapter {

    /**
     * Media player that generates the snapshot.
     */
    private final MediaPlayer mediaPlayer;

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
     * Synchronisation latch.
     */
    private final CountDownLatch snapshotTakenLatch = new CountDownLatch(1);

    /**
     * Snapshot result, a filename, or <code>null</code>.
     */
    private volatile String snapshotResult;

    /**
     * Create a snapshot taken waiter.
     *
     * @param mediaPlayer media player that generates the snapshot
     * @param file file to save the snapshot into
     * @param width width, or zero for default
     * @param height height, or zero for default
     */
    WaitForSnapshot(MediaPlayer mediaPlayer, File file, int width, int height) {
        this.mediaPlayer = mediaPlayer;
        this.file = file;
        this.width = width;
        this.height = height;
    }

    /**
     * Wait for a snapshot to be generated.
     *
     * @return filename where the snapshot was saved; or <code>null</code> if an error occurred
     */
    String getSnapshot() {
        return requestSnapshot(0);
    }

    /**
     * Wait for a snapshot to be generated.
     *
     * @param timeout number of milliseconds to wait for the snapshot to be generated before timing out
     * @return filename where the snapshot was saved; or <code>null</code> if an error occurred
     */
    String getSnapshot(long timeout) {
        return requestSnapshot(timeout);
    }

    private String requestSnapshot(long timeout) {
        try {
            mediaPlayer.events().addMediaPlayerEventListener(this);
            if (mediaPlayer.snapshots().save(file, width, height)) {
                if (timeout == 0) {
                    snapshotTakenLatch.await();
                } else {
                    snapshotTakenLatch.await(timeout, TimeUnit.MILLISECONDS);
                }
                return snapshotResult;
            } else {
                return null;
            }
        } catch (InterruptedException e ) {
            throw new RuntimeException(e);
        } finally {
            mediaPlayer.events().removeMediaPlayerEventListener(this);
        }
    }

    @Override
    public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
        snapshotResult = filename;
        snapshotTakenLatch.countDown();
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        snapshotResult = null;
        snapshotTakenLatch.countDown();
    }
}
