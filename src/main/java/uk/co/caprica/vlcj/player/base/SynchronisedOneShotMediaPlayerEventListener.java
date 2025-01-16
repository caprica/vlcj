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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of a {@link OneShotMediaPlayerEventListener} that decrements a {@link CountDownLatch} on completion.
 * <p>
 * This can be used e.g. to wait one time for a particular media player event to occur.
 */
public abstract class SynchronisedOneShotMediaPlayerEventListener extends OneShotMediaPlayerEventListener {

    /**
     * Completion synchronisation latch.
     */
    private final CountDownLatch latch = new CountDownLatch(1);

    /**
     * Wait for the event to occur, or to be interrupted.
     *
     * @throws InterruptedException if the thread was interrupted while waiting
     */
    public final void waitForCompletion() throws InterruptedException {
        latch.await();
    }

    /**
     * Wait for the event to occur, or to timeout or be interrupted.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the {@code timeout} argument
     * @return <code>true</code> if the event occurred within the timeout; <code>false</code> if the timeout was exceeded
     * @throws InterruptedException if the thread was interrupted while waiting
     */
    public final boolean waitForCompletion(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }

    @Override
    protected final void onDone(MediaPlayer mediaPlayer) {
        latch.countDown();
    }
}
