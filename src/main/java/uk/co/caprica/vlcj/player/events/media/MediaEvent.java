package uk.co.caprica.vlcj.player.events.media;

import uk.co.caprica.vlcj.media.Media;

abstract public class MediaEvent {

    /**
     * The media list the event relates to.
     */
    protected final Media media;

    /**
     * Create a media list event.
     *
     * @param media media that the event relates to
     */
    protected MediaEvent(Media media) {
        this.media = media;
    }

    /**
     * Notify a listener of the event.
     *
     * @param listener event listener to notify
     */
    abstract public void notify(MediaEventListener listener);

}
