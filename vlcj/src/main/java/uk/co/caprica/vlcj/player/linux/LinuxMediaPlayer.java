package uk.co.caprica.vlcj.player.linux;

import java.awt.Canvas;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.player.MediaPlayer;

import com.sun.jna.Native;

public class LinuxMediaPlayer extends MediaPlayer {

  public LinuxMediaPlayer(String[] args) {
    super(args);
    // TODO Auto-generated constructor stub
  }

  @Override
  protected void nativeSetVideoSurface(libvlc_media_player_t mediaPlayerInstance, Canvas videoSurface) {
    long drawable = Native.getComponentID(videoSurface);
    libvlc.libvlc_media_player_set_xwindow(mediaPlayerInstance, drawable);
  }
}
