package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

public class MediaPlayerTrackSelectionChangedEvent extends MediaPlayerEvent {

    private final TrackType trackType;

    private final String unselectedTrackId;

    private final String selectedTrackId;

    public MediaPlayerTrackSelectionChangedEvent(MediaPlayer mediaPlayer, TrackType trackType, String unselectedTrackId, String selectedTrackId) {
        super(mediaPlayer);
        this.trackType = trackType;
        this.unselectedTrackId = unselectedTrackId;
        this.selectedTrackId = selectedTrackId;
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.trackSelectionChanged(mediaPlayer, trackType, unselectedTrackId, selectedTrackId);
    }
}
