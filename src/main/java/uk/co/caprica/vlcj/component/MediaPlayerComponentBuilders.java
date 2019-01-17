package uk.co.caprica.vlcj.component;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.directaudio.AudioCallback;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;

import java.awt.*;

// FIXME some of the interface names are a bit awkward, maybe some better names can be found

/**
 * Specification for a type-safe builder for creating media player components.
 * <p>
 * @see MediaPlayerComponentBuilder
 */
public interface MediaPlayerComponentBuilders {

    interface Factory extends FactoryArgs, WithFactory {
    }

    interface FactoryArgs extends MediaPlayers {
        FactoryArgs withFactoryArgs(String... factoryArgs);
        FactoryArgs withExtraFactoryArgs(String... extraFactoryArgs);
    }

    interface WithFactory {
        MediaPlayers withFactory(MediaPlayerFactory factory);
    }

    interface MediaPlayers {
        Embedded embedded();
        Audio audio();
        Direct direct();
    }

    interface Embedded {
        Embedded withFullScreenStrategy(FullScreenStrategy fullScreenStrategy);
        Embedded withVideoSurfaceComponent(Component videoSurfaceComponent);
        Embedded withOverlay(Window overlay);
        Embedded withInputEvents(InputEvents inputEvents);
        EmbeddedMediaPlayerComponent embeddedMediaPlayerComponent();
        EmbeddedMediaListPlayerComponent embeddedMediaListPlayerComponent();
    }

    interface Audio {
        AudioMediaPlayerComponent audioMediaPlayerComponent();
        AudioMediaListPlayerComponent audioMediaListPlayerComponent();
    }

    interface Direct extends VideoFormat, AudioFormat {
    }

    interface VideoFormat {
        DirectVideo withFormat(BufferFormatCallback bufferFormatCallback);
    }

    interface AudioFormat {
        DirectAudio withFormat(String format, int rate, int channels);
    }

    interface DirectVideo {
        DirectVideo withCallback(RenderCallback renderCallback);
        DirectMediaPlayerComponent directMediaPlayerComponent();
    }

    interface DirectAudio {
        DirectAudio withCallback(AudioCallback audioCallback);
        DirectAudioPlayerComponent directAudioPlayerComponent();
    }

}
