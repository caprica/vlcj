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

package uk.co.caprica.vlcj.test.media;

import uk.co.caprica.vlcj.binding.internal.*;
import uk.co.caprica.vlcj.enums.*;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.model.MediaSlave;
import uk.co.caprica.vlcj.model.TrackInfo;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.media.events.MediaEventAdapter;
import uk.co.caprica.vlcj.test.VlcjTest;

import java.util.List;

public class MediaTest extends VlcjTest {

    public static void main(String[] args) throws Exception {
        MediaPlayerFactory factory = new MediaPlayerFactory();

        MediaPlayer mediaPlayer = factory.mediaPlayers().newMediaPlayer();

        Media media = factory.media().newMedia("/home/mark/1.mp4");
//        Media media = factory.media().newMedia("/home/mark/1.mp3");
        System.out.println(media);

        System.out.println("media type " + media.info().type());
        System.out.println("media state " + media.info().state());

        boolean addedSlave;

        addedSlave = media.slaves().add(MediaSlaveType.SUBTITLE, MediaSlavePriority.HIGHEST, "file:///home/mark/test.srt");
        System.out.println("Added slave " + addedSlave);

        addedSlave = media.slaves().add(MediaSlaveType.AUDIO, MediaSlavePriority.HIGHEST, "file:///home/mark/test.mp3");
        System.out.println("Added slave " + addedSlave);

        List<MediaSlave> slaves = media.slaves().get();
        for (MediaSlave slave : slaves) {
            System.out.println(slave);
        }

        media.events().addMediaEventListener(new MediaEventAdapter() {
            @Override
            public void mediaMetaChanged(Media media, Meta metaType) {
                System.out.printf("meta changed: %s%n", metaType);
            }

            @Override
            public void mediaSubItemAdded(Media media, libvlc_media_t subItem) {
                System.out.printf("sub item added%n");
            }

            @Override
            public void mediaDurationChanged(Media media, long newDuration) {
                System.out.printf("duration change: %d%n", newDuration);
            }

            @Override
            public void mediaParsedChanged(Media media, MediaParsedStatus newStatus) {
                System.out.printf("parsed changed: %s%n", newStatus);

                if (newStatus == MediaParsedStatus.DONE) {
                    for (Meta t : Meta.values()) {
                        String meta = media.meta().get(t);
                        if (meta != null) {
                            System.out.printf("%-26s - %s%n", t, meta);
                        }
                    }
                }

                // Slaves don't appear in this list?
                for (TrackInfo trackInfo : media.info().tracks()) {
                    System.out.println("Track: " + trackInfo);
                }
            }

            @Override
            public void mediaFreed(Media media) {
                System.out.printf("freed%n");
            }

            @Override
            public void mediaStateChanged(Media media, State newState) {
                System.out.printf("state changed: %s%n", newState);
            }

            @Override
            public void mediaSubItemTreeAdded(Media media, libvlc_media_t item) {
                System.out.printf("sub item tree added%n");
            }
        });

        boolean result;

        result = media.parsing().parse();
        System.out.println("parse result is " + result);

        // Just to let the initial parsing finish, it's not necessary for normal use play will kick off a parse if the
        // media is not yet parsed (this test is being a bit lazy, we really should use events and synchronise when
        // parse completes
        Thread.sleep(3000);

        mediaPlayer.media().set(media);

        System.out.println("AFTER SET...");

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void mediaPlayerReady(MediaPlayer mediaPlayer) {
                System.out.println(mediaPlayer.audio().getAudioDescriptions());
                // This is a track ID, it is not an index or number, the ID comes from the return audio descriptions
                mediaPlayer.audio().setAudioTrack(3);
            }
        });

        mediaPlayer.controls().play();

        Thread.currentThread().join();
    }

}
