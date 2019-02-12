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
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.model.MediaRef;

/**
 * Encapsulation of a native media instance.
 */
public final class Media {

    /**
     * Native library.
     */
    protected final LibVlc libvlc;

    /**
     * Native media instance.
     */
    protected final libvlc_media_t mediaInstance;

    /**
     *
     */
    private final EventService    eventService;
    private final InfoService     infoService;
    private final MetaService     metaService;
    private final OptionsService  optionsService;
    private final ParseService    parseService;
    private final SlaveService    slaveService;
    private final SubitemService  subitemService;
    private final UserDataService userDataService;

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
    public Media(LibVlc libvlc, libvlc_media_t media) {
        this.libvlc        = libvlc;
        this.mediaInstance = media;

        this.eventService    = new EventService   (this);
        this.infoService     = new InfoService    (this);
        this.metaService     = new MetaService    (this);
        this.optionsService  = new OptionsService (this);
        this.parseService    = new ParseService   (this);
        this.slaveService    = new SlaveService   (this);
        this.subitemService  = new SubitemService (this);
        this.userDataService = new UserDataService(this);
    }

    public EventService events() {
        return eventService;
    }

    public InfoService info() {
        return infoService;
    }

    public MetaService meta() {
        return metaService;
    }

    public OptionsService options() {
        return optionsService;
    }

    public ParseService parsing() {
        return parseService;
    }

    public SlaveService slaves() {
        return slaveService;
    }

    public SubitemService subitems() {
        return subitemService;
    }

    public UserDataService userData() {
        return userDataService;
    }

    public libvlc_media_t mediaInstance() {
        return mediaInstance;
    }

    public MediaRef newMediaRef() {
        libvlc.libvlc_media_retain(mediaInstance);
        return new MediaRef(libvlc, mediaInstance);
    }

    /**
     *
     * <p>
     * The caller <em>must</em> release the returned {@link Media} when it has no further use for it.
     *
     * @return
     */
    public Media newMedia() {
        libvlc.libvlc_media_retain(mediaInstance);
        return new Media(libvlc, mediaInstance);
    }

    public void release() {
        eventService   .release();
        infoService    .release();
        optionsService .release();
        parseService   .release();
        metaService    .release();
        slaveService   .release();
        subitemService .release();
        userDataService.release();

        libvlc.libvlc_media_release(mediaInstance);
    }

}
