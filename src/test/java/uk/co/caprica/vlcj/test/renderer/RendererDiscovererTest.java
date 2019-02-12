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

package uk.co.caprica.vlcj.test.renderer;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.renderer.RendererDiscovererEventListener;
import uk.co.caprica.vlcj.renderer.RendererDiscoverer;
import uk.co.caprica.vlcj.renderer.RendererDiscovererDescription;
import uk.co.caprica.vlcj.renderer.RendererItem;

import java.util.List;

/**
 * A simple test of the renderer discover, which can be used e.g. to send media to a Chromecast or some other device.
 * <p>
 * In this test, pass your renderer name and the MRL you want to play as command-line arguments.
 */
public class RendererDiscovererTest {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Specify <renderer-name> <mrl>");
            System.exit(-1);
        }

        final String myRendererName = args[0];
        final String mrl = args[1];

        MediaPlayerFactory factory = new MediaPlayerFactory();

        List<RendererDiscovererDescription> discoverers = factory.renderers().discoverers();
        System.out.println("discoverers=" + discoverers);

        if (discoverers.size() == 0) {
            return;
        }

        RendererDiscoverer discoverer = factory.renderers().discoverer(discoverers.get(0).name());
        System.out.println(discoverer);

        // If the renderer is found, this won't open a native window
        final MediaPlayer mediaPlayer = factory.mediaPlayers().newMediaPlayer();

        discoverer.addRendererDiscovererEventListener(new RendererDiscovererEventListener() {
            @Override
            public void rendererDiscovererItemAdded(RendererDiscoverer rendererDiscoverer, RendererItem item) {
                System.out.println("ADDED " + item);
                if (item.name().equals(myRendererName)) {
                    System.out.println("Found renderer " + myRendererName);
                    // If we want to use this item, we must explicitly hold it (we should release it later when we no
                    // longer need it)
                    item.hold();
                    item.setRenderer(mediaPlayer);
                    boolean result = mediaPlayer.media().playMedia(mrl);
                    System.out.println("result of play is " + result);
                }
            }

            @Override
            public void rendererDiscovererItemDeleted(RendererDiscoverer rendererDiscoverer, RendererItem item) {
                System.out.println("REMOVED " + item);
            }
        });

        boolean started = discoverer.start();
        System.out.println("discoverer started=" + started);

        System.out.println("Waiting for renderer...");

        Thread.currentThread().join();
    }

}
