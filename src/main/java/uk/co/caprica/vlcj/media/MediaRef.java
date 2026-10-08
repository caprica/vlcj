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

package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_duplicate;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_release;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_retain;

/**
 * An opaque reference to media.
 * <p>
 * This is used to pass around media references without requiring the full-blown {@link Media} component.
 */
public final class MediaRef {

    /**
     * Native media instance.
     */
    private final libvlc_media_t mediaInstance;

    /**
     * Create a new media reference.
     * <p>
     * The native media instance will be retained, increasing its internal reference count.
     * <p>
     * The caller <em>must</em> release this new media reference when it is of no further use.
     *
     * @param mediaInstance native media instance
     */
    public MediaRef(libvlc_media_t mediaInstance) {
        libvlc_media_retain(mediaInstance);
        this.mediaInstance = mediaInstance;
    }

    /**
     * Return a new {@link MediaRef} for this {@link MediaRef}.
     * <p>
     * The native media instance will be retained, increasing its internal reference count.
     * <p>
     * The returned media ref shares the native media instance with this any others that may be created.
     * <p>
     * The caller <em>must</em> release the new media reference when it is of no further use.
     *
     * @return media reference
     */
    public MediaRef newMediaRef() {
        return new MediaRef(mediaInstance);
    }

    /**
     * Return a duplicate {@link MediaRef} for this {@link MediaRef}.
     * <p>
     * Unlike {@link #newMediaRef()}, this function will duplicate the native media instance, meaning it is separate
     * from the native media instance in this component and any changes made to it (such as adding new media options)
     * will <em>not</em> be reflected on the original media.
     * <p>
     * The caller <em>must</em> release the duplicated media reference when it has no further use for it.
     *
     * @return duplicated media reference
     */
    public MediaRef duplicateMediaRef() {
        return new MediaRef(libvlc_media_duplicate(mediaInstance));
    }

    /**
     * Create a new {@link Media} component for this {@link MediaRef}.
     * <p>
     * The native media instance will be retained, increasing its internal reference count.
     * <p>
     * The returned media component shares the native media instance with this any others that may be created.
     * <p>
     * The caller <em>must</em> release the new media when it is of no further use.
     *
     * @return media
     */
    public Media newMedia() {
        return new Media(mediaInstance);
    }

    /**
     * Release this component and the associated native resources.
     * <p>
     * The component must no longer be used.
     */
    public void release() {
        libvlc_media_release(mediaInstance);
    }

    /**
     * Get the native media instance.
     *
     * @return media instance
     */
    public libvlc_media_t mediaInstance() {
        return mediaInstance;
    }
}
