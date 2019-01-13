package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.binding.internal.libvlc_dialog_id;

public final class DialogId {

    private final libvlc_dialog_id id;

    DialogId(libvlc_dialog_id id) {
        this.id = id;
    }

    libvlc_dialog_id id() {
        return id;
    }

}
