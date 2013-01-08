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

package uk.co.caprica.vlcj.test.manager;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.manager.MediaManager;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * A very simple test showing the absolute basics of using the media manager (VLM)
 * to broadcast multiple streams.
 * <p>
 * The client may of course be a vlcj client application, the vlc application
 * itself or any other client that can play RTP streams.
 * <p>
 * Inputs/outputs are flexible - you do not have to play a <em>file</em>.
 * <p>
 * The native media manager (VLM) is quite basic - it is probably easier to build
 * a broadcast server or video-on-demand server in Java.
 */
public class BroadcastTest extends VlcjTest {

    public static void main(String[] args) throws Exception {
        MediaPlayerFactory factory = new MediaPlayerFactory();

        MediaManager manager = factory.newMediaManager();

        // First set up all of the broadcasts...

        // Client MRL: rtp://@230.0.0.1:5004
        manager.addBroadcast("Movie1", "/movies/Movie1.iso", "#rtp{dst=230.0.0.1,port=5004,mux=ts", true, false);

        // Client MRL: rtp://@230.0.0.1:5005
        manager.addBroadcast("Movie2", "/movies/Movie2.iso", "#rtp{dst=230.0.0.1,port=5005,mux=ts", true, false);

        // Next play all of the broadcasts
        manager.play("Movie1");
        manager.play("Movie2");

        // Dump out some information about the media (these methods return JSON)
        System.out.println("Movie1: " + manager.show("Movie1"));
        System.out.println("Movie2: " + manager.show("Movie2"));

        // Wait forever
        Thread.currentThread().join();
    }
}
