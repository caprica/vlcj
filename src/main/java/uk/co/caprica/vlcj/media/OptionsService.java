package uk.co.caprica.vlcj.media;

// FIXME
// adding an option with no flags grants it TRUSTED and UNIQUE
//  (There are no other flags)
//
// if you do unique, it will REPLACE an existing option
// if you do not do unqiue, it ADDs to the existing option
//
// what that means in practical terms i don't know.
//
// generally we have not bothered with flags
//  so i think we just won't implement it

public class OptionsService extends BaseService {

    private Object userData;

    OptionsService(Media media) {
        super(media);
    }

    public boolean addOptions(String... options) {
        return MediaOptions.addMediaOptions(libvlc, mediaInstance, options);
    }

}
