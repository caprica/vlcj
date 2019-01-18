package uk.co.caprica.vlcj.player.events.media;

import uk.co.caprica.vlcj.enums.MediaParsedStatus;
import uk.co.caprica.vlcj.enums.State;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.media.Media;

public interface MediaEventListener {

    /**
     * Current media meta data changed.
     *
     * @param media media that raised the event
     * @param metaType type of meta data that changed
     */
    void mediaMetaChanged(Media media, int metaType);

    /**
     * A new sub-item was added to the current media.
     *
     * @param media media that raised the event
     * @param subItem native sub-item handle
     */
    void mediaSubItemAdded(Media media, libvlc_media_t subItem);

    /**
     * The current media duration changed.
     *
     * @param media media that raised the event
     * @param newDuration new duration (number of milliseconds)
     */
    void mediaDurationChanged(Media media, long newDuration);

    /**
     * The current media parsed status changed.
     *
     * @param media media that raised the event
     * @param newStatus new parsed status
     */
    void mediaParsedChanged(Media media, MediaParsedStatus newStatus);

    /**
     * The current media was freed.
     *
     * @param media media that raised the event
     */
    void mediaFreed(Media media);

    /**
     * The current media state changed.
     *
     * @param media media that raised the event
     * @param newState new state
     */
    void mediaStateChanged(Media media, State newState);

    /**
     * A sub-item tree was added to the media.
     *
     * @param media media that raised the event
     * @param item media item
     */
    void mediaSubItemTreeAdded(Media media, libvlc_media_t item);

}
