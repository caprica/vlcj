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

package uk.co.caprica.vlcj.media.callback.seekable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Implementation of seekable media that uses a {@link RandomAccessFile}.
 */
public class RandomAccessFileMedia extends SeekableCallbackMedia {

    /**
     * File to read media data from.
     */
    private final File file;

    /**
     * Random access file.
     */
    private RandomAccessFile randomAccessFile;

    /**
     * Create a media instance with a default IO buffer size.
     *
     * @param file file to read media data from
     */
    public RandomAccessFileMedia(File file) {
        super();
        this.file = file;
    }

    /**
     * Create a media instance.
     *
     * @param file file to read media data from
     * @param ioBufferSize IO buffer size
     */
    public RandomAccessFileMedia(File file, int ioBufferSize) {
        super(ioBufferSize);
        this.file = file;
    }

    @Override
    protected long onGetSize() {
        return file.length();
    }

    @Override
    protected boolean onOpen() {
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            return true;
        }
        catch (FileNotFoundException e) {
            return false;
        }
    }

    @Override
    protected int onRead(byte[] buffer, int bufferSize) throws IOException {
        return randomAccessFile.read(buffer, 0, bufferSize);
    }

    @Override
    protected boolean onSeek(long offset) {
        try {
            randomAccessFile.seek(offset);
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onClose() {
        try {
            randomAccessFile.close();
        }
        catch (IOException e) {
        }
    }

}
