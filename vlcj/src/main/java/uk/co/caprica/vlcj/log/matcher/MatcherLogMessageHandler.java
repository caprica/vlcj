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

package uk.co.caprica.vlcj.log.matcher;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.caprica.vlcj.log.LogMessage;
import uk.co.caprica.vlcj.log.LogMessageHandler;

/**
 * Implementation of a log message handler that uses a regular expression to
 * identify a particular pattern in a log message.
 */
public class MatcherLogMessageHandler implements LogMessageHandler {

  /**
   * Compiled pattern to apply to each log message.
   */
  private final Pattern pattern;
  
  /**
   * Call-back to invoke when a matching message is found.
   */
  private final MatcherCallback callback;
  
  /**
   * Create a new log message handler.
   * 
   * @param expression regular expression
   * @param callback call-back to invoke with matching messages
   */
  public MatcherLogMessageHandler(String expression, MatcherCallback callback) {
    this.pattern = Pattern.compile(expression);
    this.callback = callback;
  }
  
  @Override
  public void messages(List<LogMessage> messages) {
    // Create a matcher for the expression
    Matcher matcher = pattern.matcher("");      
    // For each log message...
    for(LogMessage message : messages) {
      // ...get the message text...
      String text = message.message();
      // ...and see if it matches
      matcher.reset(text);
      if(matcher.matches()) {
        callback.matched(matcher);
      }
    }
  }
}
