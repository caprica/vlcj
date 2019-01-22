package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.enums.MediaSlaveType;
import uk.co.caprica.vlcj.model.TrackDescription;

import java.io.File;
import java.util.List;

public final class SubpictureService extends BaseService {

    SubpictureService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the number of sub-pictures/sub-title tracks.
     *
     * @return number of sub-title tracks
     */
    public int getSpuCount() {
        return libvlc.libvlc_video_get_spu_count(mediaPlayerInstance);
    }

    /**
     * Get the current sub-title track.
     *
     * @return sub-title number, or -1 if none
     */
    public int getSpu() {
        return libvlc.libvlc_video_get_spu(mediaPlayerInstance);
    }

    /**
     * Set the current sub-title track.
     * <p>
     * The track identifier must be one of those returned by {@link #getSpuDescriptions()}.
     * <p>
     * Subtitles can be disabled by passing here the identifier of the track with a description of
     * "Disable".
     * <p>
     * There is no guarantee that the available subtitle identifiers go in sequence from zero up to
     * {@link #getSpuCount()}-1. The {@link #getSpuDescriptions()} method should always
     * be used to ascertain the available subtitle identifiers.
     * <p>
     * The implementation of the corresponding <em>native</em> method in libvlc is bugged before
     * vlc 2.0.6, therefore vlc 2.0.6 or later is required for correct behaviour when using this
     * method.
     *
     * @param spu sub-title identifier, or -1 for none
     * @return current sub-title identifier
     */
    public int setSpu(int spu) {
        libvlc.libvlc_video_set_spu(mediaPlayerInstance, spu);
        return getSpu(); // FIXME does this work or is it actually async?
    }

    /**
     * Get the sub-title delay.
     *
     * @return sub-title delay, in microseconds
     */
    public long getSpuDelay() {
        return libvlc.libvlc_video_get_spu_delay(mediaPlayerInstance);
    }

    /**
     * Set the sub-title delay.
     * <p>
     * The sub-title delay is set for the current item only and will be reset to zero each time the
     * media changes.
     *
     * @param delay desired sub-title delay, in microseconds
     */
    public void setSpuDelay(long delay) {
        libvlc.libvlc_video_set_spu_delay(mediaPlayerInstance, delay);
    }

    // FIXME for now we will leave this as-is, but either we change this to support addSlave (like http: etc) or we keep it just for local files and always prepend "file:///" with Java7 we could use String uri = File.toPath().toUri().toString();
    /**
     * Set the sub-title file to use.
     *
     * @param subTitleFileName name of the file containing the sub-titles
     */
    public void setSubTitleFile(String subTitleFileName) {
        mediaPlayer.slave().addSlave(MediaSlaveType.SUBTITLE, subTitleFileName, true);
    }

    /**
     * Set the sub-title file to use.
     *
     * @param subTitleFile file containing the sub-titles
     */
    public void setSubTitleFile(File subTitleFile) {
        setSubTitleFile(String.format("file://%s", subTitleFile.getAbsolutePath()));
    }

    /**
     * Get the sub-title track descriptions.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return list of descriptions, may be empty but will never be <code>null</code>
     */
    public List<TrackDescription> getSpuDescriptions() {
        return Descriptions.spuTrackDescriptions(libvlc, mediaPlayerInstance);
    }

}
