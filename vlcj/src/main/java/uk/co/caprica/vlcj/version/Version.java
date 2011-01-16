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

package uk.co.caprica.vlcj.version;

/**
 * Encapsulation of version information and related behaviours.
 * <p>
 * This may be useful to implement version-specific features. 
 */
public final class Version implements Comparable<Version> {

  /**
   * Raw version information.
   */
  private final String version;
  
  /**
   * Major version number.
   */
  private final int major;
  
  /**
   * Minor version number.
   */
  private final int minor;
  
  /**
   * Revision number.
   */
  private final int revision;

  /**
   * Create a new version.
   * 
   * @param version version string
   */
  public Version(final String version) {
    this.version = version;
    
    String[] parts = version.split("[-\\s]")[0].split("\\.");
    this.major = Integer.parseInt(parts[0]);
    this.minor = Integer.parseInt(parts[1]);
    this.revision = Integer.parseInt(parts[2]);
  }

  /**
   * Get the original version string.
   * 
   * @return version
   */
  public String version() {
    return version;
  }
 
  /**
   * Get the major version.
   * 
   * @return major version number
   */
  public int major() {
    return major;
  }
  
  /**
   * Get the minor version.
   * 
   * @return minor version number
   */
  public int minor() {
    return minor;
  }
  
  /**
   * Get the revision.
   * 
   * @return revision number
   */
  public int revision() {
    return revision;
  }
 
//  @Override
  public int compareTo(Version o) {
    if(major == o.major) {
      if(minor == o.minor) {
        if(revision == o.revision) {
          return 0;
        }
        else {
          return revision - o.revision;
        }
      }
      else {
        return minor - o.minor;
      }
    }
    else {
      return major - o.major;
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(20);
    sb.append(version);
    return sb.toString();
  }
}