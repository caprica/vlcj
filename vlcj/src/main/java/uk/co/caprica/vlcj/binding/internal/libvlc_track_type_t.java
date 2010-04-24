package uk.co.caprica.vlcj.binding.internal;

public enum libvlc_track_type_t {

  libvlc_track_unknown(-1),
  libvlc_track_audio  ( 0),
  libvlc_track_video  ( 1),
  libvlc_track_text   ( 2);

  private final int intValue;
  
  private libvlc_track_type_t(int intValue) {
    this.intValue = intValue;
  }
  
  public int intValue() {
    return intValue;
  }
}
