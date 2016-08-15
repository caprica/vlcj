package uk.co.caprica.vlcj.player.events;

/**
 * Enumeration of media player event bit-masks.
 * <p>
 * <strong>The event flag masks should be considered private implementation and are subject to
 * change.</strong>
 */
public enum MediaPlayerEventType {

    NONE                           (0x0000000000000000L),

    MEDIA_CHANGED                  (0x0000000000000001L),
    OPENING                        (0x0000000000000002L),
    BUFFERING                      (0x0000000000000004L),
    PLAYING                        (0x0000000000000008L),
    PAUSED                         (0x0000000000000010L),
    STOPPED                        (0x0000000000000020L),
    FORWARD                        (0x0000000000000040L),
    BACKWARD                       (0x0000000000000080L),
    FINISHED                       (0x0000000000000100L),
    TIME_CHANGED                   (0x0000000000000200L),
    POSITION_CHANGED               (0x0000000000000400L),
    SEEKABLE_CHANGED               (0x0000000000000800L),
    PAUSABLE_CHANGED               (0x0000000000001000L),
    TITLE_CHANGED                  (0x0000000000002000L),
    SNAPSHOT_TAKEN                 (0x0000000000004000L),
    LENGTH_CHANGED                 (0x0000000000008000L),
    VIDEO_OUTPUT                   (0x0000000000010000L),
    SCRAMBLED_CHANGED              (0x0000000000020000L),
    ES_ADDED                       (0x0000000000040000L),
    ES_DELETED                     (0x0000000000080000L),
    ES_SELECTED                    (0x0000000000100000L),
    ERROR                          (0x0000000000200000L),

    MEDIA_META_CHANGED             (0x0000000000400000L),
    MEDIA_SUB_ITEM_ADDED           (0x0000000000800000L),
    MEDIA_DURATION_CHANGED         (0x0000000001000000L),
    MEDIA_PARSED_CHANGED           (0x0000000002000000L),
    MEDIA_PARSED_STATUS            (0x0000000004000000L),
    MEDIA_FREED                    (0x0000000008000000L),
    MEDIA_STATE_CHANGED            (0x0000000010000000L),
    MEDIA_SUB_ITEM_TREE_ADDED      (0x0000000020000000L),

    MEDIA_PLAYER_CORKED            (0x0000000040000000L),
    MEDIA_PLAYER_MUTED             (0x0000000080000000L),
    MEDIA_PLAYER_AUDIO_VOLUME      (0x0000000100000000L),
    MEDIA_PLAYER_AUDIO_DEVICE      (0x0000000200000000L),

    MEDIA_PLAYER_CHAPTER_CHANGED   (0x0000000400000000L),

    NEW_MEDIA                      (0x0000000800000000L),
    SUB_ITEM_PLAYED                (0x0000001000000000L),
    SUB_ITEM_FINISHED              (0x0000002000000000L),
    END_OF_SUB_ITEMS               (0x0000004000000000L),

    ALL                            (0xffffffffffffffffL);

    /**
     * Bit-mask.
     */
    private long value;

    /**
     * Create an enumerated value.
     *
     * @param value bit-mask
     */
    private MediaPlayerEventType(long value) {
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
    public static long events(MediaPlayerEventType... types) {
        long eventMask = 0;
        for(MediaPlayerEventType type : types) {
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
    public static long notEvents(MediaPlayerEventType... types) {
        long eventMask = ALL.value;
        for(MediaPlayerEventType type : types) {
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
    public static boolean set(long value, MediaPlayerEventType type) {
        return (value & type.value()) != 0;
    }

    /**
     * Test whether or not a media player event type is set in a bit-mask.
     *
     * @param value bit-mask
     * @param type event type to test for
     * @return <code>false</code> if the type value is set in the bit-mask, otherwise <code>true</code>
     */
    public static boolean notSet(long value, MediaPlayerEventType type) {
        return (value & type.value()) == 0;
    }
}
