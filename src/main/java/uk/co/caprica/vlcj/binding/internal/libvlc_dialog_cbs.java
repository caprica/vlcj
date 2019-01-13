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
