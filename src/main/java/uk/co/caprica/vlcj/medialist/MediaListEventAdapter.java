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

package uk.co.caprica.vlcj.medialist;

import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

/**
 * Default implementation of the media list event listener.
 * <p>
 * Simply override the methods you're interested in.
 * <p>
 * Events are likely <em>not</em> raised on the Swing Event Dispatch thread so if updating user
 * interface components in response to these events care must be taken to use
 * {@link SwingUtilities#invokeLater(Runnable)}.
 */
public class MediaListEventAdapter implements MediaListEventListener {

    @Override
    public void mediaListWillAddItem(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
    }

    @Override
    public void mediaListItemAdded(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
    }

    @Override
    public void mediaListWillDeleteItem(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
    }

    @Override
    public void mediaListItemDeleted(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
    }
}
