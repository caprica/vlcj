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

import org.jspecify.annotations.Nullable;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_parser_cfg;
import uk.co.caprica.vlcj.binding.internal.libvlc_parser_request_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_parser_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_parser_task_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_thumbnailer_request_seek_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_thumbnailer_request_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_thumbnailer_seek_speed_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_thumbnailer_seek_type_t;
import uk.co.caprica.vlcj.media.Media;

import java.util.Optional;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_parser_cancel_request;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_parser_destroy;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_parser_new;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_parser_submit;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_parser_task_new_parse;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_parser_task_new_thumbnail;

public final class Parser {

    /**
     * Native parser instance.
     */
    private final libvlc_parser_t parserInstance;

    /**
     * Callback handler for native events.
     */
    private final ParserCallbackHandler parserCallbackHandler;

    /**
     * Callback handler for native events.
     */
    private final ThumbnailCallbackHandler thumbnailCallbackHandler;

    /**
     * Create a new parser.
     *
     * @param libvlcInstance native library instance
     * @param timeout parsing timeout, 0L for unlimited, -1L to inherit the native preparse-timeout value
     * @param maxParserThreads maximum number of threads used by the parser, 0 for default (1 thread)
     * @param maxThumbnailerThreads maximum number of threads used by the thumbnailer, 0 for default (1 thread)
     */
    public Parser(libvlc_instance_t libvlcInstance, long timeout, int maxParserThreads, int maxThumbnailerThreads) {
        this.parserCallbackHandler = new ParserCallbackHandler(this);
        this.thumbnailCallbackHandler = new ThumbnailCallbackHandler(this);

        libvlc_parser_cfg cfg = new libvlc_parser_cfg();
        cfg.version = 0;
        cfg.timeout = timeout;
        cfg.max_parser_threads = maxParserThreads;
        cfg.max_thumbnailer_threads = maxThumbnailerThreads;

        this.parserInstance = libvlc_parser_new(libvlcInstance, cfg);
    }

    /**
     * Create a new parser.
     *
     * @param libvlcInstance native library instance
     * @param maxParserThreads maximum number of threads used by the parser, 0 for default (1 thread)
     * @param maxThumbnailerThreads maximum number of threads used by the thumbnailer, 0 for default (1 thread)
     */
    public Parser(libvlc_instance_t libvlcInstance, int maxParserThreads, int maxThumbnailerThreads) {
        this(libvlcInstance, 0L, maxParserThreads, maxThumbnailerThreads);
    }

    /**
     * Create a new parser with a timeout.
     *
     * @param libvlcInstance native library instance
     * @param timeout parsing timeout, 0L for unlimited, -1L to inherit the native preparse-timeout value
     */
    public Parser(libvlc_instance_t libvlcInstance, long timeout) {
        this(libvlcInstance, timeout, 0, 0);
    }

    /**
     * Create a new parser with default configuration.
     *
     * @param libvlcInstance native library instance
     */
    public Parser(libvlc_instance_t libvlcInstance) {
        this(libvlcInstance, 0L, 0, 0);
    }

    public void addParserEventListener(ParserEventListener listener) {
        parserCallbackHandler.addEventListener(listener);
        thumbnailCallbackHandler.addEventListener(listener);
    }

    public void removeParserEventListener(ParserEventListener listener) {
        parserCallbackHandler.removeEventListener(listener);
        thumbnailCallbackHandler.removeEventListener(listener);
    }

    public Optional<ParserTask> newParseRequest(Media media, @Nullable MediaParseFlags mediaParseFlags) {
        libvlc_parser_request_t requestInstance = newParserRequest(media, mediaParseFlags);
        return queueParserRequest(requestInstance);
    }

    public Optional<ParserTask> newParseRequest(Media media) {
        return newParseRequest(media, null);
    }

