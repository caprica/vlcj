package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.binding.NativeString;
import uk.co.caprica.vlcj.enums.State;
import uk.co.caprica.vlcj.enums.TrackType;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t;
import uk.co.caprica.vlcj.enums.MediaType;
import uk.co.caprica.vlcj.player.*;

import java.util.List;

public class InfoService extends BaseService {

    private final libvlc_media_stats_t statsInstance = new libvlc_media_stats_t();

    InfoService(Media media) {
        super(media);
    }

    public String mrl() {
        return NativeString.copyAndFreeNativeString(libvlc, libvlc.libvlc_media_get_mrl(mediaInstance));
    }

    public MediaType type() {
        return MediaType.mediaType(libvlc.libvlc_media_get_type(mediaInstance));
    }

    public State state() {
        return State.state(libvlc.libvlc_media_get_state(mediaInstance));
    }

    public long duration() {
        return libvlc.libvlc_media_get_duration(mediaInstance);
    }

    public List<? extends TrackInfo> tracks(TrackType... types) {
        return TrackInformation.getTrackInfo(libvlc, mediaInstance, types);
    }

    @SuppressWarnings("unchecked")
    public List<AudioTrackInfo> audioTracks() {
        return (List<AudioTrackInfo>) tracks(TrackType.AUDIO);
    }

    @SuppressWarnings("unchecked")
    public List<VideoTrackInfo> videoTracks() {
        return (List<VideoTrackInfo>) tracks(TrackType.VIDEO);
    }

    @SuppressWarnings("unchecked")
    public List<TextTrackInfo> textTracks() {
        return (List<TextTrackInfo>) tracks(TrackType.TEXT);
    }

    public boolean statistics(MediaStatistics mediaStatistics) {
        if (libvlc.libvlc_media_get_stats(mediaInstance, statsInstance) != 0) {
            mediaStatistics.apply(statsInstance);
            return true;
        } else {
            return false;
        }
    }

}
