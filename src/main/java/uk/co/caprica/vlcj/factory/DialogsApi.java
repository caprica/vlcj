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

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_dialog_dismiss;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_dialog_post_action;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_dialog_post_login;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_dialog_set_callbacks;

/**
 * Behaviour pertaining to native dialogs.
 */
public final class DialogsApi extends BaseApi {

    DialogsApi(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Create a new dialogs callback component.
     *
     * @param dialogTypes types of dialogs to enable, passing no types or <code>null</code> will enable all dialogs
     * @return dialogs callback component
     */
    public Dialogs newDialogs(DialogType... dialogTypes) {
        return new Dialogs(dialogTypes);
    }

    /**
     * Enable native dialog callbacks.
     *
     * @param dialogs dialogs callback component
     */
    public void enable(Dialogs dialogs) {
        enable(dialogs, null);
    }

    /**
     * Disable native dialog callbacks.
     */
    public void disable() {
        disable(null);
    }

    /**
     * Enable native dialog callbacks, with user data.
     *
     * @param dialogs dialogs callback component
     * @param userData opaque user data associated with each dialog
     */
    public void enable(Dialogs dialogs, Pointer userData) {
        libvlc_dialog_set_callbacks(libvlcInstance, dialogs.callbacks(), userData);
    }

    /**
     * Disable native dialog callbacks, with user data.
     *
     * @param userData opaque user data associated with each dialog
     */
    public void disable(Pointer userData) {
        libvlc_dialog_set_callbacks(libvlcInstance, null, userData);
    }

    /**
     * Post credentials to a native login dialog.
     *
     * @param id identifier of the dialog to post to
     * @param username username credential
     * @param password password credential
     * @param storeCredentials <code>true</code> if the user wants to store the credential; <code>false</code> otherwise
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean postLogin(DialogId id, String username, String password, boolean storeCredentials) {
        return libvlc_dialog_post_login(id.id(), username, password, storeCredentials ? 1 : 0) == 0;
    }

    /**
     * Post (select) an action to a native question dialog.
     *
     * @param id identifier of the dialog to post to
     * @param action action to post
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean postAction(DialogId id, int action) {
        return libvlc_dialog_post_action(id.id(), action) == 0;
    }

    /**
     * Dismiss a native dialog.
     *
     * @param id identifier of the dialog to dismiss
     * @return <code>true</code> if successful; <code>false</code> if error
     */
    public boolean dismiss(DialogId id) {
        return libvlc_dialog_dismiss(id.id()) == 0;
    }

}
