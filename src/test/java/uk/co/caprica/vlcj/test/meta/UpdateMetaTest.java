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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.meta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple test to update local file meta data.
 * <p>
 * Specify a single local media file as the first (and only) command-line argument.
 */
public class UpdateMetaTest extends VlcjTest {

    /**
     * Log.
     */
    private static final Logger logger = LoggerFactory.getLogger(UpdateMetaTest.class);

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Specify a single MRL");
            System.exit(1);
        }

        // Create a media player
        MediaPlayerFactory factory = new MediaPlayerFactory();

        // Get the meta data and dump it out
        MediaMeta mediaMeta = factory.getMediaMeta(args[0], true);
        logger.info("mediaMeta={}", mediaMeta);
        logger.info("original description={}", mediaMeta.getDescription());

        // Keep the original description to restore it later
        String originalDescription = mediaMeta.getDescription();

        // Write new meta data
        mediaMeta.setDescription("Oh isn't this a lovely tune.");
        mediaMeta.save();

        mediaMeta.release();

        // Re-read to confirm the updated value
        mediaMeta = factory.getMediaMeta(args[0], true);
        logger.info("mediaMeta={}", mediaMeta);
        logger.info("updated description={}", mediaMeta.getDescription());

        // Restore the original description
        mediaMeta.setDescription(originalDescription);
        mediaMeta.save();

        mediaMeta.release();

        // Re-read to confirm
        mediaMeta = factory.getMediaMeta(args[0], true);
        logger.info("mediaMeta={}", mediaMeta);
        logger.info("restored description={}", mediaMeta.getDescription());

        mediaMeta.release();

        // Orderly clean-up
        factory.release();
    }
}
