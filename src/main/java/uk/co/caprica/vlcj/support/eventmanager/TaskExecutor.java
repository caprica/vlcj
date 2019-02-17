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

package uk.co.caprica.vlcj.support.eventmanager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Singleton component used to execute tasks on a thread different to the native event handler background thread.
 * <p>
 * Native events are generated on a native event callback thread. It is not allowed to call back into LibVLC from this
 * thread, if you do either the call will be ineffective, strange behaviour will happen, or a fatal JVM crash may occur.
 * <p>
 * To mitigate this, tasks can be submitted, serialised and executed using this service.
 * <p>
 * Internally a single-threaded executor service is used to execute tasks that need to be off-loaded from a native
 * callback thread.
 * <p>
 * See {@link #submit(Runnable)}.
 */
public final class TaskExecutor {

    /**
     * Default timeout to use when waiting for scheduled tasks to complete during shutdown.
     * <p>
     * The same timeout is used twice, first waiting for a clean shutdown then again waiting after forcing a shutdown if
     * necessary.
     */
    private static final long DEFAULT_TIMEOUT = 5000;

    /**
     * Single-threaded service to execute tasks that need to be off-loaded from a native callback thread.
     * <p>
     * Native events are generated on a native event callback thread. It is not allowed to call back into LibVLC from
     * this thread, if you do either the call will be ineffective, strange behaviour will happen, or a fatal JVM crash
     * may occur.
     * <p>
     * To mitigate this, tasks can be serialised and executed using this service.
     * <p>
     * See {@link #submit(Runnable)}.
     */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Submit a task for asynchronous execution.
     *
     * @param r task to submit
     */
    public void submit(Runnable r) {
        executor.submit(r);
    }

    /**
     * Release this component.
     * <p>
     * An attempt is made to perform an orderly shutdown, so any tasks in the queue are given a chance to complete
     * cleanly within the default timeout period.
     * <p>
     * Consequently there may be a short delay before this method returns.
     * <p>
     * If there are no tasks waiting, this method will return immediately.
     */
    public void release() {
        shutdownExecutor(DEFAULT_TIMEOUT);
    }

    /**
     * Release this component.
     * <p>
     * An attempt is made to perform an orderly shutdown, so any tasks in the queue are given a chance to complete
     * cleanly within the specified timeout period.
     * <p>
     * Consequently there may be a short delay before this method returns.
     * <p>
     * If there are no tasks waiting, this method will return immediately.
     *
     * @param timeout timeout, milliseconds
     */
    public void release(long timeout) {
        shutdownExecutor(timeout);
    }

    /**
     * Shutdown the task executor service.
     * <p>
     * Care must be taken to prevent fatal JVM crashes during shutdown due to tasks that may be still be waiting in the
     * queue to be executed (e.g. we do not want to destroy the native media player if a task is running that is going
     * to invoke a call on the native media player).
     * <p>
     * So, we first shutdown the executor service then await termination of all tasks. If there are no pending tasks we
     * will terminate immediately as normal. If there are tasks, we wait for a short timeout period before forcing a
     * termination anyway.
     * <p>
     * We should never really be waiting any significant amount of time for the queued tasks to terminate because they
     * will be very few in number, and should execute very quickly anyway. Even the short wait before timeout may be
     * useful to avoid any hard crashes during clean-up.
     */
    private void shutdownExecutor(long timeout) {
        // Immediately stop accepting new tasks
        executor.shutdown();
        try {
            // Give a chance for all existing tasks to complete cleanly
            if (!executor.awaitTermination(timeout, TimeUnit.MILLISECONDS)) {
                // Not all tasks completed cleanly in time, so force a shutdown
                executor.shutdownNow();
                executor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
            }
        }
        catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

}
