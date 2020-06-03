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

package uk.co.caprica.vlcj.factory;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.internal.libvlc_dialog_cancel_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_dialog_cbs;
import uk.co.caprica.vlcj.binding.internal.libvlc_dialog_display_error_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_dialog_display_login_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_dialog_display_progress_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_dialog_display_question_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_dialog_id;
import uk.co.caprica.vlcj.binding.internal.libvlc_dialog_update_progress_cb;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Encapsulation of native dialog callbacks.
 * <p>
 * Essentially this component bridges native dialog callbacks to methods on a {@link DialogHandler} implementation
 */
public final class Dialogs {

    private final libvlc_dialog_cbs callbacks;

    private final List<DialogHandler> handlerList = new CopyOnWriteArrayList<DialogHandler>();

    Dialogs(DialogType... dialogTypes) {
        this.callbacks = createCallbacks(dialogTypes);
    }

    /**
     * Add a handler to process the dialog callbacks.
     *
     * @param handler handler
     */
    public void addDialogHandler(DialogHandler handler) {
        handlerList.add(handler);
    }

    /**
     * Remove a handler that was processing dialog callbacks.
     *
     * @param handler handler
     */
    public void removeDialogHandler(DialogHandler handler) {
        handlerList.remove(handler);
    }

    libvlc_dialog_cbs callbacks() {
        return callbacks;
    }

    private libvlc_dialog_cbs createCallbacks(DialogType... dialogTypes) {
        libvlc_dialog_cbs callbacks = new libvlc_dialog_cbs();

        Set<DialogType> enableTypes = new HashSet<DialogType>();
        Collections.addAll(enableTypes, dialogTypes != null && dialogTypes.length > 0 ? dialogTypes : DialogType.values());

        callbacks.pf_display_error    = enableTypes.contains(DialogType.ERROR   ) ? new DisplayError()    : null;
        callbacks.pf_display_login    = enableTypes.contains(DialogType.LOGIN   ) ? new DisplayLogin()    : null;
        callbacks.pf_display_question = enableTypes.contains(DialogType.QUESTION) ? new DisplayQuestion() : null;
        callbacks.pf_display_progress = enableTypes.contains(DialogType.PROGRESS) ? new DisplayProgress() : null;
        callbacks.pf_update_progress  = enableTypes.contains(DialogType.PROGRESS) ? new UpdateProgress()  : null;
        callbacks.pf_cancel           = new Cancel();

        return callbacks;
    }

    private class DisplayError implements libvlc_dialog_display_error_cb {
        @Override
        public void callback(Pointer p_data, String psz_title, String psz_text) {
            onDisplayError(userData(p_data), psz_title, psz_text);
        }
    }

    private class DisplayLogin implements libvlc_dialog_display_login_cb {
        @Override
        public void callback(Pointer p_data, libvlc_dialog_id p_id, String psz_title, String psz_text, String psz_default_username, int b_ask_store) {
            onDisplayLogin(userData(p_data), dialogId(p_id), psz_title, psz_text, psz_default_username, b_ask_store != 0);
        }
    }

    private class DisplayQuestion implements libvlc_dialog_display_question_cb {
        @Override
        public void callback(Pointer p_data, libvlc_dialog_id p_id, String psz_title, String psz_text, int i_type, String psz_cancel, String psz_action1, String psz_action2) {
            onDisplayQuestion(userData(p_data), dialogId(p_id), psz_title, psz_text, i_type, psz_cancel, psz_action1, psz_action2);
        }
    }

    private class DisplayProgress implements libvlc_dialog_display_progress_cb {
        @Override
        public void callback(Pointer p_data, libvlc_dialog_id p_id, String psz_title, String psz_text, int b_indeterminate, float f_position, String psz_cancel) {
            onDisplayProgress(userData(p_data), dialogId(p_id), psz_title, psz_text, b_indeterminate, f_position, psz_cancel);
        }
    }

    private class Cancel implements libvlc_dialog_cancel_cb {
        @Override
        public void callback(Pointer p_data, libvlc_dialog_id p_id) {
            onCancel(userData(p_data), dialogId(p_id));
        }
    }

    private class UpdateProgress implements libvlc_dialog_update_progress_cb {
        @Override
        public void callback(Pointer p_data, libvlc_dialog_id p_id, float f_position, String psz_text) {
            onUpdateProgress(userData(p_data), dialogId(p_id), f_position, psz_text);
        }
    }

    private Long userData(Pointer pointer) {
        return pointer != null ? Pointer.nativeValue(pointer) : null;
    }

    private DialogId dialogId(libvlc_dialog_id id) {
        return new DialogId(id);
    }

    private void onDisplayError(Long userData, String title, String text) {
        for (DialogHandler handler : handlerList) {
            handler.displayError(userData, title, text);
        }
    }

    private void onDisplayLogin(Long userData, DialogId id, String title, String text, String defaultUsername, boolean askStore) {
        for (DialogHandler handler : handlerList) {
            handler.displayLogin(userData, id, title, text, defaultUsername, askStore);
        }
    }

    private void onDisplayQuestion(Long userData, DialogId id, String title, String text, int type, String cancel, String action1, String action2) {
        for (DialogHandler handler : handlerList) {
            handler.displayQuestion(userData, id, title, text, DialogQuestionType.questionType(type), cancel, action1, action2);
        }
    }

    private void onDisplayProgress(Long userData, DialogId id, String title, String text, int indeterminate, float position, String cancel) {
        for (DialogHandler handler : handlerList) {
            handler.displayProgress(userData, id, title, text, indeterminate, position, cancel);
        }
    }

    private void onCancel(Long userData, DialogId id) {
        for (DialogHandler handler : handlerList) {
            handler.cancel(userData, id);
        }
    }

    private void onUpdateProgress(Long userData, DialogId id, float position, String text) {
        for (DialogHandler handler : handlerList) {
            handler.updateProgress(userData, id, position, text);
        }
    }

}
