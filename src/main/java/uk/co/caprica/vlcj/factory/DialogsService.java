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

public final class DialogsService extends BaseService {

    DialogsService(MediaPlayerFactory factory) {
        super(factory);
    }

    public Dialogs newDialogs() {
        return new Dialogs();
    }

    public void enable(Dialogs dialogs) {
        enable(dialogs, null);
    }

    public void disable() {
        disable(null);
    }

    public void enable(Dialogs dialogs, Pointer userData) {
        libvlc.libvlc_dialog_set_callbacks(instance, dialogs.callbacks(), userData);
    }

    public void disable(Pointer userData) {
        libvlc.libvlc_dialog_set_callbacks(instance, null, userData);
    }

    public boolean postLogin(DialogId id, String username, String password, boolean storeCredentials) {
        return libvlc.libvlc_dialog_post_login(id.id(), username, password, storeCredentials ? 1 : 0) == 0;
    }

    public boolean postAction(DialogId id, int action) {
        return libvlc.libvlc_dialog_post_action(id.id(), action) == 0;
    }

    public boolean dismiss(DialogId id) {
        return libvlc.libvlc_dialog_dismiss(id.id()) == 0;
    }

}
