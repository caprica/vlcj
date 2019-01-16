package uk.co.caprica.vlcj.media;

// there is native api for this, but we don't need it

// maybe the tracks etc stuff in DefaultMEdiaPlayer should be in here!

public class UserDataService extends BaseService {

    private Object userData;

    UserDataService(Media media) {
        super(media);
    }

    public Object get() {
        return userData;
    }

    public void set(Object userData) {
        this.userData = userData;
    }

}
