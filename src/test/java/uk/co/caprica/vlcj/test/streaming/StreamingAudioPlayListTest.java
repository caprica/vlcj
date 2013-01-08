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

package uk.co.caprica.vlcj.test.streaming;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * A simple example combining a media list player along with network streaming.
 * <p>
 * A directory is scanned recursively for mp3 files to create a play-list. This play-list is then
 * randomised before being played through a media list player.
 * <p>
 * Since a multicast streaming protocol is used, you can have any number of concurrent clients. You
 * can use any client you want, whether it is vlc itself, another client built using vlcj or any
 * other client that can receive and play audio over RTP.
 * <p>
 * You specify three arguments on the command-line when starting this application:
 * <ul>
 * <li>root directory to recursively scan for audio files</li>
 * <li>multicast address to stream to</li>
 * <li>port to stream to</li>
 * </ul>
 * For example:
 *
 * <pre>
 *   /home/music 230.0.0.1 5555
 * </pre>
 *
 * Using this example, the streaming audio can be played in vlc with the following address:
 *
 * <pre>
 *   rtp://230.0.0.1:5555
 * </pre>
 */
public class StreamingAudioPlayListTest extends VlcjTest {

    private final MediaPlayerFactory factory;

    private final MediaListPlayer mediaListPlayer;

    private final MediaList playList;

    public static void main(String[] args) throws Exception {
        if(args.length == 3) {
            String dir = args[0];
            String address = args[1];
            int port = Integer.parseInt(args[2]);
            new StreamingAudioPlayListTest().start(dir, address, port);
        }
        else {
            System.out.println("Specify <media root directory> <multicast address> <port>");
            System.out.println("e.g. /home/music 230.0.0.1 5555");
        }
    }

    public StreamingAudioPlayListTest() {
        factory = new MediaPlayerFactory();
        mediaListPlayer = factory.newMediaListPlayer();
        mediaListPlayer.addMediaListPlayerEventListener(new MediaListPlayerEventAdapter() {
            @Override
            public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl) {
                System.out.println("Playing next item: " + itemMrl + " (" + item + ")");
            }
        });
        playList = factory.newMediaList();
    }

    private void start(String dir, String address, int port) throws Exception {
        System.out.println("Scanning for audio files...");
        // Scan for media files
        List<File> files = scanForMedia(dir);
        // Randomise the order
        Collections.shuffle(files);
        // Prepare the media options for streaming
        String mediaOptions = formatRtpStream(address, port);
        // Add each media file to the play-list...
        for(File file : files) {
            // You could instead set standard options on the media list player rather
            // than setting options each time you add media
            playList.addMedia(file.getAbsolutePath(), mediaOptions);
        }
        // Loop the play-list over and over
        mediaListPlayer.setMode(MediaListPlayerMode.LOOP);
        // Attach the play-list to the media list player
        mediaListPlayer.setMediaList(playList);
        // Finally, start the media player
        mediaListPlayer.play();
        System.out.println("Streaming started at rtp://" + address + ":" + port);
        // Wait forever...
        Thread.currentThread().join();
    }

    /**
     * Search a directory, recursively, for mp3 files.
     *
     * @param root root directory
     * @return collection of mp3 files
     */
    private List<File> scanForMedia(String root) {
        List<File> result = new ArrayList<File>(400);
        scanForMedia(new File(root), result);
        return result;
    }

    private void scanForMedia(File root, List<File> result) {
        if(root.exists() && root.isDirectory()) {
            // List all matching mp3 files...
            File[] files = root.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".mp3");
                }
            });
            // Add them to the collection
            result.addAll(Arrays.asList(files));
            // List all nested directories...
            File[] dirs = root.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });
            // Recursively scan each nested directory...
            for(File dir : dirs) {
                scanForMedia(dir, result);
            }
        }
    }

    private String formatRtpStream(String serverAddress, int serverPort) {
        StringBuilder sb = new StringBuilder(60);
        sb.append(":sout=#rtp{dst=");
        sb.append(serverAddress);
        sb.append(",port=");
        sb.append(serverPort);
        sb.append(",mux=ts}");
        return sb.toString();
    }
}
