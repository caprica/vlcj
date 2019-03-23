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

package uk.co.caprica.vlcj.test.basic;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.Equalizer;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.fullscreen.exclusivemode.ExclusiveModeFullScreenStrategy;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple test harness creates an AWT Window and plays a video.
 * <p>
 * This is <strong>very</strong> basic but should give you an idea of how to build a media player.
 * <p>
 * In case you didn't realise, you can press F12 to toggle the visibility of the player controls.
 */
public class TestPlayer extends VlcjTest {

    private static TestPlayer app;

    private final JFrame mainFrame;
    private final Canvas videoSurface;
    private final JPanel controlsPanel;
    private final JPanel videoAdjustPanel;

    private final JFrame equalizerFrame;

    private MediaPlayerFactory mediaPlayerFactory;

    private EmbeddedMediaPlayer mediaPlayer;

    private Equalizer equalizer;

    public static void main(final String[] args) throws Exception {
        setLookAndFeel();
        app = new TestPlayer();
    }

    public TestPlayer() {
        videoSurface = new Canvas();

        videoSurface.setBackground(Color.black);
        videoSurface.setSize(800, 600); // Only for initial layout

        // Since we're mixing lightweight Swing components and heavyweight AWT components this is probably a good idea
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        List<String> vlcArgs = new ArrayList<String>();

        vlcArgs.add("--no-snapshot-preview");
        vlcArgs.add("--quiet");

        mainFrame = new JFrame("VLCJ Test Player");
        mainFrame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());

        FullScreenStrategy fullScreenStrategy = new ExclusiveModeFullScreenStrategy(mainFrame);

        mediaPlayerFactory = new MediaPlayerFactory(vlcArgs.toArray(new String[vlcArgs.size()]));
        mediaPlayerFactory.application().setUserAgent("vlcj test player");

        mediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.fullScreen().strategy(fullScreenStrategy);
        mediaPlayer.videoSurface().set(mediaPlayerFactory.videoSurfaces().newVideoSurface(videoSurface));

        mediaPlayer.input().enableKeyInputHandling(false);
        mediaPlayer.input().enableMouseInputHandling(false);

        mediaPlayer.controls().setRepeat(true);

