package uk.co.caprica.vlcj.player.events;

/**
 * Enumeration of media player event bit-masks.
 */
public enum MediaPlayerEventType {

  MEDIA_CHANGED      (0x00000001),
  OPENING            (0x00000002),
  BUFFERING          (0x00000004),
  PLAYING            (0x00000008),
  PAUSED             (0x00000010),
  STOPPED            (0x00000020),
  FORWARD            (0x00000040),
  BACKWARD           (0x00000080),
  FINISHED           (0x00000100),
  TIME_CHANGED       (0x00000200),
  POSITION_CHANGED   (0x00000400),
  SEEKABLE_CHANGED   (0x00000800),
  PAUSABLE_CHANGED   (0x00001000),
  TITLE_CHANGED      (0x00002000),
  SNAPSHOT_TAKEN     (0x00004000),
  LENGTH_CHANGED     (0x00008000),
  ERROR              (0x00010000),

  META_DATA_AVAILABLE(0x80000000), // FIXME use this

  ALL                (0xffffffff);
  
  /**
   * Bit-mask.
   */
  private int value;
 
  /**
   * Create an enumerated value.
   * 
   * @param value bit-mask
   */
  private MediaPlayerEventType(int value) {
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
  public static int events(MediaPlayerEventType... types) {
    int eventMask = 0;
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
  public static int notEvents(MediaPlayerEventType... types) {
    int eventMask = ALL.value;
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
  public static boolean set(int value, MediaPlayerEventType type) {
    return (value & type.value()) != 0;
  }
  
  /**
   * Test whether or not a media player event type is set in a bit-mask.
   * 
   * @param value bit-mask
   * @param type event type to test for
   * @return <code>false</code> if the type value is set in the bit-mask, otherwise <code>true</code>
   */
  public static boolean notSet(int value, MediaPlayerEventType type) {
    return (value & type.value()) == 0;
  }
}
