package uk.co.caprica.vlcj.binding.internal;

public enum libvlc_video_marquee_option_t {

  libvlc_marquee_Enable  (0),
  libvlc_marquee_Text    (1),        /** string argument */
  libvlc_marquee_Color   (2),
  libvlc_marquee_Opacity (3),
  libvlc_marquee_Position(4),
  libvlc_marquee_Refresh (5),
  libvlc_marquee_Size    (6),
  libvlc_marquee_Timeout (7),
  libvlc_marquee_X       (8),
  libvlc_marquee_Y       (9);

  private final int intValue;
  
  private libvlc_video_marquee_option_t(int intValue) {
    this.intValue = intValue;
  }

  public int intValue() {
    return intValue;
  }
}
