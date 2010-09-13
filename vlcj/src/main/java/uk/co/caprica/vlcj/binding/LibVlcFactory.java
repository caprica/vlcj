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

package uk.co.caprica.vlcj.binding;

import java.lang.reflect.Proxy;

/**
 * A factory that creates interfaces to the libvlc native library.
 */
public class LibVlcFactory {

  /**
   * True if the access to the native library should be synchronised.
   */
  private boolean synchronise;

  /**
   * True if the access to the native library should be logged.
   */
  private boolean log;
  
  /**
   * Private constructor prevents direct instantiation by others.
   */
  private LibVlcFactory() {
  }

  /**
   * Create a new factory instance
   * 
   * @return factory
   */
  public static LibVlcFactory factory() {
    return new LibVlcFactory();
  }
  
  /**
   * Request that the libvlc native library instance be synchronised.
   * 
   * @return factory
   */
  public LibVlcFactory synchronise() {
    this.synchronise = true;
    return this;
  }
  
  /**
   * Request that the libvlc native library instance be logged.
   * 
   * @return factory
   */
  public LibVlcFactory log() {
    this.log = true;
    return this;
  }
  
  /**
   * Create a new libvlc native library instance.
   * 
   * @return native library instance
   */
  public LibVlc create() {
    // Synchronised or not...
    LibVlc instance = synchronise ? LibVlc.SYNC_INSTANCE : LibVlc.INSTANCE;
    
    // Logged...
    if(log) {
      instance = (LibVlc)Proxy.newProxyInstance(LibVlc.class.getClassLoader(), new Class<?>[] {LibVlc.class}, new LoggingProxy(instance));
    }
    
    return instance;
  }
}
