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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.meta;

import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple test to update local file meta data.
 * <p>
 * Specify a single local media file as the first (and only) command-line argument.
 */
public class UpdateMetaTest extends VlcjTest {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Specify a single MRL");
            System.exit(1);
        }

        Logger.setLevel(Logger.Level.INFO);

        // Create a media player
        MediaPlayerFactory factory = new MediaPlayerFactory();

        // Get the meta data and dump it out
        MediaMeta mediaMeta = factory.getMediaMeta(args[0], true);
        Logger.info("mediaMeta={}", mediaMeta);
        Logger.info("original description={}", mediaMeta.getDescription());

        // Keep the original description to restore it later
        String originalDescription = mediaMeta.getDescription();

        // Write new meta data
        mediaMeta.setDescription("Oh isn't this a lovely tune.");
        mediaMeta.save();

        mediaMeta.release();

        // Re-read to confirm the updated value
        mediaMeta = factory.getMediaMeta(args[0], true);
        Logger.info("mediaMeta={}", mediaMeta);
        Logger.info("updated description={}", mediaMeta.getDescription());

        // Restore the original description
        mediaMeta.setDescription(originalDescription);
        mediaMeta.save();

        mediaMeta.release();

        // Re-read to confirm
        mediaMeta = factory.getMediaMeta(args[0], true);
        Logger.info("mediaMeta={}", mediaMeta);
        Logger.info("restored description={}", mediaMeta.getDescription());

        mediaMeta.release();

        // Orderly clean-up
        factory.release();
    }
}
