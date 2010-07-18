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

package uk.co.caprica.vlcj.experimental;

import com.sun.jna.Memory;

/**
 * Specification for a component that wishes to be called back to process video
 * frames.
 * <p>
 * <strong>This class is experimental and is subject to change.</strong>
 */
public interface RenderCallback {

  /**
   * Call-back to allocate video memory.
   */
  public void lock();

  /**
   * Call-back to release video memory.
   */
  public void unlock();

  /**
   * Call-back when ready to display a video frame.
   * 
   * @param buffer video data
   */
  public void display(Memory buffer);
}
