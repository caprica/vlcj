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

import org.apache.log4j.Logger;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_iterator_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_message_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_t;

/**
 * Preliminary native log support.
 * <p>
 * <strong>EXPERIMENTAL.</strong>
 */
public class Log {

  /**
   * Log.
   */
  private static final Logger LOG = Logger.getLogger(Log.class);
  
  // TODO
  private final LibVlc libvlc = LibVlc.LOGGING_INSTANCE;

  /**
   * 
   */
  private final libvlc_instance_t instance;
  
  /**
   * 
   */
  private libvlc_log_t logInstance;
  
  /**
   * 
   * 
   * @param instance
   */
  public Log(libvlc_instance_t instance) {
    if(LOG.isDebugEnabled()) {LOG.debug("LibVlcLog(instance=" + instance + ")");}
    
    this.instance = instance;
  }

  /**
   * 
   */
  public void open() {
    if(LOG.isDebugEnabled()) {LOG.debug("open()");}

    if(logInstance == null) {
      logInstance = libvlc.libvlc_log_open(instance);
    }
    else {
      throw new IllegalStateException("Log is already open");
    }
  }
  
  /**
   * 
   */
  public void close() {
    if(LOG.isDebugEnabled()) {LOG.debug("close()");}

    if(logInstance != null) {
      clear();
      libvlc.libvlc_log_close(logInstance);
      logInstance = null;
    }
  }
  
  /**
   * 
   * 
   * @return
   */
  public int count() {
    if(LOG.isDebugEnabled()) {LOG.debug("count()");}

    return libvlc.libvlc_log_count(logInstance);
  }
  
  /**
   * 
   */
  public void clear() {
    if(LOG.isDebugEnabled()) {LOG.debug("clear()");}

    libvlc.libvlc_log_clear(logInstance);
  }

  /**
   * 
   * @return
   */
  public List<LogMessage> messages() {
    if(LOG.isDebugEnabled()) {LOG.debug("messages()");}
    
    return messages(new ArrayList<LogMessage>(40));
  }

  /**
   * 
   * 
   * @param messages
   * @return
   */
  public List<LogMessage> messages(List<LogMessage> messages) {
    if(LOG.isDebugEnabled()) {LOG.debug("messages(messages=[" + messages.size() + "])");}

    libvlc_log_iterator_t it = null;
    try {
      it = libvlc.libvlc_log_get_iterator(logInstance);
      
      while(libvlc.libvlc_log_iterator_has_next(it) != 0) {
        libvlc_log_message_t message = new libvlc_log_message_t(); 
        message = libvlc.libvlc_log_iterator_next(it, message);
        if(message != null) {
          // TODO convert severity to enum
          messages.add(new LogMessage(null, message.psz_type, message.psz_name, message.psz_header, message.psz_message));
        }
      }
    }
    finally {
      if(it != null) {
        libvlc.libvlc_log_iterator_free(it);
      }
    }
    
    return messages;
  }

  @Override
  protected void finalize() throws Throwable {
    if(logInstance != null) {
      close();
    }
  }
}
