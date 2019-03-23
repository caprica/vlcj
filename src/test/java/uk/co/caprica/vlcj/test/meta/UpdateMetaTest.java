/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.meta;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.test.VlcjTest;
import uk.co.caprica.vlcj.waiter.media.ParsedWaiter;

/**
 * Simple test to update local file meta data.
 * <p>
 * Specify a single local media file as the first (and only) command-line argument.
 */
public class UpdateMetaTest extends VlcjTest {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Specify a single MRL");
            System.exit(1);
        }

        // Create a media player
        MediaPlayerFactory factory = new MediaPlayerFactory();

        Media media = getParsedMedia(factory, args[0]);
        String originalDescription = media.meta().get(Meta.DESCRIPTION);
        System.out.println("originalDescription=" + originalDescription);

        media.meta().set(Meta.DESCRIPTION, "A bangin' choon.");
        media.meta().save();
        media.release();

        Media checkMedia = getParsedMedia(factory, args[0]);
        String checkDescription = checkMedia.meta().get(Meta.DESCRIPTION);
        System.out.println("checkDescription=" + checkDescription);

        checkMedia.meta().set(Meta.DESCRIPTION, originalDescription);
        checkMedia.meta().save();
        checkMedia.release();

        Media restoreMedia = getParsedMedia(factory, args[0]);
        String restoreDescription = restoreMedia.meta().get(Meta.DESCRIPTION);
        System.out.println("restoreDescription=" + restoreDescription);
        restoreMedia.release();

        factory.release();
    }

    private static Media getParsedMedia(MediaPlayerFactory factory, String mrl) throws Exception {
        final Media media = factory.media().newMedia(mrl);

        // Parsing is asynchronous, we use a conditional waiter to parse the media and wait for it to finish parsing
        ParsedWaiter parsed = new ParsedWaiter(media) {
            @Override
            protected boolean onBefore(Media component) {
                return media.parsing().parse();
            }
        };
        parsed.await();

        return media;
    }

}
