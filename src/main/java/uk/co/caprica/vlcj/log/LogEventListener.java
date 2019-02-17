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

package uk.co.caprica.vlcj.log;

/**
 * Specification for a component that will receive native log messages.
 */
public interface LogEventListener {

    /**
     * Process a native log message.
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
    void log(LogLevel level, String module, String file, Integer line, String name, String header, Integer id, String message);

}
