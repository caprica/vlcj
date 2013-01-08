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

import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HOOKPROC;

/**
 * Specification for a Windows Low Level Mouse Procedure.
 */
public interface LowLevelMouseProc extends HOOKPROC {

    /**
     * Windows message codes.
     */
    int WM_MOUSEMOVE = 512;
    int WM_LBUTTONDOWN = 513;
    int WM_LBUTTONUP = 514;
    int WM_RBUTTONDOWN = 516;
    int WM_RBUTTONUP = 517;
    int WM_MBUTTONDOWN = 519;
    int WM_MBUTTONUP = 520;
    int WM_MOUSEWHEEL = 522;

    /**
     * Call-back.
     *
     * @param nCode message code
     * @param wParam message parameter
     * @param lParam message parameter
     * @return call-back result
     */
    LRESULT callback(int nCode, WPARAM wParam, MSLLHOOKSTRUCT lParam);
}
