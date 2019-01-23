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

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 */
public class libvlc_dialog_cbs extends Structure {

    /**
     *
     */
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList("pf_display_error", "pf_display_login", "pf_display_question", "pf_display_progress", "pf_cancel", "pf_update_progress"));

    public libvlc_dialog_display_error_cb pf_display_error;

    public libvlc_dialog_display_login_cb pf_display_login;

    public libvlc_dialog_display_question_cb pf_display_question;

    public libvlc_dialog_display_progress_cb pf_display_progress;

    public libvlc_dialog_cancel_cb pf_cancel;

    public libvlc_dialog_update_progress_cb pf_update_progress;

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }

}
