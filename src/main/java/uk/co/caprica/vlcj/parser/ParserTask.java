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

package uk.co.caprica.vlcj.parser;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.internal.libvlc_parser_task_t;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_parser_task_release;

/**
 * Encapsulation of a parser task.
 * <p>
 * The internal implementation is opaque to client applications.
 */
public final class ParserTask {

    final libvlc_parser_task_t taskInstance;

    ParserTask(libvlc_parser_task_t taskInstance) {
        this.taskInstance = taskInstance;
    }

    public void release() {
        libvlc_parser_task_release(taskInstance);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ParserTask)) {
            return false;
        }
        ParserTask other = (ParserTask) obj;
        return taskInstance.equals(other.taskInstance);
    }

    @Override
    public int hashCode() {
        return taskInstance.hashCode();
    }

    @Override
    public String toString() {
        return Long.toString(Pointer.nativeValue(taskInstance.getPointer()));
    }
}
