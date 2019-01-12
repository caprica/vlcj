package uk.co.caprica.vlcj.test.renderer;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.events.renderer.RendererDiscovererEventListener;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.renderer.RendererDiscoverer;
import uk.co.caprica.vlcj.renderer.RendererDiscovererDescription;
import uk.co.caprica.vlcj.renderer.RendererItem;

import java.util.List;

/**
 * A simple test of the renderer discover, which can be used e.g. send media to a Chromecast or some other device.
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
        final HeadlessMediaPlayer mediaPlayer = factory.mediaPlayers().newHeadlessMediaPlayer();

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
