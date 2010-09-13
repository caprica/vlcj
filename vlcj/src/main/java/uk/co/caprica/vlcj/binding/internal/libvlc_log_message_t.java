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

package uk.co.caprica.vlcj.binding.internal;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 *
 */
public class libvlc_log_message_t extends Structure {

  public int    sizeof_msg;   /* sizeof() of message structure, must be filled in by user */
  public int    i_severity;   /* 0=INFO, 1=ERR, 2=WARN, 3=DBG */
  public String psz_type;     /* module type */
  public String psz_name;     /* module name */
  public String psz_header;   /* optional header */
  public String psz_message;  /* message */
  
  public libvlc_log_message_t() {
    this.sizeof_msg = 
      4            + // int
      4            + // int
      Pointer.SIZE + // char*
      Pointer.SIZE + // char*
      Pointer.SIZE + // char*
      Pointer.SIZE;  // char*
  }
}
