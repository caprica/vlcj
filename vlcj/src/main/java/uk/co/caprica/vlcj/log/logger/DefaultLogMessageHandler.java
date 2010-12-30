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
 * Copyright 2009, 2010 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.log.logger;

import java.util.List;

import uk.co.caprica.vlcj.log.LogMessage;
import uk.co.caprica.vlcj.log.LogMessageHandler;
import uk.co.caprica.vlcj.log.Logger;

/**
 * Basic implementation of a log message handler that simply sends the native
 * log messages to the local log.
 */
public class DefaultLogMessageHandler implements LogMessageHandler {

  @Override
  public void messages(List<LogMessage> messages) {
    Logger.trace("messages()");
    
    for(LogMessage message : messages) {
      switch(message.severity()) {
        case ERR:
          Logger.error("(libvlc {}) {}", message.name(), message.message());
          break;
          
        case WARN:
          Logger.warn("(libvlc {}) {}", message.name(), message.message());
          break;
          
        case INFO:
          Logger.info("(libvlc {}) {}", message.name(), message.message());
          break;
          
        case DBG:
          Logger.debug("(libvlc {}) {}", message.name(), message.message());
          break;
      }
    }
  }
}
