package uk.co.caprica.vlcj;

import java.awt.Dimension;

/**
 * Basic video meta data.
 */
public class VideoMetaData {

  /**
   * Video width/height.
   */
  private Dimension videoDimension;
  
  public Dimension getVideoDimension() {
    return videoDimension;
  }

  public void setVideoDimension(Dimension videoDimension) {
    this.videoDimension = videoDimension;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder(20);
    sb.append("videoDimension=").append(videoDimension);
    return sb.toString();
  }
}
