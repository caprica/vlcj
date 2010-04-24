package uk.co.caprica.vlcj.player.mac;

import java.awt.Canvas;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.player.MediaPlayer;

public class MacMediaPlayer extends MediaPlayer {

  public MacMediaPlayer(String[] args) {
    super(args);
  }

  @Override
  protected void nativeSetVideoSurface(libvlc_media_player_t mediaPlayerInstance, Canvas videoSurface) {
    throw new UnsupportedOperationException("Send patches!");
  }
}
