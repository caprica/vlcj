package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.TrackInfo;
import uk.co.caprica.vlcj.player.TrackType;

import java.util.List;

public final class InfoService extends BaseService {

    InfoService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the media details.
     * <p>
     * The details are available after the video has started playing, regardless of whether nor not
     * a video output has been created.
     *
     * @return video meta data, or <code>null</code> if the media meta data is not available
     */
    public MediaDetails getMediaDetails() {
        // The media must be playing to get this meta data...
        if (mediaPlayer.status().isPlaying()) { // FIXME not sure i should be checking this in here
            MediaDetails mediaDetails = new MediaDetails();
            mediaDetails.setTitleCount(mediaPlayer.titles().getTitleCount());
            mediaDetails.setVideoTrackCount(mediaPlayer.video().getVideoTrackCount());
            mediaDetails.setAudioTrackCount(mediaPlayer.audio().getAudioTrackCount());
            mediaDetails.setSpuCount(mediaPlayer.subpictures().getSpuCount());
            mediaDetails.setTitleDescriptions(mediaPlayer.titles().getTitleDescriptions());
            mediaDetails.setVideoDescriptions(mediaPlayer.video().getVideoDescriptions());
            mediaDetails.setAudioDescriptions(mediaPlayer.audio().getAudioDescriptions());
            mediaDetails.setSpuDescriptions(mediaPlayer.subpictures().getSpuDescriptions());
            mediaDetails.setChapterDescriptions(mediaPlayer.chapters().getAllChapterDescriptions());
            return mediaDetails;
        }
        else {
            return null;
        }
    }

    /**
     * Get the track (i.e. "elementary streams") information for the current media.
     * <p>
     * The media (if local) should first be parsed, see {@link #parseMedia()}, or be already
     * playing.
     * <p>
     * In the case of DVD media (for example ".iso" files) and streams the media must be played and
     * video output must be available before valid track information becomes available, and even
     * then it is not always available immediately (or it is only partially available) so polling
     * may be required.
     * <p>
     * If you invoke this method "too soon", you may only receive partial track information.
     *
     * @param types zero or more types of track to get, or <em>all</em> tracks if omitted
     * @return collection of track information, or <code>null</code> if there is no current media
     */
    public List<TrackInfo> getTrackInfo(TrackType... types) {
        return TrackInformation.getTrackInfo(libvlc, media(), types);
    }

    private libvlc_media_t media() {
        return mediaPlayer.media().media();
    }

}
