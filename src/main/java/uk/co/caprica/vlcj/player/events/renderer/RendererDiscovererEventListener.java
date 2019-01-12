package uk.co.caprica.vlcj.player.events.renderer;

import uk.co.caprica.vlcj.renderer.RendererItem;
import uk.co.caprica.vlcj.renderer.RendererDiscoverer;

public interface RendererDiscovererEventListener {

    void rendererDiscovererItemAdded(RendererDiscoverer rendererDiscoverer, RendererItem item);

    void rendererDiscovererItemDeleted(RendererDiscoverer rendererDiscoverer, RendererItem item);

}
