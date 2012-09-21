package uk.co.caprica.vlcj.player.list.events;

/**
 * Enumeration of media list player event bit-masks.
 */
public enum MediaListPlayerEventType {

    /**
     *
     */
    NEXT_ITEM_SET(0x00000001),

    /**
     *
     */
    ALL(0xffffffff);

    /**
     * Bit-mask.
     */
    private int value;

    /**
     * Create an enumerated value.
     *
     * @param value bit-mask
     */
    private MediaListPlayerEventType(int value) {
        this.value = value;
    }

    /**
     * Get the bit-mask.
     *
     * @return value
     */
    public final int value() {
        return value;
    }

    /**
     * Get a bit-mask for one or more event types.
     *
     * @param types one or more event types
     * @return bit-mask
     */
    public static int events(MediaListPlayerEventType... types) {
        int eventMask = 0;
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
    public static int notEvents(MediaListPlayerEventType... types) {
        int eventMask = ALL.value;
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
    public static boolean set(int value, MediaListPlayerEventType type) {
        return (value & type.value()) != 0;
    }

    /**
     * Test whether or not a media player event type is set in a bit-mask.
     *
     * @param value bit-mask
     * @param type event type to test for
     * @return <code>false</code> if the type value is set in the bit-mask, otherwise <code>true</code>
     */
    public static boolean notSet(int value, MediaListPlayerEventType type) {
        return (value & type.value()) == 0;
    }
}
