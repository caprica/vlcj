package uk.co.caprica.vlcj.player.events.renderer;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.renderer_discoverer_item_deleted;
import uk.co.caprica.vlcj.renderer.RendererItem;
import uk.co.caprica.vlcj.renderer.RendererDiscoverer;

public class RendererDiscovererItemDeletedEvent extends RendererDiscovererEvent {

    private final RendererItem item;

    public RendererDiscovererItemDeletedEvent(LibVlc libvlc, RendererDiscoverer rendererDiscoverer, libvlc_event_t event) {
        super(libvlc, rendererDiscoverer);
        this.item = new RendererItem(libvlc, ((renderer_discoverer_item_deleted) event.u.getTypedValue(renderer_discoverer_item_deleted.class)).item);
    }

    @Override
    public void notify(RendererDiscovererEventListener listener) {
        listener.rendererDiscovererItemDeleted(rendererDiscoverer, item);
    }
}
