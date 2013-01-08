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

package uk.co.caprica.vlcj.binding.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * Native log subscriber handle.
 * <p>
 * The members of this structure should not be accessed from outside of the native
 * library.
 */
public class libvlc_log_subscriber_t extends Structure {

    /**
     *
     */
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList("prev", "next", "func", "opaque"));

    public static class ByReference extends libvlc_log_subscriber_t implements Structure.ByReference {}

    public libvlc_log_subscriber_t.ByReference prev;
    public libvlc_log_subscriber_t.ByReference next;
    public libvlc_log_cb func;
    public Pointer opaque;

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }
}
