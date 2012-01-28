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
 * Copyright 2009, 2010, 2011, 2012 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

import java.awt.image.BufferedImage;

/**
 * Specification for media meta data.
 */
public interface MediaMeta {

  String getTitle();

  String getArtist();

  String getGenre();

  String getCopyright();

  String getAlbum();

  String getTrackNumber();

  String getDescription();

  String getRating();

  String getDate();

  String getSetting();

  String getUrl();

  String getLanguage();

  String getNowPlaying();

  String getPublisher();

  String getEncodedby();

  String getArtworkUrl();

  String getTrackId();

  BufferedImage getArtwork();
}
