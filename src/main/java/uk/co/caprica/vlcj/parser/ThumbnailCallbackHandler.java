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

package uk.co.caprica.vlcj.parser;

import uk.co.caprica.vlcj.binding.internal.libvlc_thumbnailer_cbs;
import uk.co.caprica.vlcj.media.Picture;
import uk.co.caprica.vlcj.support.callback.NativeCallbackHandler;

import static uk.co.caprica.vlcj.parser.events.ParserEventFactory.createThumbnailerEndedEvent;

final class ThumbnailCallbackHandler extends NativeCallbackHandler<Parser, libvlc_thumbnailer_cbs, ParserEventListener> {

    ThumbnailCallbackHandler(Parser source) {
        super(source);
    }

    @Override
    protected libvlc_thumbnailer_cbs initCallbacks() {
        libvlc_thumbnailer_cbs cbs = new libvlc_thumbnailer_cbs();
        cbs.version = 0;

        cbs.on_ended = (opaque, task, picture) -> {
            raiseEvent(createThumbnailerEndedEvent(
                source(),
                new ParserTask(task),
                new Picture(picture)
            ));
        };

        return cbs;
    }
}
