package uk.co.caprica.vlcj.binding.internal;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum libvlc_dialog_question_type {

    LIBVLC_DIALOG_QUESTION_NORMAL  (0),
    LIBVLC_DIALOG_QUESTION_WARNING (1),
    LIBVLC_DIALOG_QUESTION_CRITICAL(2);

    private static final Map<Integer, libvlc_dialog_question_type> INT_MAP = new HashMap<Integer, libvlc_dialog_question_type>();

    static {
        for(libvlc_dialog_question_type type : libvlc_dialog_question_type.values()) {
            INT_MAP.put(type.intValue, type);
        }
    }

    public static libvlc_dialog_question_type dialogQuestionType(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    private libvlc_dialog_question_type(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
