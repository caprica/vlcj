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

import java.util.regex.Matcher;

/**
 * Specification for a component to be notified when text matching a
 * pattern is detected in the log.
 */
public interface MatcherCallback {

  /**
   * Call-back method invoked when a match is found in the log.
   * 
   * @param matcher matcher, from which groups may be extracted
   */
  void matched(Matcher matcher);
}
