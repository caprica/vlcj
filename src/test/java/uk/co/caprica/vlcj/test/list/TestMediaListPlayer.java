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

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListEventAdapter;
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A simple example showing how to use a media list player.
 * <p>
 * This test does not embed a video window so a new native video window will be created for each
 * movie in the play-list.
 */
@SuppressWarnings("serial")
public class TestMediaListPlayer extends VlcjTest {

    private final MediaPlayerFactory mediaPlayerFactory;

    private final EmbeddedMediaPlayer mediaPlayer;

    private final Canvas canvas;

    private final VideoSurface videoSurface;

    private final MediaList mediaList;

    private final MediaListPlayer mediaListPlayer;

    private final JFrame mainFrame;

    private final JFrame listFrame;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        TestMediaListPlayer app = new TestMediaListPlayer();
    }

    public TestMediaListPlayer() {
        this.mediaPlayerFactory = new MediaPlayerFactory();
        this.mediaPlayer        = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        this.canvas             = new Canvas();
        this.videoSurface       = mediaPlayerFactory.videoSurfaces().newVideoSurface(canvas);
        this.mediaList          = mediaPlayerFactory.media().newMediaList();
        this.mediaListPlayer    = mediaPlayerFactory.mediaPlayers().newMediaListPlayer();

        MediaRef mediaRef = mediaPlayerFactory.media().newMediaRef("https://www.youtube.com/watch?v=zGt444zwSAM");
        mediaList.media().add(mediaRef);
        mediaRef.release();

        MediaListRef mediaListRef = mediaList.newMediaListRef();
        try {
            mediaListPlayer.list().setMediaList(mediaListRef);
        }
        finally {
            mediaListRef.release();
        }

        mediaListPlayer.mediaPlayer().setMediaPlayer(mediaPlayer);
        mediaPlayer.videoSurface().set(videoSurface);

        mediaListPlayer.mediaPlayer().setMediaPlayer(mediaPlayer);

        this.mainFrame = new MainFrame();
        this.listFrame = new ListFrame();

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                listFrame.setVisible(false);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                listFrame.setVisible(true);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                mediaListPlayer.release();
                mediaList.release();
                mediaPlayer.release();
                System.exit(0);
            }
        });

        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                listFrame.setLocation(mainFrame.getX() + mainFrame.getWidth() + 4, mainFrame.getY());
                listFrame.setSize(listFrame.getWidth(), mainFrame.getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                listFrame.setLocation(mainFrame.getX() + mainFrame.getWidth() + 4, mainFrame.getY());
            }

            @Override
            public void componentShown(ComponentEvent e) {
                listFrame.setVisible(true);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                listFrame.setVisible(false);
            }

        });

        mainFrame.setVisible(true);
    }

    private class MainFrame extends JFrame {

        public MainFrame() {
            setTitle("MediaListPlayer Test");
            setBounds(100, 100, 800, 600);

            JPanel contentPane = new JPanel();
            contentPane.setLayout(new BorderLayout());
            canvas.setBackground(Color.black);
            contentPane.add(canvas, BorderLayout.CENTER);
            setContentPane(contentPane);
        }

    }

    private class ListFrame extends JFrame {

        public ListFrame() {
            setTitle("Playlist");
            setBounds(904, 100, 400, 600);
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            JPanel contentPane = new JPanel();
            contentPane.setLayout(new BorderLayout());

            JList list = new JList();
            list.setModel(new PlaylistModel());

            JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setViewportView(list);
            contentPane.add(scrollPane, BorderLayout.CENTER);

            JPanel controlsPane = new JPanel();
            controlsPane.setLayout(new FlowLayout(FlowLayout.LEFT));
            JButton playButton = new JButton("Play");
            controlsPane.add(playButton);
            JButton nextButton = new JButton("Next");
            controlsPane.add(nextButton);
            JButton previousButton = new JButton("Previous");
            controlsPane.add(previousButton);
            JButton pauseButton = new JButton("Pause");
            controlsPane.add(pauseButton);
            JButton stopButton = new JButton("Stop");
            controlsPane.add(stopButton);
            JButton clearButton = new JButton("Clear");
            controlsPane.add(clearButton);
            contentPane.add(controlsPane, BorderLayout.SOUTH);

            setContentPane(contentPane);

            list.setTransferHandler(new MediaTransferHandler() {
                @Override
                protected void onMediaDropped(String[] uris) {
                    for (String uri : uris) {
                        mediaList.media().add(uri);
                    }
                    System.out.println("IS READ ONLY RETURNS " + mediaList.media().isReadOnly());
                }
            });

            list.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent listSelectionEvent) {
                }
            });

            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JList list = (JList) e.getSource();
                    if (e.getClickCount() == 2) {
                        int index = list.locationToIndex(e.getPoint());
                        if (index != -1) {
                            mediaListPlayer.controls().play(index);
                        }
                    }
                }
            });

            playButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    mediaListPlayer.controls().play();
                }
            });

            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    mediaListPlayer.controls().playNext();
                }
            });

            previousButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    mediaListPlayer.controls().playPrevious();
                }
            });

            pauseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    mediaListPlayer.controls().pause();
                }
            });

            stopButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    mediaListPlayer.controls().stop();
                }
            });

            clearButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    mediaList.media().clear();
                }
            });
        }
    }

    // This is not really ideal, there could be an update from a native thread between getSize() and something else...
    // it's almost like each change should copy the native list to a java list really.
    private class PlaylistModel extends AbstractListModel {

        private PlaylistModel() {
            mediaList.events().addMediaListEventListener(new MediaListEventAdapter() {
                @Override
                public void mediaListItemAdded(MediaList mediaList, MediaRef item, final int index) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            fireIntervalAdded(this, index, index);
                        }
                    });
                }

                @Override
                public void mediaListItemDeleted(MediaList mediaList, MediaRef item, final int index) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            fireIntervalRemoved(this, index, index);
                        }
                    });
                }
            });
        }

        @Override
        public int getSize() {
            return mediaList.media().count();
        }

        @Override
        public Object getElementAt(int i) {
            return mediaList.media().mrl(i);
        }
    }

}
