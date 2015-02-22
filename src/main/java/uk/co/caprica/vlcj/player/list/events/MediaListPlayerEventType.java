package uk.co.caprica.vlcj.player.list.events;

/**
 * Enumeration of media list player event bit-masks.
 */
public enum MediaListPlayerEventType {

    /**
     *
     */
    NONE          (0x0000000000000000L),

    /**
     *
     */
    NEXT_ITEM_SET (0x0000000000000001L),

    /**
     *
     */
    ALL           (0xffffffffffffffffL);

    /**
     * Bit-mask.
     */
    private long value;

    /**
     * Create an enumerated value.
     *
     * @param value bit-mask
     */
    private MediaListPlayerEventType(long value) {
        this.value = value;
    }

    /**
     * Get the bit-mask.
     *
     * @return value
     */
    public final long value() {
        return value;
    }

    /**
     * Get a bit-mask for one or more event types.
     *
     * @param types one or more event types
     * @return bit-mask
     */
    public static long events(MediaListPlayerEventType... types) {
        long eventMask = 0;
        for(MediaListPlayerEventType type : types) {
            eventMask |= type.value();
        }
        return eventMask;
    }

    /**
     * Get an inverse bit-mask for one or more event types.
     *
     * @param types one or more event types
     * @return bit-mask
     */
    public static long notEvents(MediaListPlayerEventType... types) {
        long eventMask = ALL.value;
        for(MediaListPlayerEventType type : types) {
            eventMask &= type.value() ^ -1;
        }
        return eventMask;
    }

    /**
     * Test whether or not a media player event type is set in a bit-mask.
     *
     * @param value bit-mask
     * @param type event type to test for
     * @return <code>true</code> if the type value is set in the bit-mask, otherwise <code>false</code>
     */
    public static boolean set(long value, MediaListPlayerEventType type) {
        return (value & type.value()) != 0;
    }

    /**
     * Test whether or not a media player event type is set in a bit-mask.
     *
     * @param value bit-mask
     * @param type event type to test for
     * @return <code>false</code> if the type value is set in the bit-mask, otherwise <code>true</code>
     */
    public static boolean notSet(long value, MediaListPlayerEventType type) {
        return (value & type.value()) == 0;
    }
}
