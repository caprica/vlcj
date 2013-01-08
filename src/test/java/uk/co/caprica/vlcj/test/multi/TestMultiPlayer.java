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

package uk.co.caprica.vlcj.test.multi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Example multi-instance player.
 * <p>
 * This approach to multi-instance players is not really recommended due to potential
 * concurrency/re-entrancy problems with the long list of native libraries that vlc uses. The more
 * player instances you have in one application, the more likely you are to see fatal crashes in
 * those native libraries.
 * <p>
 * Also, a current limitation of vlc is that all in-process media players will share the same audio
 * controls - there is no independent audio control available.
 * <p>
 * Out of process media players are reliable, but a lot more difficult and intricate to implement.
 */
public class TestMultiPlayer extends VlcjTest {

    private final String[] medias = {
        "wibble1.mp4",
        "wibble2.mp4"
        // Your MRL's go here
    };

    private final int rows = 1;
    private final int cols = 4;

    private final Frame mainFrame;

    private final List<PlayerInstance> players = new ArrayList<PlayerInstance>();

    private final MediaPlayerFactory factory;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TestMultiPlayer().start();
            }
        });
    }

    public TestMultiPlayer() {
        JPanel contentPane = new JPanel();
        contentPane.setBackground(Color.black);
        contentPane.setLayout(new GridLayout(rows, cols, 16, 16));
        contentPane.setBorder(new EmptyBorder(16, 16, 16, 16));

        mainFrame = new Frame("VLCJ Test Multi Player for VLC 1.2.x");
        mainFrame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setBackground(Color.black);
        mainFrame.add(contentPane, BorderLayout.CENTER);
        mainFrame.setBounds(100, 100, 1600, 300);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                for(PlayerInstance pi : players) {
                    pi.mediaPlayer().release();
                }
                factory.release();
                System.exit(0);
            }
        });

        mainFrame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                for(int i = 0; i < players.size(); i ++ ) {
                    players.get(i).mediaPlayer().pause();
                }
            }
        });

        factory = new MediaPlayerFactory("--no-video-title-show");

        FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(mainFrame);

        for(int i = 0; i < medias.length; i ++ ) {
            EmbeddedMediaPlayer player = factory.newEmbeddedMediaPlayer(fullScreenStrategy);
            PlayerInstance playerInstance = new PlayerInstance(player);
            players.add(playerInstance);

            JPanel playerPanel = new JPanel();
            playerPanel.setLayout(new BorderLayout());
            playerPanel.setBorder(new LineBorder(Color.white, 2));
            playerPanel.add(playerInstance.videoSurface());

            contentPane.add(playerPanel);
        }

        mainFrame.setVisible(true);
    }

    private void start() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < medias.length; i ++ ) {
                    players.get(i).mediaPlayer().setVideoSurface(factory.newVideoSurface(players.get(i).videoSurface()));
                    players.get(i).mediaPlayer().prepareMedia(medias[i]);
                }

                // There is a race condition somewhere when invoking libvlc_media_player_play()
                // multiple times in quick succession that causes a hard-failure and a fatal
                // VM crash.
                //
                // This is _not_ about _concurrently_ calling play multiple times, but the
                // native play function call must be off-loading something to a separate
                // thread and returning - then a subsequent call to play somehow interferes
                // with that or fails because of that.
                //
                // When libvlc_media_player_play() is called, the video playback is kicked
                // off asynchronously - so the API call will return before the video has
                // started playing. If we invoke play and then wait (making this effectively
                // a synchronous call) for the player to start playing, the hard VM crash
                // does not occur
                for(int i = 0; i < medias.length; i ++ ) {
                    EmbeddedMediaPlayer mediaPlayer = players.get(i).mediaPlayer();
                    mediaPlayer.start();
                }
            }
        });
    }
}
