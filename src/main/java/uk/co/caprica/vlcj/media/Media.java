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
 * Encapsulation of a native media instance.
 */
public final class Media {

    /**
     * Native media instance.
     */
    private final libvlc_media_t mediaInstance;

    private final InfoApi    infoApi;
    private final MetaApi    metaApi;
    private final OptionsApi optionsApi;
    private final ParseApi   parseApi;
    private final SlaveApi   slaveApi;
    private final StatsApi   statsApi;
    private final SubitemApi subitemApi;
    private final TrackApi   trackApi;

    /**
     * Create a new media item.
     * <p>
     * The native media instance will be retained, increasing its internal reference count.
     * <p>
     * The caller <em>must</em> release this new media when it is of no further use.
     *
     * @param media native media instance
     */
    public Media(libvlc_media_t media) {
        libvlc_media_retain(media);

        this.mediaInstance  = media;

        this.infoApi    = new InfoApi   (this);
        this.metaApi    = new MetaApi   (this);
        this.optionsApi = new OptionsApi(this);
        this.parseApi   = new ParseApi  (this);
        this.slaveApi   = new SlaveApi  (this);
        this.statsApi   = new StatsApi  (this);
        this.subitemApi = new SubitemApi(this);
        this.trackApi   = new TrackApi  (this);
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
     * @return media options behaviour
     */
    public OptionsApi options() {
        return optionsApi;
    }

    /**
     * Behaviour pertaining to media parsed status.
     *
     * @return media parsed behaviour
     */
    public ParseApi parse() {
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
     * Behaviour pertaining to media stat values.
     * <p>
     * <em>Note that media stat values are only available in the specific case of sub-items discovered when playing or
     * parsing a directory.</em>
     *
     * @return media stats behaviour
     */
    public StatsApi stats() {
        return statsApi;
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
     * Behaviour pertaining to tracks.
     *
     * @return track behaviour
     */
    public TrackApi tracks() {
        return trackApi;
    }

    /**
     * Create a new {@link Media} from this media.
     * <p>
     * The native media instance will be retained, increasing its internal reference count.
     * <p>
     * The caller <em>must</em> release the new {@link Media} when it has no further use for it.
     *
     * @return media
     */
    public Media newMedia() {
        return new Media(mediaInstance);
    }

    /**
     * Return a duplicate {@link Media} component for this {@link MediaRef}.
     * <p>
     * Unlike {@link #newMedia()}, this function will duplicate the native media instance, meaning it is separate from
     * the native media instance in this component and any changes made to it (such as adding new media options) will
     * <em>not</em> be reflected on the original media.
     * <p>
     * The caller <em>must</em> release the duplicated media {@link Media} when it has no further use for it.
     *
     * @return duplicated media
     */
    public Media duplicateMedia() {
        return new Media(libvlc_media_duplicate(mediaInstance));
    }

    /**
     * Create a new {@link MediaRef} from this media.
     * <p>
     * The native media instance will be retained, increasing its internal reference count.
     * <p>
     * The caller <em>must</em> release the new {@link MediaRef} when it has no further use for it.
     *
     * @return media reference
     */
    public MediaRef newMediaRef() {
        return new MediaRef(mediaInstance);
    }

    /**
     * Release this component and the associated native resources.
     * <p>
     * The component must no longer be used.
     */
    public void release() {
        infoApi   .release();
        optionsApi.release();
        parseApi  .release();
        metaApi   .release();
        slaveApi  .release();
        statsApi  .release();
        subitemApi.release();
        trackApi  .release();

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
