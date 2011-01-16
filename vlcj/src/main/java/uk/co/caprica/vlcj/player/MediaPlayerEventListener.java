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

/**
 * Specification for a component that is interested in receiving event
 * notifications from the media player.
 */
public interface MediaPlayerEventListener {

  /**
   * 
   * 
   * @param mediaPlayer
   */
  void mediaChanged(MediaPlayer mediaPlayer);

  /**
   * 
   * 
   * @param mediaPlayer
   */
  void opening(MediaPlayer mediaPlayer);

  /**
   * 
   * 
   * @param mediaPlayer
   */
  void buffering(MediaPlayer mediaPlayer);

  /**
   * 
   * 
   * @param mediaPlayer
   */
  void playing(MediaPlayer mediaPlayer);

  /**
   * 
   * 
   * @param mediaPlayer
   */
  void paused(MediaPlayer mediaPlayer);

  /**
   * 
   * 
   * @param mediaPlayer
   */
  void stopped(MediaPlayer mediaPlayer);

  /**
   * 
   * 
   * @param mediaPlayer
   */
  void forward(MediaPlayer mediaPlayer);

  /**
   * 
   * 
   * @param mediaPlayer
   */
  void backward(MediaPlayer mediaPlayer);

  /**
   * 
   * 
   * @param mediaPlayer
   */
  void finished(MediaPlayer mediaPlayer);
  
  /**
   * 
   * 
   * @param mediaPlayer
   * @param newTime
   */
  void timeChanged(MediaPlayer mediaPlayer, long newTime);

  /**
   * 
   * 
   * @param mediaPlayer
   * @param newPosition
   */
  void positionChanged(MediaPlayer mediaPlayer, float newPosition);

  /**
   * 
   * 
   * @param mediaPlayer
   * @param newSeekable
   */
  void seekableChanged(MediaPlayer mediaPlayer, int newSeekable);

  /**
   * 
   * 
   * @param mediaPlayer
   * @param newSeekable
   */
  void pausableChanged(MediaPlayer mediaPlayer, int newSeekable);

  /**
   * 
   * 
   * @param mediaPlayer
   * @param newSeekable
   */
  void titleChanged(MediaPlayer mediaPlayer, int newSeekable);

  /**
   * 
   * 
   * @param mediaPlayer
   * @param filename
   */
  void snapshotTaken(MediaPlayer mediaPlayer, String filename);

  /**
   * 
   * 
   * @param mediaPlayer
   * @param newLength
   */
  void lengthChanged(MediaPlayer mediaPlayer, long newLength);

  /**
   * 
   * 
   * @param mediaPlayer
   */
  void error(MediaPlayer mediaPlayer);

  /**
   * 
   * 
   * @param mediaPlayer
   * @param videoMetaData
   */
  void metaDataAvailable(MediaPlayer mediaPlayer, VideoMetaData videoMetaData);
}
