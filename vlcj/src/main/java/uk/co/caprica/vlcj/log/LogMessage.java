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
   * 
   */
  private final LogSeverity severity;
  
  /**
   * 
   */
  private final String type;
  
  /**
   * 
   */
  private final String name;
  
  /**
   * 
   */
  private final String header;
  
  /**
   * 
   */
  private final String message;
  
  /**
   * 
   * 
   * @param severity
   * @param type
   * @param name
   * @param header
   * @param message
   */
  public LogMessage(LogSeverity severity, String type, String name, String header, String message) {
    this.severity = severity;
    this.type = type;
    this.name = name;
    this.header = header;
    this.message = message;
  }
  
  /**
   * 
   * 
   * @return
   */
  public LogSeverity severity() {
    return severity;
  }
  
  /**
   * 
   * 
   * @return
   */
  public String type() {
    return type;
  }
  
  /**
   * 
   * 
   * @return
   */
  public String name() {
    return name;
  }
  
  /**
   * 
   * 
   * @return
   */
  public String header() {
    return header;
  }
  
  /**
   * 
   * 
   * @return
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
