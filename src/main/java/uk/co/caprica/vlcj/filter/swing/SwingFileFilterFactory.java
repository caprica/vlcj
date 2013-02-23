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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.filter.swing;

import javax.swing.filechooser.FileFilter;

import uk.co.caprica.vlcj.filter.AudioFileFilter;
import uk.co.caprica.vlcj.filter.MediaFileFilter;
import uk.co.caprica.vlcj.filter.PlayListFileFilter;
import uk.co.caprica.vlcj.filter.SubTitleFileFilter;
import uk.co.caprica.vlcj.filter.VideoFileFilter;

/**
 * A factory that creates new instances of a {@link javax.swing.filechooser.FileFilter FileFilter}
 * configured with the recognised vlc file-types.
 * <p>
 * Use this factory to help initialise a {@link javax.swing.JFileChooser}, for example:
 *
 * <pre>
 * fileChooser = new JFileChooser();
 * fileChooser.setApproveButtonText(&quot;Play&quot;);
 * fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newVideoFileFilter());
 * fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newAudioFileFilter());
 * fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newPlayListFileFilter());
 * fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newMediaFileFilter());
 * fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newSubTitleFileFilter());
 * </pre>
 *
 * @see javax.swing.filechooser.FileNameExtensionFilter
 * @see javax.swing.JFileChooser
 */
public final class SwingFileFilterFactory {

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
     * Prevent direct instantiation by others.
     */
    private SwingFileFilterFactory() {
    }

    /**
     * Create a new file name extension filter that accepts video files.
     *
     * @return filter
     */
    public static FileFilter newVideoFileFilter() {
        return new SwingFileFilter(VIDEO_FILTER_DESCRIPTION, new VideoFileFilter());
    }

    /**
     * Create a new file name extension filter that accepts audio files.
     *
     * @return filter
     */
    public static FileFilter newAudioFileFilter() {
        return new SwingFileFilter(AUDIO_FILTER_DESCRIPTION, new AudioFileFilter());
    }

    /**
     * Create a new file name extension filter that accepts play-list files.
     *
     * @return filter
     */
    public static FileFilter newPlayListFileFilter() {
        return new SwingFileFilter(PLAYLIST_FILTER_DESCRIPTION, new PlayListFileFilter());
    }

    /**
     * Create a new file name extension filter that accepts all recognised media files.
     * <p>
     * A media file is one of:
     * <ul>
     * <li>Video</li>
     * <li>Audio</li>
     * <li>Play-list</li>
     * </ul>
     *
     * @return filter
     */
    public static FileFilter newMediaFileFilter() {
        return new SwingFileFilter(MEDIA_FILTER_DESCRIPTION, new MediaFileFilter());
    }

    /**
     * Create a new file name extension filter that accepts sub-title files.
     *
     * @return filter
     */
    public static FileFilter newSubtitleFileFilter() {
        return new SwingFileFilter(SUBTITLE_FILTER_DESCRIPTION, new SubTitleFileFilter());
    }
}
