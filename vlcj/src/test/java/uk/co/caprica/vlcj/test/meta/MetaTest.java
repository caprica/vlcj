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

import uk.co.caprica.vlcj.player.MediaMetaType;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

/**
 * Simple test to show local file meta data.
 * <p>
 * Specify a single local media file as the first (and only) command-line
 * argument. 
 * <p>
 * An example of the output of a local mp3 file:
 * <pre>
 *        TITLE -> Find Yourself (Radio Edit)
 *       ARTIST -> John Ocallaghan Feat Sarah Howells
 *        GENRE -> Trance
 *    COPYRIGHT -> 
 *        ALBUM -> 40 Summer Trance Hits 2010
 *  TRACKNUMBER -> 16
 *  DESCRIPTION -> 
 *       RATING -> 
 *         DATE -> 2010
 *      SETTING -> 
 *          URL -> 
 *     LANGUAGE -> English
 *   NOWPLAYING -> 
 *    PUBLISHER -> 
 *    ENCODEDBY -> Lame 3.97
 *   ARTWORKURL -> 
 *      TRACKID -> 
 * </pre>
 */
public class MetaTest {

  public static void main(String[] args) {
    // Create a media player
    MediaPlayerFactory factory = new MediaPlayerFactory();
    MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();
    
    // You do not need to "play" the media, simply "preparing" it is enough
    // (so long as it exists)
    mediaPlayer.prepareMedia(args[0]);

    // Parse the media - this is the synchronous version, there is also a 
    // method to parse asynchronously and be notified via events
    mediaPlayer.parseMedia();
    
    // Dump out the enumerated values...
    for(MediaMetaType metaType : MediaMetaType.values()) {
      String val = mediaPlayer.getMeta(metaType);
      System.out.printf("%12s -> %s\n", metaType, val != null ? val : "");
    }
    
    // Orderly clean-up
    mediaPlayer.release();
    factory.release();
  }
}
