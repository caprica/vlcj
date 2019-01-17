package uk.co.caprica.vlcj.component;

import com.sun.jna.Memory;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;

import java.awt.*;
import java.awt.event.*;

/**
 *
 *
 * <p>
 * Having this class saves cluttering the {@link DirectMediaPlayerComponent} with all of these empty event listener
 * methods.
 */
abstract class DirectMediaPlayerComponentBase extends MediaPlayerEventAdapter implements RenderCallback {

    protected DirectMediaPlayerComponentBase() {
    }

    // === RenderCallback =======================================================

    @Override
    public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) {
        // Default implementation does nothing, sub-classes should override this or provide their own implementation of
        // a RenderCallback
    }

}
