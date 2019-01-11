package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.MediaStatistics;
import uk.co.caprica.vlcj.player.TrackInfo;
import uk.co.caprica.vlcj.player.TrackType;

import java.util.List;

// FIXME rename to simple get?

public final class StatsService extends BaseService {

    private libvlc_media_stats_t nativeStats;

    StatsService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the current media statistics.
     * <p>
     * Statistics are only updated if the video is playing.
     *
     * @return media statistics
     */
    public MediaStatistics getMediaStatistics(MediaStatistics stats) {
        if (mediaPlayer.status().isPlaying()) {
            if (libvlc.libvlc_media_get_stats(mediaPlayer.media().media(), nativeStats) != 0) {
                stats.apply(nativeStats);
            }
        }
        return stats;
    }

    /**
     * Reset the cached statistics.
     * <p>
     * This should be invoked when a new media instance has been successfully set on the media player.
     */
    void reset() {
        nativeStats = new libvlc_media_stats_t();
    }

}
