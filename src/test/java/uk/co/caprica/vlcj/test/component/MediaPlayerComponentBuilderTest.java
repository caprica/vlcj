package uk.co.caprica.vlcj.test.component;

import uk.co.caprica.vlcj.component.*;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;

import java.awt.*;

import static uk.co.caprica.vlcj.component.MediaPlayerComponentBuilder.mediaPlayerComponentBuilder;

public class MediaPlayerComponentBuilderTest {

    public static void main(String[] args) {

        mediaPlayerComponentBuilder()
                .embedded()
                .withFullScreenStrategy(null);

        mediaPlayerComponentBuilder()
                .withFactoryArgs("a", "b")
                .withExtraFactoryArgs("c", "d", "e")
                .embedded()
                .withVideoSurfaceComponent(new Canvas())
                .withFullScreenStrategy(null);

        EmbeddedMediaPlayerComponent embeddedMediaPlayerComponent = mediaPlayerComponentBuilder()
                .withFactory(new MediaPlayerFactory())
                .embedded()
                .withFullScreenStrategy(null)
                .withVideoSurfaceComponent(new Canvas())
                .embeddedMediaPlayerComponent();

        EmbeddedMediaListPlayerComponent embeddedMediaListPlayerComponent = mediaPlayerComponentBuilder()
                .withFactory(new MediaPlayerFactory())
                .embedded()
                .withFullScreenStrategy(null)
                .withVideoSurfaceComponent(new Canvas())
                .embeddedMediaListPlayerComponent();

        AudioMediaPlayerComponent audioMediaPlayerComponent = mediaPlayerComponentBuilder()
                .audio()
                .audioMediaPlayerComponent();

        AudioMediaListPlayerComponent audioMediaListPlayerComponent = mediaPlayerComponentBuilder()
                .audio()
                .audioMediaListPlayerComponent();

        DirectMediaPlayerComponent directMediaPlayerComponent = mediaPlayerComponentBuilder()
                .withFactory(new MediaPlayerFactory())
                .direct()
                .withFormat((BufferFormatCallback) null)
                .directMediaPlayerComponent();

        DirectAudioPlayerComponent directAudioPlayerComponent = mediaPlayerComponentBuilder()
                .withFactory(new MediaPlayerFactory())
                .direct()
                .withFormat("mp3", 192, 2)
                .withCallback(null)
                .directAudioPlayerComponent();

    }
}
