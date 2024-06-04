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
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.log;

import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.vlcj.binding.lib.LibC;
import uk.co.caprica.vlcj.binding.support.strings.NativeString;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_t;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_log_get_context;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_log_get_object;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_log_set;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_log_unset;

/**
 * Encapsulation of the vlc native log.
 * <p>
 * The native library specifies that implementations of native log handlers (like that encapsulated within this class)
 * must be thread-safe.
 * <p>
 * The default log level is {@link LogLevel#NOTICE}, this can be changed by invoking {@link #setLevel(LogLevel)}.
 */
public final class NativeLog {

    /**
     * Default string buffer size.
     * <p>
     * Plus one for the null terminator.
     */
    private static final int BUFFER_SIZE = 200 + 1;

    /**
     * Collection of media event listeners.
     * <p>
     * A {@link CopyOnWriteArrayList} is used defensively so as not to interfere with the processing of any existing
     * events that may be being generated by the native callback in the unlikely case that a listener is being added or
     * removed.
     */
    private final List<LogEventListener> eventListenerList = new CopyOnWriteArrayList<LogEventListener>();

    /**
     * LibVlc instance.
     */
    private final libvlc_instance_t instance;

    /**
     * Native log callback.
     */
    private NativeLogCallback callback = new NativeLogCallback();

    /**
     * Log level.
     * <p>
     * Set to <code>null</code> to suppress all log messages.
     */
    private LogLevel logLevel = LogLevel.NOTICE;

    /**
     * Create a new native log component.
     *
     * @param instance libvlc instance
     */
    public NativeLog(libvlc_instance_t instance) {
        this.instance = instance;

        libvlc_log_set(instance, callback, null);
    }

    /**
     * Add a component to be notified of log messages.
     *
     * @param listener component to add
     */
    public final void addLogListener(LogEventListener listener) {
       eventListenerList.add(listener);
    }

    /**
     * Remove a component previously added so it is no longer notified of log messages.
     *
     * @param listener component to remove
     */
    public final void removeLogListener(LogEventListener listener) {
        eventListenerList.remove(listener);
    }

    /**
     * Set the log threshold level.
     * <p>
     * Only log messages that are equal to or exceed this threshold are notified to
     * listeners.
     *
     * @param logLevel log threshold level
     */
    public final void setLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * Get the log threshold level.
     *
     * @return level
     */
    public final LogLevel getLevel() {
        return logLevel;
    }

    /**
     * Release the native log component.
     */
    public final void release() {
        eventListenerList.clear();
        libvlc_log_unset(instance);
    }

    /**
     * Callback to receive native log events.
     * <p>
     * This implementation <em>must</em> be thread-safe - this is why we do not re-use a shared buffer.
     */
    private final class NativeLogCallback implements libvlc_log_cb {

        private NativeLogCallback() {
            Native.setCallbackThreadInitializer(this, new CallbackThreadInitializer(true, false, "log-events"));
        }

        @Override
        public void log(Pointer data, int level, libvlc_log_t ctx, String format, Pointer args) {
            // If the log is not being suppressed...
            if (logLevel != null && level >= logLevel.intValue()) {
                // Allocate a new buffer to hold the formatted log message
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
                // Delegate to the native library to format the log message
                int size = LibC.INSTANCE.vsnprintf(byteBuffer, byteBuffer.capacity(), format, args);
                // If the message was formatted without error...
                if (size >= 0) {
                    // Determine the number of available characters (actually number of bytes) - note that the message
                    // may have been truncated if the buffer was not large enough
                    size = Math.min(size, BUFFER_SIZE);
                    // Create a new string from the byte buffer contents
                    byte[] bytes = new byte[size];
                    byteBuffer.get(bytes);
                    String message = new String(bytes);
                    if (message.length() > 0) {
                        // Get the information about the object that emitted the log statement
                        PointerByReference modulePointer = new PointerByReference();
                        PointerByReference filePointer = new PointerByReference();
                        IntByReference linePointer = new IntByReference();
                        libvlc_log_get_context(ctx, modulePointer, filePointer, linePointer);
                        PointerByReference namePointer = new PointerByReference();
                        PointerByReference headerPointer = new PointerByReference();
                        LongByReference idPointer = new LongByReference();
                        libvlc_log_get_object(ctx, namePointer, headerPointer, idPointer);
                        String module = getString(modulePointer);
                        String file = getString(filePointer);
                        Integer line = linePointer.getValue();
                        String name = getString(namePointer);
                        String header = getString(headerPointer);
                        Long id = idPointer.getValue();
                        // ...send the event
                        raiseLogEvent(LogLevel.level(level), module, file, line, name, header, id, message);
                    }
                }
                else {
                    // This occurs when vsnprintf failed, rather than just truncating the message
                    raiseLogEvent(LogLevel.ERROR, null, null, null, null, null, null, "Failed to format native log message");
                }
            }
        }
    }

    /**
     * Dereference a pointer (that may be <code>null</code>) to get a string.
     *
     * @param pointer pointer
     * @return string, or <code>null</code> if the pointer is <code>null</code>
     */
    private String getString(PointerByReference pointer) {
        // The string is copied but not freed, the native string will be reclaimed when the native callback returns
        Pointer value = pointer.getValue();
        return value != null ? NativeString.copyNativeString(value) : null;
    }

    /**
     * Raise a log event.
     *
     * @param level log level
     * @param module module
     * @param file file
     * @param line line number
     * @param name name
     * @param header header
     * @param id object identifier
     * @param message log message
     */
    private void raiseLogEvent(LogLevel level, String module, String file, Integer line, String name, String header, Long id, String message) {
        for (LogEventListener listener : eventListenerList) {
            try {
                listener.log(level, module, file, line, name, header, id, message);
            }
            catch (Exception e) {
                // Not much we can do really
            }
        }
    }

}
