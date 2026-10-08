package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

abstract class MediaPlayerTrackListEvent extends MediaPlayerEvent {

    protected final TrackType trackType;

    protected final String trackId;

    protected MediaPlayerTrackListEvent(MediaPlayer mediaPlayer, TrackType trackType, String trackId) {
        super(mediaPlayer);
        this.trackType = trackType;
        this.trackId = trackId;
    }
}
