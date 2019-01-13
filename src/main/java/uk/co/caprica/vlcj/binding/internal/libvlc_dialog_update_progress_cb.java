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

package uk.co.caprica.vlcj.binding.internal;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

/**
 *
 */
public interface libvlc_dialog_update_progress_cb extends Callback {

    /**
     * Called when a progress dialog needs to be updated
     *
     * @param p_data opaque pointer for the callback
     * @param p_id id of the dialog
     * @param f_position osition of the progress bar (between 0.0 and 1.0)
     * @param psz_text new text of the progress dialog
     */
    void callback(Pointer p_data, libvlc_dialog_id p_id, float f_position, String psz_text);
}
