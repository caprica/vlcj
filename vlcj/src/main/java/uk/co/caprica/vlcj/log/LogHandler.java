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

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * Consumer for the native log.
 * <p>
 * If a log is opened, it's contents must be regularly consumed to prevent 
 * clogging - this implementation creates a background thread to consume the
 * log messages and send them to the local log sub-system.
 */
public class LogHandler {

  /**
   * Log.
   */
  private static final Logger LOG = Logger.getLogger(LogHandler.class);

  /**
   * The log. 
   */
  private final Log log;
  
  /**
   * Log refresh period, in milliseconds.
   */
  private final int period;
  
  /**
   * Background executor.
   */
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  
  /**
   * Create a new log handler.
   * 
   * @param log log
   * @param period log refresh period, in milliseconds
   */
  public LogHandler(Log log, int period) {
    if(LOG.isDebugEnabled()) {LOG.debug("LogHandler(log=" + log + ",period=" + period + ")");}
    
    this.log = log;
    this.period = period;
  }

  /**
   * 
   */
  public void start() {
    if(LOG.isDebugEnabled()) {LOG.debug("start()");}
    
    executor.scheduleAtFixedRate(new LogProcessor(), period, period, TimeUnit.MILLISECONDS);
  }
  
  /**
   * 
   */
  public void release() {
    if(LOG.isDebugEnabled()) {LOG.debug("release()");}
    
    executor.shutdown();
  }
  
  /**
   *
   */
  private final class LogProcessor implements Runnable {

    @Override
    public void run() {
      LOG.trace("run()");
      
      int count = log.count();
      if(LOG.isTraceEnabled()) {LOG.trace("count=" + count);}
      
      List<LogMessage> messages = log.messages();
      for(LogMessage message : messages) {
        LOG.debug("message=" + message);
      }
    }
  }
}
