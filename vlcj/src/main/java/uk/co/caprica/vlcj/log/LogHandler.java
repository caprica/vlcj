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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Consumer for the native log.
 * <p>
 * If a log is opened, it's contents must be regularly consumed to prevent 
 * clogging - this implementation creates a background thread to consume the
 * log messages for processing by client applications.
 * <p>
 * This implementation periodically checks the native libvlc log to retrieve
 * log messages.
 * <p>
 * Log message handler implementations may be added to the log handler itself
 * so that individual messages may be inspected and processed - for example to
 * send the messages to a debug log or raise events when certain log messages 
 * are detected.
 * <p>
 * In this way the proper consumption of the native log messages is isolated 
 * from the specific message handling implementation.
 */
public class LogHandler {

  /**
   * The Logger. 
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
   * Collection of log message handlers.
   */
  private final List<LogMessageHandler> logMessageHandlers = new ArrayList<LogMessageHandler>();
  
  /**
   * Create a new log handler.
   * 
   * @param log log
   * @param period log refresh period, in milliseconds
   */
  public LogHandler(Log log, int period) {
    Logger.debug("LogHandler(log={},period={})", log, period);
    this.log = log;
    this.period = period;
  }

  /**
   * 
   * 
   * @param logMessageHandler
   * @return this
   */
  public LogHandler addLogMessageHandler(LogMessageHandler logMessageHandler) {
    Logger.debug("addLogMessageHandler(logMessageHandler={})", logMessageHandler);
    logMessageHandlers.add(logMessageHandler);
    return this;
  }
  
  /**
   * 
   * 
   * @param logMessageHandler
   */
  public void removeLogMessageHandler(LogMessageHandler logMessageHandler) {
    Logger.debug("removeLogMessageHandler(logMessageHandler={})", logMessageHandler);
    logMessageHandlers.remove(logMessageHandler);
  }
  
  /**
   * 
   */
  public void start() {
    Logger.debug("start()");
    executor.scheduleAtFixedRate(new LogProcessor(), period, period, TimeUnit.MILLISECONDS);
  }
  
  /**
   * 
   */
  public void release() {
    Logger.debug("release()");
    executor.shutdownNow();
  }
  
  /**
   *
   */
  private final class LogProcessor implements Runnable {

//    @Override
    public void run() {
      Logger.trace("run()");
      
      int count = log.count();
      Logger.trace("count={}", count);

      // If there is at least one message and at least one message handler...
      if(count > 0 && !logMessageHandlers.isEmpty()) {
        // ...get the next batch of log messages
        List<LogMessage> logMessages = log.messages();
        // ...and send each message to each handler
        for(LogMessageHandler logMessageHandler : logMessageHandlers) {
          logMessageHandler.messages(logMessages);
        }
      }
    }
  }
}
