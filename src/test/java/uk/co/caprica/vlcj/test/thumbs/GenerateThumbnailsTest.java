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
 * Copyright 2009-2020 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.thumbs;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.Picture;
import uk.co.caprica.vlcj.media.PictureType;
import uk.co.caprica.vlcj.media.ThumbnailRequest;
import uk.co.caprica.vlcj.media.ThumbnailerSeekSpeed;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;

public class GenerateThumbnailsTest extends VlcjTest {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Specify an MRL");
            System.exit(1);
        }

        String mrl = args[0];

        MediaPlayerFactory factory = new MediaPlayerFactory();
        MediaPlayer mediaPlayer = factory.mediaPlayers().newMediaPlayer();

        final CountDownLatch thumbnailGenerated = new CountDownLatch(1);

        mediaPlayer.events().addMediaEventListener(new MediaEventAdapter() {
            @Override
            public void mediaThumbnailGenerated(Media media, Picture picture) {
                System.out.println("mediaThumbnailGenerated");
                try {
                    FileOutputStream out = new FileOutputStream("thumb.png");
                    out.write(picture.buffer());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                thumbnailGenerated.countDown();
            }
        });

        if (mediaPlayer.media().start(mrl)) {
            Media media = null;
            ThumbnailRequest thumbnailRequest = null;
            try {
                media = mediaPlayer.media().newMedia();
                thumbnailRequest = media.thumbnails().requestByPosition(
                    0.5f,
                    ThumbnailerSeekSpeed.FAST,
                    800,
                    600,
                    false,
                    PictureType.PNG,
                    5000L
                );
                thumbnailGenerated.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (thumbnailRequest != null) {
                    thumbnailRequest.release();
                }
                if (media != null) {
                    media.release();
                }
            }

            mediaPlayer.controls().stop();
        }

        mediaPlayer.release();
        factory.release();

    }

}
