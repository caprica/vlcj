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

package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_duplicate;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_retain;

/**
 * Encapsulation of a native media instance.
 */
public final class Media {

    /**
     * Native library instance.
     */
    protected final libvlc_instance_t libvlcInstance;

    /**
     * Native media instance.
     */
    protected final libvlc_media_t mediaInstance;

    private final EventApi   eventApi;
    private final InfoApi    infoApi;
    private final MetaApi    metaApi;
    private final OptionsApi optionsApi;
    private final ParseApi   parseApi;
    private final SlaveApi   slaveApi;
    private final SubitemApi subitemApi;

    /**
     * Create a new media item.
     * <p>
     * This component will "own" the supplied native media instance and will take care of releasing it during
     * {@link #release()}.
     * <p>
     * The caller should <em>not</em> release the native media instance.
     *
     * @param libvlcInstance native library instance
     * @param media native media instance
     */
    public Media(libvlc_instance_t libvlcInstance, libvlc_media_t media) {
        this.libvlcInstance = libvlcInstance;
        this.mediaInstance  = media;

        this.eventApi    = new EventApi  (this);
        this.infoApi     = new InfoApi   (this);
        this.metaApi     = new MetaApi   (this);
        this.optionsApi  = new OptionsApi(this);
        this.parseApi    = new ParseApi  (this);
        this.slaveApi    = new SlaveApi  (this);
        this.subitemApi  = new SubitemApi(this);
    }

    /**
     * Behaviour pertaining to events.
     *
     * @return event behaviour
     */
    public EventApi events() {
        return eventApi;
    }

    /**
     * Behaviour pertaining to information about the media.
     *
     * @return information behaviour
     */
    public InfoApi info() {
        return infoApi;
    }

    /**
     * Behaviour pertaining to media meta data.
     *
     * @return meta data behaviour
     */
    public MetaApi meta() {
        return metaApi;
    }

    /**
     * Behaviour pertaining to media options.
     *
     * @return meida options behaviour
     */
    public OptionsApi options() {
        return optionsApi;
    }

    /**
     * Behaviour pertaining to parsing of the media.
     *
     * @return parsing behaviour
     */
    public ParseApi parsing() {
        return parseApi;
    }

    /**
     * Behaviour pertaining to media slaves.
     *
     * @return media slave behaviour
     */
    public SlaveApi slaves() {
        return slaveApi;
    }

    /**
     * Behaviour pertianing to media subitems.
     *
     * @return subitem behaviour
     */
    public SubitemApi subitems() {
        return subitemApi;
    }

    /**
     * Create a new {@link MediaRef} from this media.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaRef} when it has no further use for it.
     *
     * @return media reference
     */
    public MediaRef newMediaRef() {
        libvlc_media_retain(mediaInstance);
        return new MediaRef(libvlcInstance, mediaInstance);
    }

    /**
     * Create a new {@link Media} from this media.
     * <p>
     * The caller <em>must</em> release the returned {@link Media} when it has no further use for it.
     *
     * @return media
     */
    public Media newMedia() {
        libvlc_media_retain(mediaInstance);
        return new Media(libvlcInstance, mediaInstance);
    }


    /**
     * Return a duplicate {@link MediaRef} for this {@link MediaRef}.
     * <p>
     * Unlike {@link #newMediaRef()}, this function will duplicate the native media instance, meaning it is separate
     * from the native media instance in this component and any changes made to it (such as adding new media options)
     * will <em>not</em> be reflected on the original media.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaRef} when it has no further use for it.
     *
     * @return duplicated media reference
     */
    public MediaRef duplicateMediaRef() {
        return new MediaRef(libvlcInstance, libvlc_media_duplicate(mediaInstance));
    }

    /**
     * Return a duplicate {@link Media} component for this {@link MediaRef}.
     * <p>
     * Unlike {@link #newMedia()}, this function will duplicate the native media instance, meaning it is separate from
     * the native media instance in this component and any changes made to it (such as adding new media options) will
     * <em>not</em> be reflected on the original media.
     * <p>
     * The caller <em>must</em> release the returned {@link Media} when it has no further use for it.
     *
     * @return duplicated media
     */
    public Media duplicateMedia() {
        return new Media(libvlcInstance, libvlc_media_duplicate(mediaInstance));
    }

    /**
     * Release this component and the associated native resources.
     * <p>
     * The component must no longer be used.
     */
    public void release() {
        eventApi  .release();
        infoApi   .release();
        optionsApi.release();
        parseApi  .release();
        metaApi   .release();
        slaveApi  .release();
        subitemApi.release();

        libvlc_media_release(mediaInstance);
    }

    /**
     * Get the associated native media instance.
     *
     * @return media instance
     */
    public libvlc_media_t mediaInstance() {
        return mediaInstance;
    }

}
