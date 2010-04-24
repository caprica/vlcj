package uk.co.caprica.vlcj.player.windows;

import java.awt.Canvas;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.player.MediaPlayer;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class WindowsMediaPlayer extends MediaPlayer {

  public WindowsMediaPlayer(String[] args) {
    super(args);
  }

  @Override
  protected void nativeSetVideoSurface(libvlc_media_player_t mediaPlayerInstance, Canvas videoSurface) {
    // Thanks for patch from BraCa
    long drawable = Native.getComponentID(videoSurface);
    Pointer ptr = Pointer.createConstant(drawable);
    libvlc.libvlc_media_player_set_hwnd(mediaPlayerInstance, ptr);
  }
}
