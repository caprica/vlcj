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

package uk.co.caprica.vlcj.log;

import java.util.ArrayList;
import java.util.List;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_iterator_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_message_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_t;

/**
 * Native log support.
 */
public class Log {

  /**
   * 
   */
  private final LibVlc libvlc;

  /**
   * 
   */
  private final libvlc_instance_t instance;

  /**
   * 
   */
  private LogLevel threshold;

  /**
   * 
   */
  private libvlc_log_t logInstance;

  /**
   * 
   * 
   * @param libvlc 
   * @param instance
   */
  public Log(LibVlc libvlc, libvlc_instance_t instance) {
    Logger.debug("Log(libvlc={},instance={})", libvlc, instance);

    this.libvlc = libvlc;
    this.instance = instance;
  }

  /**
   * 
   * 
   * @param threshold
   */
  public void setThreshold(LogLevel threshold) {
    Logger.debug("setThreshold(threshold={})", threshold);
    
    this.threshold = threshold;
  }
  
  /**
   * Open a log instance.
   */
  public void open() {
    Logger.debug("open()");

    if(logInstance == null) {
      logInstance = libvlc.libvlc_log_open(instance);
    }
    else {
      throw new IllegalStateException("Log is already open");
    }
  }
  
  /**
   * Close the log instance.
   * <p>
   * The underlying native log itself is not actually 'closed'.
   */
  public void close() {
    Logger.debug("close()");

    if(logInstance != null) {
      clear();
      libvlc.libvlc_log_close(logInstance);
      logInstance = null;
    }
  }
  
  /**
   * Get the count of the number of messages in the Logger.
   * 
   * @return number of messages in the log
   */
  public int count() {
    Logger.trace("count()");

    return libvlc.libvlc_log_count(logInstance);
  }
  
  /**
   * Clear (remove all messages from) the Logger.
   */
  public void clear() {
    Logger.trace("clear()");

    libvlc.libvlc_log_clear(logInstance);
  }

  /**
   * Get all of the messages currently in the Logger.
   * <p>
   * The log will be cleared after the messages have been retrieved.
   * 
   * @return messages
   */
  public List<LogMessage> messages() {
    Logger.trace("messages()");
    
    return messages(new ArrayList<LogMessage>(40));
  }

  /**
   * Get all of the messages currently in the Logger.
   * <p>
   * The log will be cleared after the messages have been retrieved.
   * 
   * @param messages list to store the messages in
   * @return messages
   */
  public List<LogMessage> messages(List<LogMessage> messages) {
    Logger.trace("messages(messages=[{}])", messages.size());

    libvlc_log_iterator_t it = null;
    try {
      it = libvlc.libvlc_log_get_iterator(logInstance);
      
      while(libvlc.libvlc_log_iterator_has_next(it) != 0) {
        libvlc_log_message_t message = new libvlc_log_message_t(); 
        message = libvlc.libvlc_log_iterator_next(it, message);
        if(message != null) {
          LogLevel severity = LogLevel.value(message.i_severity);
          if(threshold == null || message.i_severity <= threshold.intValue()) {
            messages.add(new LogMessage(severity, message.psz_type, message.psz_name, message.psz_header, message.psz_message));
          }
        }
      }
    }
    finally {
      if(it != null) {
        libvlc.libvlc_log_iterator_free(it);
      }
    }
    
    // Iterating the native log does not clear it, so clear it
    // FIXME Is there a race where there is a missed log message, and could that be fixed?
    clear();
    
    return messages;
  }
}
