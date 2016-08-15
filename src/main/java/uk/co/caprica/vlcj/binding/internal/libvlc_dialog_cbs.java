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

    public display_error pf_display_error;

    public display_login pf_display_login;

    public display_question pf_display_question;

    public display_progress pf_display_progress;

    public cancel pf_cancel;

    public update_progress pf_update_progress;

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }
}
