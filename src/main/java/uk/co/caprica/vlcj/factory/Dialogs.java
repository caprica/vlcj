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
import uk.co.caprica.vlcj.binding.internal.*;
import uk.co.caprica.vlcj.enums.DialogQuestionType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Dialogs {

    private final libvlc_dialog_cbs callbacks = createCallbacks();

    private final List<DialogHandler> handlerList = new CopyOnWriteArrayList<DialogHandler>();

    Dialogs() {
    }

    public void addDialogHandler(DialogHandler handler) {
        handlerList.add(handler);
    }

    public void removeDialogHandler(DialogHandler handler) {
        handlerList.remove(handler);
    }

    libvlc_dialog_cbs callbacks() {
        return callbacks;
    }

    private libvlc_dialog_cbs createCallbacks() {
        libvlc_dialog_cbs callbacks = new libvlc_dialog_cbs();

        callbacks.pf_display_error    = new DisplayError();
        callbacks.pf_display_login    = new DisplayLogin();
        callbacks.pf_display_question = new DisplayQuestion();
        callbacks.pf_display_progress = new DisplayProgress();
        callbacks.pf_cancel           = new Cancel();
        callbacks.pf_update_progress  = new UpdateProgress();

        return callbacks;
    }

    private class DisplayError implements libvlc_dialog_display_error_cb {
        @Override
        public void callback(Pointer p_data, String psz_title, String psz_text) {
            onDisplayError(p_data, psz_title, psz_text);
        }
    }

    private class DisplayLogin implements libvlc_dialog_display_login_cb {
        @Override
        public void callback(Pointer p_data, libvlc_dialog_id p_id, String psz_title, String psz_text, String psz_default_username, int b_ask_store) {
            onDisplayLogin(p_data, dialogId(p_id), psz_title, psz_text, psz_default_username, b_ask_store != 0);
        }
    }

    private class DisplayQuestion implements libvlc_dialog_display_question_cb {
        @Override
        public void callback(Pointer p_data, libvlc_dialog_id p_id, String psz_title, String psz_text, int i_type, String psz_cancel, String psz_action1, String psz_action2) {
            onDisplayQuestion(p_data, dialogId(p_id), psz_title, psz_text, i_type, psz_cancel, psz_action1, psz_action2);
        }
    }

    private class DisplayProgress implements libvlc_dialog_display_progress_cb {
        @Override
        public void callback(Pointer p_data, libvlc_dialog_id p_id, String psz_title, String psz_text, int b_indeterminate, float f_position, String psz_cancel) {
            onDisplayProgress(p_data, dialogId(p_id), psz_title, psz_text, b_indeterminate, f_position, psz_cancel);
        }
    }

    private class Cancel implements libvlc_dialog_cancel_cb {
        @Override
        public void callback(Pointer p_data, libvlc_dialog_id p_id) {
            onCancel(p_data, dialogId(p_id));
        }
    }

    private class UpdateProgress implements libvlc_dialog_update_progress_cb {
        @Override
        public void callback(Pointer p_data, libvlc_dialog_id p_id, float f_position, String psz_text) {
            onUpdateProgress(p_data, dialogId(p_id), f_position, psz_text);
        }
    }

    private DialogId dialogId(libvlc_dialog_id id) {
        return new DialogId(id);
    }

    private void onDisplayError(Pointer userData, String title, String text) {
        for (DialogHandler handler : handlerList) {
            handler.displayError(userData, title, text);
        }
    }

    private void onDisplayLogin(Pointer userData, DialogId id, String title, String text, String defaultUsername, boolean askStore) {
        for (DialogHandler handler : handlerList) {
            handler.displayLogin(userData, id, title, text, defaultUsername, askStore);
        }
    }

    private void onDisplayQuestion(Pointer userData, DialogId id, String title, String text, int type, String cancel, String action1, String action2) {
        for (DialogHandler handler : handlerList) {
            handler.displayQuestion(userData, id, title, text, DialogQuestionType.questionType(type), cancel, action1, action2);
        }
    }

    private void onDisplayProgress(Pointer userData, DialogId id, String title, String text, int indeterminate, float position, String cancel) {
        for (DialogHandler handler : handlerList) {
            handler.displayProgress(userData, id, title, text, indeterminate, position, cancel);
        }
    }

    private void onCancel(Pointer userData, DialogId id) {
        for (DialogHandler handler : handlerList) {
            handler.cancel(userData, id);
        }
    }

    private void onUpdateProgress(Pointer userData, DialogId id, float position, String text) {
        for (DialogHandler handler : handlerList) {
            handler.updateProgress(userData, id, position, text);
        }
    }

}
