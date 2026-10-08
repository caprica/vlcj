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

import uk.co.caprica.vlcj.binding.internal.libvlc_parser_cbs;
import uk.co.caprica.vlcj.parser.events.ParserEventFactory;
import uk.co.caprica.vlcj.support.callback.NativeCallbackHandler;

import static uk.co.caprica.vlcj.parser.ParserStatus.parserStatus;

final class ParserCallbackHandler extends NativeCallbackHandler<Parser, libvlc_parser_cbs, ParserEventListener> {

    ParserCallbackHandler(Parser source) {
        super(source);
    }

    @Override
    protected libvlc_parser_cbs initCallbacks() {
        libvlc_parser_cbs cbs = new libvlc_parser_cbs();
        cbs.version = 0;

        cbs.on_parsed = (opaque, task, status) -> {
            raiseEvent(ParserEventFactory.createParserParsedEvent(
                source(),
                new ParserTask(task),
                parserStatus(status)
            ));
        };

        cbs.on_attachments_added = (opaque, task, attachments) -> {
            raiseEvent(ParserEventFactory.createParserAttachmentsAddedEvent(
                source(),
                new ParserTask(task),
                new Pictures(attachments)
            ));
        };

        return cbs;
    }
}
