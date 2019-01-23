package uk.co.caprica.vlcj.medialist;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.eventmanager.EventNotification;
import uk.co.caprica.vlcj.eventmanager.NativeEventManager;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListEventListener;
import uk.co.caprica.vlcj.player.events.media.MediaEventFactory;
import uk.co.caprica.vlcj.player.events.media.MediaEventListener;
import uk.co.caprica.vlcj.player.events.medialist.MediaListEventFactory;

final class MediaListNativeEventManager extends NativeEventManager<MediaList, MediaListEventListener> {

    MediaListNativeEventManager(LibVlc libvlc, MediaList eventObject) {
        super(libvlc, eventObject, libvlc_event_e.libvlc_MediaListItemAdded, libvlc_event_e.libvlc_MediaListEndReached, "media-list-events");
    }

    @Override
    protected libvlc_event_manager_t onGetEventManager(LibVlc libvlc, MediaList eventObject) {
        return libvlc.libvlc_media_list_event_manager(eventObject.mediaListInstance());
    }

    @Override
    protected EventNotification onCreateEvent(LibVlc libvlc, libvlc_event_t event, MediaList eventObject) {
        return MediaListEventFactory.createEvent(eventObject, event);
    }

}
