package uk.co.caprica.vlcj.player.events.media;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.media.Media;

abstract public class MediaEvent {

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

    /**
     * Notify a listener of the event.
     *
     * @param listener event listener to notify
     */
    abstract public void notify(MediaEventListener listener);

}
