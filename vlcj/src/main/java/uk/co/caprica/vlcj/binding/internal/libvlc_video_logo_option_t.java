package uk.co.caprica.vlcj.binding.internal;

public enum libvlc_video_logo_option_t {
  
  libvlc_logo_enable  (0),
  libvlc_logo_file    (1),           /**< string argument, "file,d,t;file,d,t;..." */
  libvlc_logo_x       (2),
  libvlc_logo_y       (3),
  libvlc_logo_delay   (4),
  libvlc_logo_repeat  (5),
  libvlc_logo_opacity (6),
  libvlc_logo_position(7);

  private final int intValue;
  
  private libvlc_video_logo_option_t(int intValue) {
    this.intValue = intValue;
  }
  
  public int intValue() {
    return intValue;
  }
}
