package uk.co.caprica.vlcj.player.windows;

import java.awt.Canvas;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.player.MediaPlayer;

public class WindowsMediaPlayer extends MediaPlayer {

  public WindowsMediaPlayer(String[] args) {
    super(args);
  }

  @Override
  protected void nativeSetVideoSurface(libvlc_media_player_t mediaPlayerInstance, Canvas videoSurface) {
    throw new UnsupportedOperationException("Send patches!");
  }
}
