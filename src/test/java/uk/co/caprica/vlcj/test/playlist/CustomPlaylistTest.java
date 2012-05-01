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
 * Copyright 2009, 2010, 2011, 2012 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.playlist;

import static uk.co.caprica.vlcj.component.playlist.discovery.PlaylistDiscovery.addFiles;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.component.playlist.Playlist;
import uk.co.caprica.vlcj.component.playlist.PlaylistComponent;
import uk.co.caprica.vlcj.component.playlist.PlaylistEntry;
import uk.co.caprica.vlcj.component.playlist.PlaylistMode;
import uk.co.caprica.vlcj.component.playlist.RepeatMode;
import uk.co.caprica.vlcj.component.playlist.swing.PlaylistListModel;
import uk.co.caprica.vlcj.filter.AudioFileFilter;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Test the play-list component.
 */
public class CustomPlaylistTest extends VlcjTest {
    
    private final JFrame mainFrame;
    private final JPanel contentPane;
    private final JList list;
    private final JScrollPane listScrollPane;
    private final JButton nextButton;
    private final JButton gotoEndButton;
    
    private final JLabel currentItemLabel;
    
    private final PlaylistListModel listModel;
    
    private final AudioMediaPlayerComponent mediaPlayerComponent;
    
    private final PlaylistComponent playlistComponent;
    
    public static void main(String[] args) throws Throwable {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CustomPlaylistTest test = new CustomPlaylistTest();
                test.start();
            }
        });
    }
    
    @SuppressWarnings("serial")
    public CustomPlaylistTest() {
        nextButton = new JButton("Next");
        gotoEndButton = new JButton("Goto End");
        
        list = new JList();
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                PlaylistEntry playlistEntry = (PlaylistEntry)value;
                JLabel c = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setText(playlistEntry.getMrl());
                return c;
            }
        });
        
        listScrollPane = new JScrollPane();
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        listScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.setViewportView(list);
        
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        
        JPanel controlPanel = new JPanel();
        controlPanel.add(nextButton);
        controlPanel.add(gotoEndButton);
        
        JPanel statusPanel = new JPanel();
        currentItemLabel = new JLabel();
        statusPanel.add(currentItemLabel);
        
        contentPane.add(statusPanel, BorderLayout.NORTH);
        contentPane.add(listScrollPane, BorderLayout.CENTER);
        contentPane.add(controlPanel, BorderLayout.SOUTH);
        
        mainFrame = new JFrame("vlcj playlist");
        mainFrame.setContentPane(contentPane);
        mainFrame.setSize(1200, 800);
        mainFrame.setLocation(50, 50);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                mediaPlayerComponent.getMediaPlayer().stop();
                mediaPlayerComponent.release();
            }
        });
        
        mediaPlayerComponent = new AudioMediaPlayerComponent();
        playlistComponent = new PlaylistComponent(mediaPlayerComponent.getMediaPlayer()) {
            @Override
            public void playlistItemChanged(PlaylistComponent source, final int itemIndex, final PlaylistEntry item) {
                Logger.debug("playlistItemChanged(source={},itemIndex={},item={})", source, itemIndex, item);
                final MediaMeta meta = mediaPlayerComponent.getMediaPlayer().getMediaMeta();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        list.setSelectedIndex(itemIndex);
                        if(itemIndex != -1) {
                            Rectangle r = list.getCellBounds(itemIndex, itemIndex);
                            list.scrollRectToVisible(r);
                        }
                        if(meta != null) {
                            currentItemLabel.setText(meta.getTitle());
                        }
                    }
                });
            }

            @Override
            public void playlistFinished(PlaylistComponent playlistComponent) {
                System.out.println("##################################");
                System.out.println("### Playlist Finished ############");
                System.out.println("##################################");
            }
        };

        playlistComponent.setMode(PlaylistMode.NORMAL);
        playlistComponent.setRepeatMode(RepeatMode.REPEAT_LIST);
        
        listModel = new PlaylistListModel(playlistComponent.getPlaylist());
        list.setModel(listModel);
        
        String dir = "/big/music";
        
        addFiles(playlistComponent.getPlaylist(), new File(dir), AudioFileFilter.INSTANCE);
        
        Playlist playlist = playlistComponent.getPlaylist();
        int i = 1;
        for(PlaylistEntry media : playlist) {
            System.out.printf("%4d %s\n", i++, media);
        }
        
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playlistComponent.playNext();
            }
        });
        
        gotoEndButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playlistComponent.getMediaPlayer().setPosition(0.95f);
            }
        });
    }
    
    private void start() {
        playlistComponent.playNext();
    }
}
