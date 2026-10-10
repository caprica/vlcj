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

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Private helper to take a screenshot and wait until the corresponding screenshot taken event is received (or an error
 * occurs).
 */
final class WaitForScreenshot extends MediaPlayerEventAdapter {

    /**
     * Media player that generates the screenshot.
     */
    private final MediaPlayer mediaPlayer;

    /**
     * File to save the screenshot into.
     */
    private final File file;

    /**
     * Width for the screenshot, or zero for default.
     */
    private final int width;

    /**
     * Height for the screenshot, or zero for default.
     */
    private final int height;

    /**
     * Synchronisation latch.
     */
    private final CountDownLatch screenshotTakenLatch = new CountDownLatch(1);

    /**
     * Screenshot result, a filename, or <code>null</code>.
     */
    @Nullable
    private volatile String screenshotResult;

    /**
     * Create a screenshot taken waiter.
     *
     * @param mediaPlayer media player that generates the screenshot
     * @param file file to save the screenshot into
     * @param width width, or zero for default
     * @param height height, or zero for default
     */
    WaitForScreenshot(MediaPlayer mediaPlayer, File file, int width, int height) {
        this.mediaPlayer = mediaPlayer;
        this.file = file;
        this.width = width;
        this.height = height;
    }

    /**
     * Wait for a screenshot to be generated.
     *
     * @return filename where the screenshot was saved; or <code>null</code> if an error occurred
     */
    @Nullable String getScreenshot() {
        return requestScreenshot(0);
    }

    /**
     * Wait for a screenshot to be generated.
     *
     * @param timeout number of milliseconds to wait for the screenshot to be generated before timing out
     * @return filename where the screenshot was saved; or <code>null</code> if an error occurred
     */
    @Nullable String getScreenshot(long timeout) {
        return requestScreenshot(timeout);
    }

    private @Nullable String requestScreenshot(long timeout) {
        try {
            mediaPlayer.events().addMediaPlayerEventListener(this);
            if (mediaPlayer.snapshots().save(file, width, height)) {
                if (timeout == 0) {
                    screenshotTakenLatch.await();
                } else {
                    screenshotTakenLatch.await(timeout, TimeUnit.MILLISECONDS);
                }
                return screenshotResult;
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
    public void screenshotTaken(MediaPlayer mediaPlayer, String filename) {
        screenshotResult = filename;
        screenshotTakenLatch.countDown();
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        screenshotResult = null;
        screenshotTakenLatch.countDown();
    }
}
