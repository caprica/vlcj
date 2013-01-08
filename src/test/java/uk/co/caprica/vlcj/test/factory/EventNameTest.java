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

package uk.co.caprica.vlcj.test.factory;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Trivial test to dump out the native event names.
 */
public class EventNameTest extends VlcjTest {

    public static void main(String[] args) {
        LibVlc libvlc = LibVlc.INSTANCE;

        for(libvlc_event_e event : libvlc_event_e.values()) {
            int val = event.intValue();
            System.out.printf("%4d %04x %s%n", val, val, libvlc.libvlc_event_type_name(val));
        }
    }
}
