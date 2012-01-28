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
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * Representation of all available media meta data.
 */
class DefaultMediaMeta implements MediaMeta {

  private String title;
  private String artist;
  private String genre;
  private String copyright;
  private String album;
  private String trackNumber;
  private String description;
  private String rating;
  private String date;
  private String setting;
  private String url;
  private String language;
  private String nowPlaying;
  private String publisher;
  private String encodedBy;
  private String artworkUrl;
  private String trackId;

  private BufferedImage artwork;
  
  DefaultMediaMeta() {
  }
  
//  @Override
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

//  @Override
  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

//  @Override
  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

//  @Override
  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

//  @Override
  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

//  @Override
  public String getTrackNumber() {
    return trackNumber;
  }

  public void setTrackNumber(String trackNumber) {
    this.trackNumber = trackNumber;
  }

//  @Override
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

//  @Override
  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

//  @Override
  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

//  @Override
  public String getSetting() {
    return setting;
  }

  public void setSetting(String setting) {
    this.setting = setting;
  }

//  @Override
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

//  @Override
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

//  @Override
  public String getNowPlaying() {
    return nowPlaying;
  }

  public void setNowPlaying(String nowPlaying) {
    this.nowPlaying = nowPlaying;
  }

//  @Override
  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

//  @Override
  public String getEncodedby() {
    return encodedBy;
  }

  public void setEncodedBy(String encodedBy) {
    this.encodedBy = encodedBy;
  }

//  @Override
  public String getArtworkUrl() {
    return artworkUrl;
  }

  public void setArtworkUrl(String artworkUrl) {
    this.artworkUrl = artworkUrl;
  }

//  @Override
  public String getTrackId() {
    return trackId;
  }

  public void setTrackId(String trackId) {
    this.trackId = trackId;
  }

//  @Override
  public BufferedImage getArtwork() {
    if(artwork == null && artworkUrl != null) {
      try {
        artwork = ImageIO.read(new URL(artworkUrl));
      }
      catch(Exception e) {
        throw new RuntimeException("Failed to load artwork", e);
      }
    }
    return artwork;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(200);
    sb.append(getClass().getSimpleName()).append('[');
    sb.append("title=").append(title).append(',');
    sb.append("artist=").append(artist).append(',');
    sb.append("genre=").append(genre).append(',');
    sb.append("copyright=").append(copyright).append(',');
    sb.append("album=").append(album).append(',');
    sb.append("trackNumber=").append(trackNumber).append(',');
    sb.append("description=").append(description).append(',');
    sb.append("rating=").append(rating).append(',');
    sb.append("date=").append(date).append(',');
    sb.append("setting=").append(setting).append(',');
    sb.append("url=").append(url).append(',');
    sb.append("language=").append(language).append(',');
    sb.append("nowPlaying=").append(nowPlaying).append(',');
    sb.append("publisher=").append(publisher).append(',');
    sb.append("encodedBy=").append(encodedBy).append(',');
    sb.append("artworkUrl=").append(artworkUrl).append(',');
    sb.append("trackId=").append(trackId).append(']');
    return sb.toString();
  }
}