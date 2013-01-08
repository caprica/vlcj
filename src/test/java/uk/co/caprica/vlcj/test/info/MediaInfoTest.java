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

package uk.co.caprica.vlcj.test.info;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * A test for the various media information functions.
 * <p>
 * For regular media files (like ".mpg" or ".avi") the track information is available after the
 * media has been parsed (or played).
 * <p>
 * For DVD media files (like ".iso" files) the track information is not available after the media
 * has been parsed, a video output must have been created, and even then the video track
 * width/height might not be available until a short time later.
 * <p>
 * In all cases, the other functions for title, video, audio and chapter descriptions require that a
 * video output has been created before they return valid information.
 */
public class MediaInfoTest extends VlcjTest {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Specify an MRL");
            System.exit(1);
        }

        MediaPlayerFactory factory = new MediaPlayerFactory();
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
                System.out.println("     Track Information: " + mediaPlayer.getTrackInfo());
                System.out.println("    Title Descriptions: " + mediaPlayer.getTitleDescriptions());
                System.out.println("    Video Descriptions: " + mediaPlayer.getVideoDescriptions());
                System.out.println("    Audio Descriptions: " + mediaPlayer.getAudioDescriptions());
                for(int i = 0; i < mediaPlayer.getTitleDescriptions().size(); i ++ ) {
                    System.out.println("Chapter Descriptions " + i + ": " + mediaPlayer.getChapterDescriptions(i));
                }
            }
        });

        mediaPlayer.prepareMedia(args[0]);

        mediaPlayer.parseMedia();

        mediaPlayer.start();

        try {
            Thread.sleep(3000);
        }
        catch(InterruptedException e) {
        }

        System.out.println("Track Information before end: " + mediaPlayer.getTrackInfo());

        mediaPlayer.stop();

        mediaPlayer.release();
        factory.release();
    }
}
