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

package uk.co.caprica.vlcj.media.callback.seekable;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Implementation of seekable callback media for files.
 * <p>
 * This class is essentially a means to get a {@link MappedByteBuffer} from a local file or path.
 * <p>
 * Note that even though the underlying buffer is seekable, not all media types support seeking via this API.
 * <p>
 * Operations on a {@link MappedByteBuffer} are limited to int values by {@link MappedByteBuffer#position(int)}, whereas
 * {@link #onSeek(long)} takes a long.
 * <p>
 * That means this media can not support file sizes greater than {@link Integer#MAX_VALUE}.
 * <p>
 * A custom implementation could support long-based operations by encapsulating multiple {@link MappedByteBuffer}
 * instances at various offsets within the file.
 */
public class FileMappedByteBufferCallbackMedia extends MappedByteBufferCallbackMedia {

    /**
     * Local file path.
     */
    private final Path path;

    /**
     * File channel.
     */
    private FileChannel fileChannel;

    /**
     * Create callback media.
     *
     * @param path file path
     */
    public FileMappedByteBufferCallbackMedia(Path path) {
        this.path = path;
    }

    /**
     * Create callback media.
     *
     * @param file file
     */
    public FileMappedByteBufferCallbackMedia(File file) {
        this(file.toPath());
    }

    /**
     * Create callback media.
     *
     * @param filename file name
     */
    public FileMappedByteBufferCallbackMedia(String filename) {
        this(Paths.get(filename));
    }

    @Override
    protected MappedByteBuffer getBuffer() {
        try {
            fileChannel = (FileChannel) Files.newByteChannel(path, StandardOpenOption.READ);
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onClose() {
        try {
            fileChannel.close();
        } catch (IOException e) {
        }
    }
}
