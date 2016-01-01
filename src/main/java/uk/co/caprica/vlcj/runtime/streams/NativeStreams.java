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

package uk.co.caprica.vlcj.runtime.streams;

import uk.co.caprica.vlcj.binding.LibC;

import com.sun.jna.Pointer;

/**
 * Provide a way to redirect the native process standard output and error streams.
 * <p>
 * This can be useful with LibVLC because VLC and its various plugins can generate noisy and
 * superfluous log messages to the native process standard error stream.
 * <p>
 * The normal Java redirection of System.out and System.err can not redirect the native process
 * streams.
 * <p>
 * This should be used immediately at the start of your application.
 *
 * <strong>This class is experimental and may not work on all platforms!</strong>
 */
public final class NativeStreams {

    /**
     * File descriptor for stdout.
     */
    private static final int STDOUT_FD = 1;

    /**
     * File descriptor for stdin.
     */
    private static final int STDERR_FD = 2;

    /**
     * Open files in write mode.
     */
    private static final String STREAM_MODE = "w";

    /**
     * The stdout stream.
     */
    private Pointer outputStream;

    /**
     * The redirected stdout stream.
     */
    private Pointer redirectedOutputStream;

    /**
     * The stderr stream.
     */
    private Pointer errorStream;

    /**
     * The redirected stderr stream.
     */
    private Pointer redirectedErrorStream;

    /**
     * Redirect native streams to files.
     *
     * @param outputTo new stdout file name, or <code>null</code> for no redirection of stdout
     * @param errorTo new stderr file name, or <code>null</code> for no redirection of stderr
     */
    public NativeStreams(String outputTo, String errorTo) {
        if (outputTo != null) {
            if (!redirectOutputTo(outputTo)) {
                throw new IllegalStateException("Failed to redirect stdout");
            }
        }
        if (errorTo != null) {
            if (!redirectErrorTo(errorTo)) {
                throw new IllegalStateException("Failed to redirect stderr");
            }
        }
    }

    /**
     * Redirect the native process standard output stream to a file.
     *
     * @param target file
     * @return <code>true</code> if the stream was successfully redirected; <code>false</code> otherwise
     */
    private boolean redirectOutputTo(String target) {
        outputStream = LibC.INSTANCE.fdopen(STDOUT_FD, STREAM_MODE);
        if (outputStream != null) {
            redirectedOutputStream = LibC.INSTANCE.freopen(target, STREAM_MODE, outputStream);
            return redirectedOutputStream != null;
        }
        else {
            return false;
        }
    }

    /**
     * Redirect the native process standard error stream to a file.
     *
     * @param target file
     * @return <code>true</code> if the stream was successfully redirected; <code>false</code> otherwise
     */
    private boolean redirectErrorTo(String target) {
        errorStream = LibC.INSTANCE.fdopen(STDERR_FD, STREAM_MODE);
        if (errorStream != null) {
            redirectedErrorStream = LibC.INSTANCE.freopen(target, STREAM_MODE, errorStream);
            return redirectedErrorStream != null;
        }
        else {
            return false;
        }
    }

    /**
     * Close the redirected files.
     */
    public void release() {
        if (redirectedOutputStream != null) {
            LibC.INSTANCE.fclose(redirectedOutputStream);
        }
        if (redirectedErrorStream != null) {
            LibC.INSTANCE.fclose(redirectedErrorStream);
        }
    }
}
