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

package uk.co.caprica.vlcj.test.factory;

import java.util.List;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.ModuleDescription;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple test to dump out audio/video filters.
 * <p>
 * Requires libvlc 1.2.x.
 */
public class FilterTest extends VlcjTest {

    private static final String FORMAT_PATTERN = "%3s %-24s %-24s %-80s %s\n";

    public static void main(String[] args) throws Exception {
        MediaPlayerFactory factory = new MediaPlayerFactory();

        List<ModuleDescription> audioFilters = factory.getAudioFilters();
        List<ModuleDescription> videoFilters = factory.getVideoFilters();

        System.out.println("Audio Filters:");
        System.out.println();

        System.out.printf(FORMAT_PATTERN, "#", "Name", "Short Name", "Long Name", "Help");
        System.out.printf(FORMAT_PATTERN, "=", "====", "==========", "=========", "====");
        for(int i = 0; i < audioFilters.size(); i ++ ) {
            dump(i, audioFilters.get(i));
        }

        System.out.println();

        System.out.println("Video Filters:");
        System.out.println();

        System.out.printf(FORMAT_PATTERN, "#", "Name", "Short Name", "Long Name", "Help");
        System.out.printf(FORMAT_PATTERN, "=", "====", "==========", "=========", "====");
        for(int i = 0; i < videoFilters.size(); i ++ ) {
            dump(i, videoFilters.get(i));
        }
    }

    private static void dump(int i, ModuleDescription moduleDescription) {
        System.out.printf(FORMAT_PATTERN, String.valueOf(i + 1), moduleDescription.name(), moduleDescription.shortName(), moduleDescription.longName(), formatHelp(moduleDescription.help()));
    }

    private static String formatHelp(String help) {
        return help != null ? help.replaceAll("\\n", " ") : "";
    }
}
