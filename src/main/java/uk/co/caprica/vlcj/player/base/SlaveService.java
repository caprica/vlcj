package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.enums.MediaSlaveType;

// FIXME rename to simple add?

public final class SlaveService extends BaseService {

    SlaveService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Add an input slave to the current media.
     * <p>
     * The success of this call does not indicate that the slave being added is actually valid or not, it simply
     * associates a slave URI with the current media player (for example, a sub-title file will not be parsed and
     * checked for validity during this call).
     * <p>
     * If the URI represents a local file, it <em>must</em> be of the form "file://" otherwise it will not work, so this
     * will work:
     * <pre>
     *     file:///home/movies/movie.srt
     * </pre>
     * This will not work:
     * <pre>
     *     file:/home/movies/movie.srt
     * </pre>
     *
     * @param type type of slave to add
     * @param uri URI of the slave to add
     * @param select <code>true</code> if this slave should be automatically selected when added
     * @return <code>true</code> on success; <code>false</code> otherwise
     */
    public boolean addSlave(MediaSlaveType type, String uri, boolean select) {
        return libvlc.libvlc_media_player_add_slave(mediaPlayerInstance, type.intValue(), uri, select ? 1 : 0) == 0;
    }

}
