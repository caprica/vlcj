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

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

/**
 * Encapsulation of a native media instance.
 */
public final class Media {

    /**
     * Native library.
     */
    protected final LibVlc libvlc;

    /**
     * Native library instance.
     */
    protected final libvlc_instance_t libvlcInstance;

    /**
     * Native media instance.
     */
    protected final libvlc_media_t mediaInstance;

    private final EventService   eventService;
    private final InfoService    infoService;
    private final MetaService    metaService;
    private final OptionsService optionsService;
    private final ParseService   parseService;
    private final SlaveService   slaveService;
    private final SubitemService subitemService;

    /**
     * Create a new media item.
     * <p>
     * This component will "own" the supplied native media instance and will take care of releasing it during
     * {@link #release()}.
     * <p>
     * The caller should <em>not</em> release the native media instance.
     *
     * @param libvlc native library
     * @param media native media instance
     */
    public Media(LibVlc libvlc, libvlc_instance_t libvlcInstance, libvlc_media_t media) {
        this.libvlc         = libvlc;
        this.libvlcInstance = libvlcInstance;
        this.mediaInstance  = media;

        this.eventService    = new EventService  (this);
        this.infoService     = new InfoService   (this);
        this.metaService     = new MetaService   (this);
        this.optionsService  = new OptionsService(this);
        this.parseService    = new ParseService  (this);
        this.slaveService    = new SlaveService  (this);
        this.subitemService  = new SubitemService(this);
    }

    /**
     * Behaviour pertaining to events.
     *
     * @return event behaviour
     */
    public EventService events() {
        return eventService;
    }

    /**
     * Behaviour pertaining to information about the media.
     *
     * @return information behaviour
     */
    public InfoService info() {
        return infoService;
    }

    /**
     * Behaviour pertaining to media meta data.
     *
     * @return meta data behaviour
     */
    public MetaService meta() {
        return metaService;
    }

    /**
     * Behaviour pertaining to media options.
     *
     * @return meida options behaviour
     */
    public OptionsService options() {
        return optionsService;
    }

    /**
     * Behaviour pertaining to parsing of the media.
     *
     * @return parsing behaviour
     */
    public ParseService parsing() {
        return parseService;
    }

    /**
     * Behaviour pertaining to media slaves.
     *
     * @return media slave behaviour
     */
    public SlaveService slaves() {
        return slaveService;
    }

    /**
     * Behaviour pertianing to media subitems.
     *
     * @return subitem behaviour
     */
    public SubitemService subitems() {
        return subitemService;
    }

    /**
     * Create a new {@link MediaRef} from this media.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaRef} when it has no further use for it.
     *
     * @return media reference
     */
    public MediaRef newMediaRef() {
        libvlc.libvlc_media_retain(mediaInstance);
        return new MediaRef(libvlc, libvlcInstance, mediaInstance);
    }

    /**
     * Create a new {@link Media} from this media.
     * <p>
     * The caller <em>must</em> release the returned {@link Media} when it has no further use for it.
     *
     * @return media
     */
    public Media newMedia() {
        libvlc.libvlc_media_retain(mediaInstance);
        return new Media(libvlc, libvlcInstance, mediaInstance);
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
        return new MediaRef(libvlc, libvlcInstance, libvlc.libvlc_media_duplicate(mediaInstance));
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
        return new Media(libvlc, libvlcInstance, libvlc.libvlc_media_duplicate(mediaInstance));
    }

    /**
     * Release this component and the associated native resources.
     * <p>
     * The component must no longer be used.
     */
    public void release() {
        eventService  .release();
        infoService   .release();
        optionsService.release();
        parseService  .release();
        metaService   .release();
        slaveService  .release();
        subitemService.release();

        libvlc.libvlc_media_release(mediaInstance);
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
