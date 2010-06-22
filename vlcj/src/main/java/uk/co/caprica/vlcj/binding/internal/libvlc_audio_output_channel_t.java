package uk.co.caprica.vlcj.binding.internal;

public enum libvlc_audio_output_channel_t {
  
  libvlc_AudioChannel_Error  (-1),
  libvlc_AudioChannel_Stereo ( 1),
  libvlc_AudioChannel_RStereo( 2),
  libvlc_AudioChannel_Left   ( 3),
  libvlc_AudioChannel_Right  ( 4),
  libvlc_AudioChannel_Dolbys ( 5);

  private final int intValue;
  
  private libvlc_audio_output_channel_t(int intValue) {
    this.intValue = intValue;
  }
  
  public int intValue() {
    return intValue;
  }
}
