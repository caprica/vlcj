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

package uk.co.caprica.vlcj.test.volume;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * A minimal volume (not audio volume!) test player.
 * <p>
 * This test scans a directory recursively for ".iso" media files and plays each one for a short
 * period.
 * <p>
 * This test is mainly used so that a profiler can connect to the JVM to help detect any Java heap
 * memory leaks.
 */
public class VolumeTestPlayer extends VlcjTest {

    public static void main(String[] args) throws Exception {
        Frame f = new Frame("Test Player");
        f.setIconImage(new ImageIcon(VolumeTestPlayer.class.getResource("/icons/vlcj-logo.png")).getImage());
        f.setSize(800, 600);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setLayout(new BorderLayout());
        Canvas vs = new Canvas();
        f.add(vs, BorderLayout.CENTER);
        f.setVisible(true);

        MediaPlayerFactory factory = new MediaPlayerFactory();

        EmbeddedMediaPlayer mediaPlayer = factory.newEmbeddedMediaPlayer();
        mediaPlayer.setVideoSurface(factory.newVideoSurface(vs));

        List<File> files = scanMedia(new File("/movies"));

        Thread.sleep(3000);

        for(File file : files) {
            mediaPlayer.playMedia(file.getAbsolutePath());
            Thread.sleep(500);
            mediaPlayer.setChapter(4);
            Thread.sleep(2000);
        }

        mediaPlayer.stop();
        mediaPlayer.release();
        factory.release();

        f.setVisible(false);

        System.out.println("Finished");

        // Don't exit so a profiler can connect
    }

    private static List<File> scanMedia(File root) {
        List<File> result = new ArrayList<File>(100);
        scanMedia(root, result);
        return result;
    }

    private static void scanMedia(File root, List<File> result) {
        File[] files = root.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".iso");
            }
        });

        for(File file : files) {
            result.add(file);
        }

        File[] dirs = root.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        for(File dir : dirs) {
            scanMedia(dir, result);
        }
    }
}