        controlsPanel = new PlayerControlsPanel(mediaPlayerFactory, mediaPlayer);
        videoAdjustPanel = new PlayerVideoAdjustPanel(mediaPlayer);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.setBackground(Color.black);
        mainFrame.add(videoSurface, BorderLayout.CENTER);
        mainFrame.add(controlsPanel, BorderLayout.SOUTH);
        mainFrame.add(videoAdjustPanel, BorderLayout.EAST);
        mainFrame.setJMenuBar(buildMenuBar());
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                if (mediaPlayerFactory != null) {
                    mediaPlayerFactory.release();
                    mediaPlayerFactory = null;
                }
            }
        });

        equalizer = mediaPlayerFactory.equalizer().newEqualizer();
        equalizerFrame = new EqualizerFrame(mediaPlayerFactory.equalizer().bands(), mediaPlayerFactory.equalizer().presets(), mediaPlayerFactory, mediaPlayer, equalizer);

        // Global AWT key handler, you're better off using Swing's InputMap and ActionMap with a JFrame - that would
        // solve all sorts of focus issues too
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event instanceof KeyEvent) {
                    KeyEvent keyEvent = (KeyEvent)event;
                    if (keyEvent.getID() == KeyEvent.KEY_PRESSED) {
                        if (keyEvent.getKeyCode() == KeyEvent.VK_F12) {
                            controlsPanel.setVisible(!controlsPanel.isVisible());
                            videoAdjustPanel.setVisible(!videoAdjustPanel.isVisible());
                            mainFrame.getJMenuBar().setVisible(!mainFrame.getJMenuBar().isVisible());
                            mainFrame.invalidate();
                            mainFrame.validate();
                        } else if (keyEvent.getKeyCode() == KeyEvent.VK_A) {
                            mediaPlayer.audio().setDelay(mediaPlayer.audio().delay() - 50000);
                        } else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
                            mediaPlayer.audio().setDelay(mediaPlayer.audio().delay() + 50000);
                        } else if (keyEvent.getKeyCode() == KeyEvent.VK_1) {
                            mediaPlayer.controls().setTime(60000 * 1);
                        } else if (keyEvent.getKeyCode() == KeyEvent.VK_2) {
                            mediaPlayer.controls().setTime(60000 * 2);
                        } else if (keyEvent.getKeyCode() == KeyEvent.VK_3) {
                            mediaPlayer.controls().setTime(60000 * 3);
                        }
                    }
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);

        mainFrame.setVisible(true);

        equalizerFrame.pack();
        equalizerFrame.setVisible(true);

        mediaPlayer.events().addMediaPlayerEventListener(new TestPlayerMediaPlayerEventListener());

        // You can set a logo like this if you like...
        File logoFile = new File("./etc/vlcj-logo.png");
        if (logoFile.exists()) {
            mediaPlayer.logo().setFile(logoFile.getAbsolutePath());
            mediaPlayer.logo().setOpacity(0.5f);
            mediaPlayer.logo().setLocation(10, 10);
            mediaPlayer.logo().enable(true);
        }

        // Demo the marquee
        mediaPlayer.marquee().setText("vlcj java bindings for vlc");
        mediaPlayer.marquee().setSize(40);
        mediaPlayer.marquee().setOpacity(95);
        mediaPlayer.marquee().setColour(Color.white);
        mediaPlayer.marquee().setTimeout(5000);
        mediaPlayer.marquee().setLocation(50, 120);
        mediaPlayer.marquee().enable(true);
    }

    private JMenuBar buildMenuBar() {
        // Menus are just added as an example of overlapping the video - they are non-functional in this demo player
        JMenuBar menuBar = new JMenuBar();

        JMenu mediaMenu = new JMenu("Media");
        mediaMenu.setMnemonic('m');

        JMenuItem mediaPlayFileMenuItem = new JMenuItem("Play File...");
        mediaPlayFileMenuItem.setMnemonic('f');
        mediaMenu.add(mediaPlayFileMenuItem);

        JMenuItem mediaPlayStreamMenuItem = new JMenuItem("Play Stream...");
        mediaPlayFileMenuItem.setMnemonic('s');
        mediaMenu.add(mediaPlayStreamMenuItem);

        mediaMenu.add(new JSeparator());

        JMenuItem mediaExitMenuItem = new JMenuItem("Exit");
        mediaExitMenuItem.setMnemonic('x');
        mediaMenu.add(mediaExitMenuItem);

        menuBar.add(mediaMenu);

        JMenu playbackMenu = new JMenu("Playback");
        playbackMenu.setMnemonic('p');

        JMenu playbackChapterMenu = new JMenu("Chapter");
        playbackChapterMenu.setMnemonic('c');
        for (int i = 1; i <= 25; i ++ ) {
            JMenuItem chapterMenuItem = new JMenuItem("Chapter " + i);
            playbackChapterMenu.add(chapterMenuItem);
        }
        playbackMenu.add(playbackChapterMenu);

        JMenu subtitlesMenu = new JMenu("Subtitles");
        playbackChapterMenu.setMnemonic('s');
        String[] subs = {"01 English (en)", "02 English Commentary (en)", "03 French (fr)", "04 Spanish (es)", "05 German (de)", "06 Italian (it)"};
        for (int i = 0; i < subs.length; i ++ ) {
            JMenuItem subtitlesMenuItem = new JMenuItem(subs[i]);
            subtitlesMenu.add(subtitlesMenuItem);
        }
        playbackMenu.add(subtitlesMenu);

        menuBar.add(playbackMenu);

        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic('t');

        JMenuItem toolsPreferencesMenuItem = new JMenuItem("Preferences...");
        toolsPreferencesMenuItem.setMnemonic('p');
        toolsMenu.add(toolsPreferencesMenuItem);

        menuBar.add(toolsMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('h');

        JMenuItem helpAboutMenuItem = new JMenuItem("About...");
        helpAboutMenuItem.setMnemonic('a');
        helpMenu.add(helpAboutMenuItem);

        menuBar.add(helpMenu);

        return menuBar;
    }

    private final class TestPlayerMediaPlayerEventListener extends MediaPlayerEventAdapter {
        @Override
        public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
            if (newCount == 0) {
                return;
            }
            final Dimension dimension = mediaPlayer.video().videoDimension();
            if (dimension != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        videoSurface.setSize(dimension);
                        mainFrame.pack();
                    }
                });
            }
        }
    }

}
