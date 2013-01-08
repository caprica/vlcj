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

package uk.co.caprica.vlcj.binding;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.IntByReference;

/**
 * JNA interface to the native LibDwm (Desktop Window Manager) library.
 * <p>
 * This library is only available on Windows platforms.
 * <p>
 * The functionality provided by this library may be helpful especially when working with
 * full-screen exclusive mode in Java which suffers from numerous idiosyncrasies on Windows, some of
 * which can be mitigated by ensuring that desktop window composition is disabled. Disabling
 * composition can be done at the operating system level, but is more convenient for end-users if
 * the application itself does it.
 * <p>
 * <strong>This class may be removed in future versions of vlcj due to it's uselessness.</strong>
 */
public interface LibDwmApi extends Library {

    /**
     * Native library instance.
     */
    LibDwmApi INSTANCE = (LibDwmApi)Native.loadLibrary("dwmapi", LibDwmApi.class);

    /**
     * Disable composition.
     */
    int DWM_EC_DISABLECOMPOSITION = 0;

    /**
     * Enable composition.
     */
    int DWM_EC_ENABLECOMPOSITION = 1;

    /**
     * API success code.
     */
    int S_OK = 0;

    /**
     * Enable/disable desktop window composition.
     *
     * @param uCompositionAction enable/disable, i.e. DWM_EC_ENABLECOMPOSITION or DWM_EC_DISABLECOMPOSITION
     * @return S_OK or HRESULT error code
     */
    HRESULT DwmEnableComposition(int uCompositionAction);

    /**
     * Check whether or not desktop window composition is currently enabled.
     *
     * @param pfEnabled pointer to enabled/disabled flag.
     * @return S_OK or HRESULT error code
     */
    HRESULT DwmIsCompositionEnabled(IntByReference pfEnabled);
}
