package uk.co.caprica.vlcj.player.events.renderer;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.renderer_discoverer_item_added;
import uk.co.caprica.vlcj.renderer.RendererItem;
import uk.co.caprica.vlcj.renderer.RendererDiscoverer;

// probably gonna have to create the RendererItem in here to give the listener a chance to hold it

public class RendererDiscovererItemAddedEvent extends RendererDiscovererEvent {

    private RendererItem item;

    public RendererDiscovererItemAddedEvent(LibVlc libvlc, RendererDiscoverer rendererDiscoverer, libvlc_event_t event) {
        super(libvlc, rendererDiscoverer);
        this.item = new RendererItem(libvlc, ((renderer_discoverer_item_added) event.u.getTypedValue(renderer_discoverer_item_added.class)).item);
    }

    @Override
    public void notify(RendererDiscovererEventListener listener) {
        listener.rendererDiscovererItemAdded(rendererDiscoverer, item);
    }
}
