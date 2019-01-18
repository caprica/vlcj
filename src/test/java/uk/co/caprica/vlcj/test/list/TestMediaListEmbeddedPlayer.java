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

package uk.co.caprica.vlcj.test.list;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.enums.PlaybackMode;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Example showing how to combine a media list player with an embedded media player.
 */
public class TestMediaListEmbeddedPlayer extends VlcjTest {

    public static void main(String[] args) throws Exception {
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

        Canvas canvas = new Canvas();
        canvas.setBackground(Color.black);
        VideoSurface videoSurface = mediaPlayerFactory.videoSurfaces().newVideoSurface(canvas);

        EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().setVideoSurface(videoSurface);

        MediaListPlayer mediaListPlayer = mediaPlayerFactory.mediaPlayers().newMediaListPlayer();

        mediaListPlayer.events().addMediaListPlayerEventListener(new MediaListPlayerEventAdapter() {
            @Override
            public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item) {
                System.out.println("nextItem()");
            }
        });

        mediaListPlayer.mediaPlayer().setMediaPlayer(mediaPlayer); // <--- Important, associate the media player with the media list player

        JPanel cp = new JPanel();
        cp.setBackground(Color.black);
        cp.setLayout(new BorderLayout());
        cp.add(canvas, BorderLayout.CENTER);

        JFrame f = new JFrame("vlcj embedded media list player test");
        f.setIconImage(new ImageIcon(TestMediaListEmbeddedPlayer.class.getResource("/icons/vlcj-logo.png")).getImage());
        f.setContentPane(cp);
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        MediaList mediaList = mediaPlayerFactory.media().newMediaList();
        mediaList.items().addMedia(mediaPlayerFactory.media().newMedia("/movies/1.mp4"));
        mediaList.items().addMedia(mediaPlayerFactory.media().newMedia("/movies/2.mp4"));
        mediaList.items().addMedia(mediaPlayerFactory.media().newMedia("/movies/3.mp4"));

        mediaListPlayer.list().setMediaList(mediaList);
        mediaListPlayer.mode().setMode(PlaybackMode.LOOP);

        mediaListPlayer.controls().play();

        // This looping is just for purposes of demonstration, ordinarily you would
        // not do this of course
        for(;;) {
            Thread.sleep(500);
            mediaPlayer.chapters().setChapter(3);

            Thread.sleep(5000);
            mediaListPlayer.controls().playNext();
        }

        // mediaList.release();
        // mediaListPlayer.release();
        // mediaPlayer.release();
        // mediaPlayerFactory.release();
    }
}
