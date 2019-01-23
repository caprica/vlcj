package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.eventmanager.EventNotification;
import uk.co.caprica.vlcj.eventmanager.NativeEventManager;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListEventListener;
import uk.co.caprica.vlcj.player.events.medialist.MediaListEventFactory;
import uk.co.caprica.vlcj.player.list.events.MediaListPlayerEventFactory;

final public class MediaListPlayerNativeEventManager extends NativeEventManager<MediaListPlayer, MediaListPlayerEventListener> {

    MediaListPlayerNativeEventManager(LibVlc libvlc, MediaListPlayer eventObject) {
        super(libvlc, eventObject, libvlc_event_e.libvlc_MediaListPlayerPlayed, libvlc_event_e.libvlc_MediaListPlayerStopped, "media-list-player-events");
    }

    @Override
    protected libvlc_event_manager_t onGetEventManager(LibVlc libvlc, MediaListPlayer eventObject) {
        return libvlc.libvlc_media_list_player_event_manager(eventObject.mediaListPlayerInstance());
    }

    @Override
    protected EventNotification onCreateEvent(LibVlc libvlc, libvlc_event_t event, MediaListPlayer eventObject) {
        return MediaListPlayerEventFactory.createEvent(eventObject, event);
    }

}
