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

package uk.co.caprica.vlcj.media.callback.nonseekable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Media implementation that reads media data from a {@link FileInputStream}.
 */
public class FileInputStreamMedia extends NonSeekableInputStreamMedia {

    /**
     * File to read media data from.
     */
    private final File file;

    /**
     * Create a media instance with a default IO buffer size.
     *
     * @param file file to read media data from
     */
    public FileInputStreamMedia(File file) {
        super();
        this.file = file;
    }

    /**
     * Create a media instance.
     *
     * @param file file to read media data from
     * @param ioBufferSize IO buffer size
     */
    public FileInputStreamMedia(File file, int ioBufferSize) {
        super(ioBufferSize);
        this.file = file;
    }

    @Override
    protected InputStream onOpenStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    protected void onCloseStream(InputStream inputStream) throws IOException {
        inputStream.close();
    }

    @Override
    protected long onGetSize() {
        return file.length();
    }

}
