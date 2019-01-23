package uk.co.caprica.vlcj.renderer;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.eventmanager.EventNotification;
import uk.co.caprica.vlcj.eventmanager.NativeEventManager;
import uk.co.caprica.vlcj.player.events.renderer.RendererDiscovererEventFactory;
import uk.co.caprica.vlcj.player.events.renderer.RendererDiscovererEventListener;

final class RendererDiscovererNativeEventManager extends NativeEventManager<RendererDiscoverer, RendererDiscovererEventListener> {

    RendererDiscovererNativeEventManager(LibVlc libvlc, RendererDiscoverer eventObject) {
        super(libvlc, eventObject, libvlc_event_e.libvlc_RendererDiscovererItemAdded, libvlc_event_e.libvlc_RendererDiscovererItemDeleted, "renderer-discoverer-events");
    }

    @Override
    protected libvlc_event_manager_t onGetEventManager(LibVlc libvlc, RendererDiscoverer eventObject) {
        return libvlc.libvlc_renderer_discoverer_event_manager(eventObject.rendererDiscovererInstance());
    }

    @Override
    protected EventNotification onCreateEvent(LibVlc libvlc, libvlc_event_t event, RendererDiscoverer eventObject) {
        return RendererDiscovererEventFactory.createEvent(libvlc, eventObject, event);
    }

}
