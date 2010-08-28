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

package uk.co.caprica.vlcj.player;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic video meta data.
 */
public class VideoMetaData {

  /**
   * Video width/height.
   */
  private Dimension videoDimension;
  
  /**
   * 
   */
  private int titleCount;
  
  /**
   * 
   */
  private int videoTrackCount;
  
  /**
   * 
   */
  private int audioTrackCount;
  
  /**
   * Number of sub-picture/sub-titles.
   */
  private int spuCount;
  
  /**
   * 
   */
  private List<String> titleDescriptions;
  
  /**
   * 
   */
  private List<String> videoDescriptions;
  
  /**
   * 
   */
  private List<String> audioDescriptions;
  
  /**
   * 
   */
  private List<String> spuDescriptions;
  
  /**
   * 
   */
  private Map<Integer, List<String>> chapterDescriptions = new HashMap<Integer, List<String>>();
  
  public Dimension getVideoDimension() {
    return videoDimension;
  }

  public void setVideoDimension(Dimension videoDimension) {
    this.videoDimension = videoDimension;
  }
  
  public int getTitleCount() {
    return titleCount;
  }

  public void setTitleCount(int titleCount) {
    this.titleCount = titleCount;
  }

  public int getVideoTrackCount() {
    return videoTrackCount;
  }

  public void setVideoTrackCount(int videoTrackCount) {
    this.videoTrackCount = videoTrackCount;
  }

  public int getAudioTrackCount() {
    return audioTrackCount;
  }

  public void setAudioTrackCount(int audioTrackCount) {
    this.audioTrackCount = audioTrackCount;
  }

  public int getSpuCount() {
    return spuCount;
  }

  public void setSpuCount(int spuCount) {
    this.spuCount = spuCount;
  }

  public List<String> getTitleDescriptions() {
    return titleDescriptions;
  }

  public void setTitleDescriptions(List<String> titleDescriptions) {
    this.titleDescriptions = titleDescriptions;
  }

  public List<String> getVideoDescriptions() {
    return videoDescriptions;
  }

  public void setVideoDescriptions(List<String> videoDescriptions) {
    this.videoDescriptions = videoDescriptions;
  }

  public List<String> getAudioDescriptions() {
    return audioDescriptions;
  }

  public void setAudioDescriptions(List<String> audioDescriptions) {
    this.audioDescriptions = audioDescriptions;
  }

  public List<String> getSpuDescriptions() {
    return spuDescriptions;
  }

  public void setSpuDescriptions(List<String> spuDescriptions) {
    this.spuDescriptions = spuDescriptions;
  }

  public Map<Integer, List<String>> getChapterDescriptions() {
    return chapterDescriptions;
  }

  public void setChapterDescriptions(Map<Integer, List<String>> chapterDescriptions) {
    this.chapterDescriptions = chapterDescriptions;
  }
  
//  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(200);
    sb.append(getClass().getSimpleName()).append('[');
    sb.append("videoDimension=").append(videoDimension).append(',');
    sb.append("titleCount=").append(titleCount).append(',');
    sb.append("videoTrackCount=").append(videoTrackCount).append(',');
    sb.append("audioTrackCount=").append(audioTrackCount).append(',');
    sb.append("spuCount=").append(spuCount).append(',');
    sb.append("titleDescriptions=").append(titleDescriptions).append(',');
    sb.append("videoDescriptions=").append(videoDescriptions).append(',');
    sb.append("audioDescriptions=").append(audioDescriptions).append(',');
    sb.append("spuDescriptions=").append(spuDescriptions).append(',');
    sb.append("chapterDescriptions=").append(chapterDescriptions).append(']');
    return sb.toString();
  }
}
