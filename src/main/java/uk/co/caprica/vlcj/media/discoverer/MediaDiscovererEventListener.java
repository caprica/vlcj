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

package uk.co.caprica.vlcj.media.discoverer;

import uk.co.caprica.vlcj.media.MediaRef;

import javax.swing.SwingUtilities;

/**
 * Specification for a component that is interested in receiving event notifications from the media discoverer.
 * <p>
 * Events are <em>not</em> raised on the Swing Event Dispatch thread so if updating user interface components in
 * response to these events care must be taken to use {@link SwingUtilities#invokeLater(Runnable)}.
 * <p>
 * Equally, care must be taken not to call back into LibVLC from the event handling thread.
 * <p>
 * In the listener callback methods the provided media reference is valid <em>only for the duration of the listener
 * call</em>, if a permanent reference is needed then one of the following must be used:
 * <ul>
 *   <li>{@link MediaRef#newMediaRef()}</li>
 *   <li>{@link MediaRef#duplicateMediaRef()}</li>
 *   <li>{@link MediaRef#newMedia()}</li>
 * </ul>
 */
public interface MediaDiscovererEventListener {

    void mediaAdded(MediaDiscoverer mediaDiscoverer, MediaRef media);

    void mediaRemoved(MediaDiscoverer mediaDiscoverer, MediaRef media);
}
