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
