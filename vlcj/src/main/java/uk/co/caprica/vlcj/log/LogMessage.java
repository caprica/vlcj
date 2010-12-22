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

/**
 * Representation of a native log message.
 */
public class LogMessage {
  
  /**
   * Log severity level.
   */
  private final LogLevel severity;
  
  /**
   * Type of message.
   */
  private final String type;
  
  /**
   * Module name.
   */
  private final String name;
  
  /**
   * Optional header.
   */
  private final String header;
  
  /**
   * Message text.
   */
  private final String message;
  
  /**
   * Create a new log message.
   * 
   * @param severity severity level
   * @param type type of message
   * @param name module name
   * @param header optional message header
   * @param message message text
   */
  public LogMessage(LogLevel severity, String type, String name, String header, String message) {
    this.severity = severity;
    this.type = type;
    this.name = name;
    this.header = header;
    this.message = message;
  }
  
  /**
   * Get the log message severity level.
   * 
   * @return severity level
   */
  public LogLevel severity() {
    return severity;
  }
  
  /**
   * Get the log message type.
   * 
   * @return message type
   */
  public String type() {
    return type;
  }
  
  /**
   * Get the name of the module that generated the log message.
   * 
   * @return module name
   */
  public String name() {
    return name;
  }
  
  /**
   * Get the (optional) message header.
   * 
   * @return message header, may be <code>null</code>
   */
  public String header() {
    return header;
  }
  
  /**
   * Get the message text.
   * 
   * @return text
   */
  public String message() {
    return message;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(100);
    sb.append('[');
    sb.append("severity=").append(severity).append(',');
    sb.append("type=").append(type).append(',');
    sb.append("name=").append(name).append(',');
    sb.append("header=").append(header).append(',');
    sb.append("message=").append(message).append(']');
    return sb.toString();
  }
}
