package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.eventmanager.EventNotification;
import uk.co.caprica.vlcj.eventmanager.NativeEventManager;
import uk.co.caprica.vlcj.player.events.media.MediaEventFactory;
import uk.co.caprica.vlcj.player.events.media.MediaEventListener;

final class MediaNativeEventManager extends NativeEventManager<Media, MediaEventListener> {

    MediaNativeEventManager(LibVlc libvlc, Media eventObject) {
        super(libvlc, eventObject, libvlc_event_e.libvlc_MediaMetaChanged, libvlc_event_e.libvlc_MediaThumbnailGenerated, "media-events");
    }

    @Override
    protected libvlc_event_manager_t onGetEventManager(LibVlc libvlc, Media eventObject) {
        return libvlc.libvlc_media_event_manager(eventObject.mediaInstance());
    }

    @Override
    protected EventNotification onCreateEvent(LibVlc libvlc, libvlc_event_t event, Media eventObject) {
        return MediaEventFactory.createEvent(libvlc, eventObject, event);
    }

}
