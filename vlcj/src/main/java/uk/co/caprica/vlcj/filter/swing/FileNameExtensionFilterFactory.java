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

package uk.co.caprica.vlcj.filter.swing;

import java.util.Arrays;

import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.filter.AudioFileFilter;
import uk.co.caprica.vlcj.filter.PlayListFileFilter;
import uk.co.caprica.vlcj.filter.SubTitleFileFilter;
import uk.co.caprica.vlcj.filter.VideoFileFilter;

/**
 * A factory that creates new instances of a {@link javax.swing.filechooser.FileNameExtensionFilter FileNameExtensionFilter}
 * configured with the recognised vlc file-types.
 * <p>
 * Use this factory to help initialise a {@link javax.swing.JFileChooser}, for example:
 * <pre>
 *  fileChooser = new JFileChooser();
 *  fileChooser.setApproveButtonText("Play");
 *  fileChooser.addChoosableFileFilter(FileNameExtensionFilterFactory.newVideoFileNameExtensionFilter());
 *  fileChooser.addChoosableFileFilter(FileNameExtensionFilterFactory.newAudioFileNameExtensionFilter());
 *  fileChooser.addChoosableFileFilter(FileNameExtensionFilterFactory.newPlayListFileFilter());
 *  fileChooser.addChoosableFileFilter(FileNameExtensionFilterFactory.newMediaFileNameExtensionFilter());
 * </pre>
 * 
 * @see javax.swing.filechooser.FileNameExtensionFilter
 * @see javax.swing.JFileChooser
 */
public class FileNameExtensionFilterFactory {

  /**
   * Description for the video file filter.
   */
  private static final String VIDEO_FILTER_DESCRIPTION = "Video Files";
  
  /**
   * Description for the audio file filter.
   */
  private static final String AUDIO_FILTER_DESCRIPTION = "Audio Files";
  
  /**
   * Description for the play-list file filter.
   */
  private static final String PLAYLIST_FILTER_DESCRIPTION = "Playlist Files";
  
  /**
   * Description for the media file filter.
   */
  private static final String MEDIA_FILTER_DESCRIPTION = "Media Files";

  /**
   * Description for the sub-title file filter.
   */
  private static final String SUBTITLE_FILTER_DESCRIPTION = "Subtitle Files";
  
  /**
   * Create a new file name extension filter that accepts video files.
   * 
   * @return filter
   */
  public static FileNameExtensionFilter newVideoFileNameExtensionFilter() {
    return new FileNameExtensionFilter(VIDEO_FILTER_DESCRIPTION, VideoFileFilter.INSTANCE.getExtensions());
  }
  
  /**
   * Create a new file name extension filter that accepts audio files.
   * 
   * @return filter
   */
  public static FileNameExtensionFilter newAudioFileNameExtensionFilter() {
    return new FileNameExtensionFilter(AUDIO_FILTER_DESCRIPTION, AudioFileFilter.INSTANCE.getExtensions());
  }
  
  /**
   * Create a new file name extension filter that accepts play-list files.
   * 
   * @return filter
   */
  public static FileNameExtensionFilter newPlayListFileFilter() {
    return new FileNameExtensionFilter(PLAYLIST_FILTER_DESCRIPTION, PlayListFileFilter.INSTANCE.getExtensions());
  }
  
  /**
   * Create a new file name extension filter that accepts all recognised media 
   * files.
   * <p>
   * A media file is one of:
   * <ul>
   *   <li>Video</li>
   *   <li>Audio</li>
   *   <li>Play-list</li>
   * </ul>
   * 
   * @return filter
   */
  public static FileNameExtensionFilter newMediaFileNameExtensionFilter() {
    return new FileNameExtensionFilter(MEDIA_FILTER_DESCRIPTION, 
      add(
        add(VideoFileFilter.INSTANCE.getExtensions(), AudioFileFilter.INSTANCE.getExtensions()),
        PlayListFileFilter.INSTANCE.getExtensions())
      );
  }

  /**
   * Create a new file name extension filter that accepts sub-title files.
   * 
   * @return filter
   */
  public static FileNameExtensionFilter newSubtitleFileFilter() {
    return new FileNameExtensionFilter(SUBTITLE_FILTER_DESCRIPTION, SubTitleFileFilter.INSTANCE.getExtensions());
  }
  
  /**
   * Join two arrays together.
   * 
   * @param first first array
   * @param second second array
   * @return joined array
   */
  private static String[] add(String[] first, String[] second) {
//    String[] result = Arrays.copyOf(first, first.length + second.length);
//    System.arraycopy(second, 0, result, first.length, second.length);
    // Maintain JDK 1.5 compatibility
    String[] result = new String[first.length + second.length];
    System.arraycopy(first, 0, result, 0, first.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }
}
