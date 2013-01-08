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

package uk.co.caprica.vlcj.filter.swing;

import java.io.File;

/**
 * A bridge between regular file filters and Swing file filters.
 * <p>
 * This file filter always accepts directories.
 */
public class SwingFileFilter extends javax.swing.filechooser.FileFilter {

    /**
     * Description of the filter.
     */
    private final String description;

    /**
     * The regular file filter implementation.
     */
    private final java.io.FileFilter delegate;

    /**
     * Create a Swing file filter.
     *
     * @param description
     * @param delegate
     */
    public SwingFileFilter(String description, java.io.FileFilter delegate) {
        this.description = description;
        this.delegate = delegate;
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || delegate.accept(f);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