    public Optional<ParserTask> newThumbnailRequest(Media media, ThumbnailRequest request) {
        libvlc_thumbnailer_request_t requestInstance = newThumbnailerRequest(media, request);
        requestInstance.seek.type = libvlc_thumbnailer_seek_type_t.libvlc_thumbnailer_seek_none.intValue();
        return queueThumbnailRequest(requestInstance);
    }

    public Optional<ParserTask> newThumbnailAtTimeRequest(Media media, ThumbnailRequest request, long time) {
        libvlc_thumbnailer_request_t requestInstance = newThumbnailerRequest(media, request);
        requestInstance.seek.type = libvlc_thumbnailer_seek_type_t.libvlc_thumbnailer_seek_time.intValue();
        requestInstance.seek.value.setType(long.class);
        requestInstance.seek.value.time = time;
        return queueThumbnailRequest(requestInstance);
    }

    public Optional<ParserTask> newThumbnailAtPositionRequest(Media media, ThumbnailRequest request, float position) {
        libvlc_thumbnailer_request_t requestInstance = newThumbnailerRequest(media, request);
        requestInstance.seek.type = libvlc_thumbnailer_seek_type_t.libvlc_thumbnailer_seek_pos.intValue();
        requestInstance.seek.value.setType(double.class);
        requestInstance.seek.value.position = position;
        return queueThumbnailRequest(requestInstance);
    }

    /**
     *
     *
     * The task must not have already been submitted, unless the previous attempt failed.
     *
     * @param parserTask
     * @return
     */
    public boolean submit(ParserTask parserTask) {
        int result = libvlc_parser_submit(parserInstance, parserTask.taskInstance);
        return result == 0;
    }

    public void cancel(ParserTask task) {
        libvlc_parser_cancel_request(parserInstance, task.taskInstance);
    }

    private libvlc_parser_request_t newParserRequest(Media media, @Nullable MediaParseFlags mediaParseFlags) {
        libvlc_parser_request_t requestInstance = new libvlc_parser_request_t();
        requestInstance.version = 0;
        requestInstance.media = media.mediaInstance();
        requestInstance.parse_flags = Optional.ofNullable(mediaParseFlags)
            .map(MediaParseFlags::get)
            .orElse(0);
        return requestInstance;
    }

    private libvlc_thumbnailer_request_t newThumbnailerRequest(Media media, ThumbnailRequest request) {
        libvlc_thumbnailer_request_t requestInstance = new libvlc_thumbnailer_request_t();
        requestInstance.version = 0;
        requestInstance.media = media.mediaInstance();
        requestInstance.width = request.width();
        requestInstance.height = request.height();
        requestInstance.crop = request.crop() ? 1 : 0;
        requestInstance.type = request.type().intValue();
        requestInstance.seek = new libvlc_thumbnailer_request_seek_t();
        if (request.fastSeek()) {
            requestInstance.seek.speed = libvlc_thumbnailer_seek_speed_t.libvlc_thumbnailer_seek_fast.intValue();
        } else {
            requestInstance.seek.speed = libvlc_thumbnailer_seek_speed_t.libvlc_thumbnailer_seek_precise.intValue();
        }
        requestInstance.hw_dec = request.hardwareDecoder() ? 1 : 0;
        return requestInstance;
    }

    private Optional<ParserTask> queueParserRequest(libvlc_parser_request_t requestInstance) {
        libvlc_parser_task_t task = libvlc_parser_task_new_parse(
            this.parserInstance,
            requestInstance,
            parserCallbackHandler.callbacks(),
            null
        );
        return Optional.ofNullable(task).map(ParserTask::new);
    }

    private Optional<ParserTask> queueThumbnailRequest(libvlc_thumbnailer_request_t requestInstance) {
        libvlc_parser_task_t task = libvlc_parser_task_new_thumbnail(
            this.parserInstance,
            requestInstance,
            thumbnailCallbackHandler.callbacks(),
            null
        );
        return Optional.ofNullable(task).map(ParserTask::new);
    }

    public void release() {
        libvlc_parser_destroy(this.parserInstance);
    }
}
