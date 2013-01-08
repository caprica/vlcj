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

package uk.co.caprica.vlcj.runtime.windows.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinUser.POINT;

/**
 *
 */
public class MSLLHOOKSTRUCT extends Structure {

    /**
     *
     */
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList("pt", "mouseData", "flags", "time", "dwExtraInfo"));

    public static class ByReference extends MSLLHOOKSTRUCT implements Structure.ByReference {};

    public POINT pt;
    public DWORD mouseData;
    public DWORD flags;
    public DWORD time;
    public ULONG_PTR dwExtraInfo;

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }
}
