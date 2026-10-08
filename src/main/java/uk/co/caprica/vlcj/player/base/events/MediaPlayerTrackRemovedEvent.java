package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

public class MediaPlayerTrackRemovedEvent extends MediaPlayerTrackListEvent {

    public MediaPlayerTrackRemovedEvent(MediaPlayer mediaPlayer, TrackType trackType, String trackId) {
        super(mediaPlayer, trackType, trackId);
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.trackRemoved(mediaPlayer, trackType, trackId);
    }
}
