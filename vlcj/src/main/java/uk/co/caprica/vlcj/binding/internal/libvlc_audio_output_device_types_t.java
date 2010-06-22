package uk.co.caprica.vlcj.binding.internal;

public enum libvlc_audio_output_device_types_t {
  
  libvlc_AudioOutputDevice_Error (-1),
  libvlc_AudioOutputDevice_Mono  ( 1),
  libvlc_AudioOutputDevice_Stereo( 2),
  libvlc_AudioOutputDevice_2F2R  ( 4),
  libvlc_AudioOutputDevice_3F2R  ( 5),
  libvlc_AudioOutputDevice_5_1   ( 6),
  libvlc_AudioOutputDevice_6_1   ( 7),
  libvlc_AudioOutputDevice_7_1   ( 8),
  libvlc_AudioOutputDevice_SPDIF (10);

  private final int intValue;
  
  private libvlc_audio_output_device_types_t(int intValue) {
    this.intValue = intValue;
  }
  
  public int intValue() {
    return intValue;
  }
}
