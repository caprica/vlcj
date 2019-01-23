package uk.co.caprica.vlcj.player.events.media;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.eventmanager.EventNotification;
import uk.co.caprica.vlcj.media.Media;

abstract class MediaEvent implements EventNotification<MediaEventListener> {

    protected final LibVlc libvlc;

    /**
     * The media list the event relates to.
     */
    protected final Media media;

    /**
     * Create a media list event.
     *
     * @param media media that the event relates to
     */
    protected MediaEvent(LibVlc libvlc, Media media) {
        this.libvlc = libvlc;
        this.media = media;
    }

}
