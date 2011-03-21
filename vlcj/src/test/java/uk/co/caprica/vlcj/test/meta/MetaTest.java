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
 * Copyright 2009, 2010, 2011 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.meta;

import java.awt.image.BufferedImage;

import uk.co.caprica.vlcj.log.Logger;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple test to show local file meta data.
 * <p>
 * Specify a single local media file as the first (and only) command-line
 * argument. 
 * <p>
 * An interesting feature of vlc is that if the media contains embedded art-
 * work, the ARTWORKURL meta data field will point to a valid local file for 
 * the extracted art-work.  
 */
public class MetaTest extends VlcjTest {

  public static void main(String[] args) {
    if(args.length != 1) {
      System.out.println("Specify a single MRL");
      System.exit(1);
    }

    Logger.setLevel(Logger.Level.INFO);
    
    // Create a media player
    MediaPlayerFactory factory = new MediaPlayerFactory();
    MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();
    
    // You do not need to "play" the media, simply "preparing" it is enough
    // (so long as it exists)
    mediaPlayer.prepareMedia(args[0]);

    // Parse the media - this is the synchronous version, there is also a 
    // method to parse asynchronously and be notified via events
    mediaPlayer.parseMedia();
    
    // Get the meta data and dump it out
    MediaMeta mediaMeta = mediaPlayer.getMediaMeta();
    System.out.println(mediaMeta);
    
    // Load the artwork into a buffered image (if available)
    BufferedImage artwork = mediaMeta.getArtwork();
    System.out.println(artwork);
    
    // Orderly clean-up
    mediaPlayer.release();
    factory.release();
  }
}
