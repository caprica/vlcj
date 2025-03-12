package uk.co.caprica.vlcj.test.media;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.MediaParsedStatus;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.support.Info;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParseMediaTest {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Specify MRL");
            System.exit(1);
        }

        System.out.printf("vlcj version: %s%n", Info.getInstance().vlcjVersion());

        MediaPlayerFactory mpf = new MediaPlayerFactory("--quiet");
        Media media = mpf.media().newMedia(args[0]);

        CountDownLatch latch = new CountDownLatch(1);

        AtomicBoolean parsed = new AtomicBoolean(false);

        media.events().addMediaEventListener(new MediaEventAdapter() {
            @Override
            public void mediaParsedChanged(Media media, MediaParsedStatus newStatus) {
                System.out.printf("Parsed changed: %s%n", newStatus);
                switch (newStatus) {
                    case SKIPPED:
                    case FAILED:
                    case TIMEOUT:
                        latch.countDown();
                        break;
                    case DONE:
                        parsed.set(true);
                        latch.countDown();
                        break;
                }
            }
        });

        media.parsing().parse();

        latch.await();

        if (!parsed.get()) {
            System.out.println("Failed to parse");
            System.exit(0);
        }

        MetaData md = media.meta().asMetaData();
        System.out.printf("Parsed: %s%n", md);
    }
}
