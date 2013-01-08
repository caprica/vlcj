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

package uk.co.caprica.vlcj.test.fullscreen;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * This test plays multiple media files in full-screen mode.
 * <p>
 * Each media file should appear in the full-screen window, no native windows should be created.
 * <p>
 * Specify a directory containing media files on the command-line, and use the space key to start
 * playing the next file.
 * <p>
 * This test searches for ".iso" media files, change the {@link #VIDEO_FILE_FILTER} to search for
 * other media file types.
 */
public class FullScreenMultiMediaTest extends VlcjTest {

    private static final FileFilter VIDEO_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isFile() && pathname.getName().endsWith(".iso"); // <--- change this to match other media files
        }
    };

    private static final FileFilter DIR_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    };

    public static void main(final String[] args) {
        if(args.length != 1) {
            System.err.println("Specify a single media directory");
            System.exit(1);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FullScreenMultiMediaTest(args);
            }
        });
    }

    private int currentIndex = 0;

    public FullScreenMultiMediaTest(String[] args) {
        Canvas c = new Canvas();
        c.setBackground(Color.black);

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(c, BorderLayout.CENTER);

        JFrame f = new JFrame();
        f.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        f.setContentPane(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(800, 600);

        final MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        final EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new DefaultFullScreenStrategy(f));
        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));

        f.setVisible(true);

        mediaPlayer.setFullScreen(true);

        final List<File> files = scan(new File(args[0]));

        p.getActionMap().put("next", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentIndex ++ ;
                if(currentIndex < files.size()) {
                    mediaPlayer.playMedia(files.get(currentIndex).getAbsolutePath());
                }
            }
        });

        p.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "next");

        if(files.isEmpty()) {
            System.out.println("No media files found");
            System.exit(1);
        }

        mediaPlayer.playMedia(files.get(0).getAbsolutePath());
    }

    private static List<File> scan(File root) {
        List<File> result = new ArrayList<File>(200);
        scan(root, result);
        return result;
    }

    private static void scan(File root, List<File> result) {
        for(File file : root.listFiles(VIDEO_FILE_FILTER)) {
            result.add(file);
        }
        for(File dir : root.listFiles(DIR_FILTER)) {
            scan(dir, result);
        }
    }
}
