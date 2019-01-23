package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.eventmanager.EventNotification;
import uk.co.caprica.vlcj.eventmanager.NativeEventManager;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.events.standard.StandardEventFactory;

final class MediaPlayerNativeEventManager extends NativeEventManager<MediaPlayer, MediaPlayerEventListener> {

    MediaPlayerNativeEventManager(LibVlc libvlc, MediaPlayer eventObject) {
        super(libvlc, eventObject, libvlc_event_e.libvlc_MediaPlayerMediaChanged, libvlc_event_e.libvlc_MediaPlayerChapterChanged, "media-player-events");
    }

    @Override
    protected libvlc_event_manager_t onGetEventManager(LibVlc libvlc, MediaPlayer eventObject) {
        return libvlc.libvlc_media_player_event_manager(eventObject.mediaPlayerInstance());
    }

    @Override
    protected EventNotification onCreateEvent(LibVlc libvlc, libvlc_event_t event, MediaPlayer eventObject) {
        return StandardEventFactory.createEvent(eventObject, event);
    }

}
