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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.parser.events;

import uk.co.caprica.vlcj.media.Picture;
import uk.co.caprica.vlcj.parser.Parser;
import uk.co.caprica.vlcj.parser.ParserEventListener;
import uk.co.caprica.vlcj.parser.ParserTask;

public final class ThumbnailerEndedEvent extends ParserEvent {

    private final ParserTask task;

    private final Picture picture;

    ThumbnailerEndedEvent(Parser parser, ParserTask task, Picture picture) {
        super(parser);
        this.task = task;
        this.picture = picture;
    }

    @Override
    public void notify(ParserEventListener listener) {
        listener.thumbnailerEnded(parser, task, picture);
    }
}
