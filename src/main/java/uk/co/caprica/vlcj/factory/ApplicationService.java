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

package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.enums.TrackType;
import uk.co.caprica.vlcj.log.NativeLog;

public final class ApplicationService extends BaseService {

    ApplicationService(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Get the libvlc version.
     *
     * @return native library version
     */
    public String version() {
        return libvlc.libvlc_get_version();
    }

    /**
     * Get the compiler used to build libvlc.
     *
     * @return compiler
     */
    public String compiler() {
        return libvlc.libvlc_get_compiler();
    }

    /**
     * Get the source code change-set id used to build libvlc.
     *
     * @return change-set
     */
    public String changeset() {
        return libvlc.libvlc_get_changeset();
    }

    /**
     * Set the application name.
     *
     * @param userAgent application name
     */
    public void setUserAgent(String userAgent) {
        setUserAgent(userAgent, null);
    }

    /**
     * Set the application name.
     *
     * @param userAgent application name
     * @param httpUserAgent application name for HTTP
     */
    public void setUserAgent(String userAgent, String httpUserAgent) {
        libvlc.libvlc_set_user_agent(libvlcInstance, userAgent, httpUserAgent);
    }

    /**
     * Set the application identification information.
     *
     * @param id application id, e.g. com.somecompany.myapp
     * @param version application version
     * @param icon path to application icon
     */
    public void setApplicationId(String id, String version, String icon) {
        libvlc.libvlc_set_app_id(libvlcInstance, id, version, icon);
    }

    /**
     * Create a new native log component.
     *
     * @return native log component, or <code>null</code> if the native log is not available
     */
    public NativeLog newLog() {
        return new NativeLog(libvlc, libvlcInstance);
    }

    /**
     * Get the time as defined by LibVLC.
     * <p>
     * The time is not meaningful in the sense of what time is it, rather it is a monotonic clock
     * with an arbitrary starting value.
     *
     * @return current clock time value, in microseconds
     */
    public long clock() {
        return libvlc.libvlc_clock();
    }


    /**
     * Get a description for a particular codec value.
     *
     * @param type type of track
     * @param codec codec value (or codec FourCC)
     * @return codec description
     */
    public String codecDescription(TrackType type, int codec) {
        return libvlc.libvlc_media_get_codec_description(type.intValue(), codec);
    }

}
