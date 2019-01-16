package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_type_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.player.*;

import java.util.List;

public class InfoService extends BaseService {

    private final libvlc_media_stats_t statsInstance = new libvlc_media_stats_t();

    InfoService(Media media) {
        super(media);
    }

    public String mrl() {
        // The returned string must be freed // this has been checked for this method against VLC source, but i think i'd like to annotate the native method in LibVlc.java ? FIXME
        return NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_mrl(mediaInstance));
    }

    public libvlc_media_type_e type() {
        return libvlc_media_type_e.mediaType(libvlc.libvlc_media_get_type(mediaInstance));
    }

    public libvlc_state_t state() {
        return libvlc_state_t.state(libvlc.libvlc_media_get_state(mediaInstance));
    }

    public long duration() {
        return libvlc.libvlc_media_get_duration(mediaInstance);
    }

    public List<? extends TrackInfo> tracks(TrackType... types) {
        return TrackInformation.getTrackInfo(libvlc, mediaInstance, types);
    }

    public List<AudioTrackInfo> audioTracks() {
        return (List<AudioTrackInfo>) tracks(TrackType.AUDIO);
    }

    public List<VideoTrackInfo> videoTracks() {
        return (List<VideoTrackInfo>) tracks(TrackType.VIDEO);
    }

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
