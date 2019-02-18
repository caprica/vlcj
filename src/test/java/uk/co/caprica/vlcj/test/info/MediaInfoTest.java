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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.info;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
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
        final MediaPlayer mediaPlayer = factory.mediaPlayers().newMediaPlayer();

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void mediaPlayerReady(MediaPlayer mediaPlayer) {
                System.out.println("MEDIA PLAYER READY...");
                System.out.println("     Track Information: " + mediaPlayer.media().info().tracks());
                System.out.println("    Title Descriptions: " + mediaPlayer.titles().titleDescriptions());
                System.out.println("    Video Descriptions: " + mediaPlayer.video().trackDescriptions());
                System.out.println("    Audio Descriptions: " + mediaPlayer.audio().trackDescriptions());
                System.out.println("Chapter Descriptions: " + mediaPlayer.chapters().allDescriptions());
                System.out.println();
            }
        });

        mediaPlayer.media().prepare(args[0]);
        mediaPlayer.controls().start();

        try {
            Thread.sleep(5000);
        }
        catch(InterruptedException e) {
        }

//        System.out.println("Track Information before end: " + mediaPlayer.getTrackInfo());
//
//        System.out.println("    UNKNOWN: " +  mediaPlayer.getTrackInfo(TrackType.UNKNOWN));
//        System.out.println("      AUDIO: " +  mediaPlayer.getTrackInfo(TrackType.AUDIO));
//        System.out.println("      VIDEO: " +  mediaPlayer.getTrackInfo(TrackType.VIDEO));
//        System.out.println("       TEXT: " +  mediaPlayer.getTrackInfo(TrackType.TEXT));
//        System.out.println("AUDIO+VIDEO: " +  mediaPlayer.getTrackInfo(TrackType.AUDIO, TrackType.VIDEO));

        mediaPlayer.controls().stop();

        mediaPlayer.release();
        factory.release();
    }
}
