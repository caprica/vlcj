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

package uk.co.caprica.vlcj.support.eventmanager;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.media.MediaRef;

/**
 * Base implementation for an event.
 *
 * @param <C> type of component the event relates to
 * @param <L> type of event notification listener
 */
abstract public class BaseEvent<C, L> implements EventNotification<L> {

    /**
     * Native library instance.
     */
    protected final libvlc_instance_t libvlcInstance;

    /**
     * Component the event relates to.
     */
    protected final C component;

    /**
     * Create an event.
     *
     * @param libvlcInstance native library instance
     * @param component component the event relates to
     */
    protected BaseEvent(libvlc_instance_t libvlcInstance, C component) {
        this.libvlcInstance = libvlcInstance;
        this.component = component;
    }

    /**
     * Create a temporary media reference to wrap the native media handle.
     * <p>
     * This is used so as not to pass a native binding type on various listener methods.
     * <p>
     * Returning this temporary reference does <em>not</em> cause the native media instance to be retained - there is no
     * need to do so since the reference only exists for the lifetime of the native callback, and since it is not
     * retained, there is no need to release it either.
     * <p>
     * If a client application needs to keep the {@link MediaRef} it must use either {@link MediaRef#newMediaRef()} or
     * {@link MediaRef#newMedia()}.
     *
     * @param mediaInstance native media instance
     * @return temporary media reference, which must <em>not</em> be released
     */
    protected final MediaRef temporaryMediaRef(libvlc_media_t mediaInstance) {
        return new MediaRef(libvlcInstance, mediaInstance);
    }

}
