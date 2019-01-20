package uk.co.caprica.vlcj.component;

/**
 * Consolidated VLC command-line options/switches/arguments used by default in the media player components when creating
 * a {@link uk.co.caprica.vlcj.factory.MediaPlayerFactory}.
 * <p>
 * <pre>
 * --intf=dummy
 *     This is the main interface used by VLC.
 *
 * --no-snapshot-preview
 *     Display the snapshot preview in the screen’s top-left corner.
 *
 * --quiet
 *     Turn off all messages on the console.
 *
 * --quiet-synchro
 *     This avoids flooding the message log with debug output from the video output synchronization mechanism.
 *
 * --video-title
 *     Custom title for the video window
 * </pre>
 */
final class MediaPlayerComponentDefaults {

    static String[] EMBEDDED_MEDIA_PLAYER_ARGS = {
        "--video-title=vlcj video output",
        "--no-snapshot-preview",
        "--quiet",
        "--intf=dummy"
    };

    static String[] AUDIO_MEDIA_PLAYER_ARGS = {
        "--quiet",
        "--intf=dummy"
    };

    static String[] DIRECT_MEDIA_PLAYER_ARGS = {
        "--no-snapshot-preview",
        "--quiet",
        "--intf=dummy"
    };

    static String[] DIRECT_AUDIO_PLAYER_ARGS = {
        "--quiet",
        "--intf=dummy"
    };

    private MediaPlayerComponentDefaults() {
    }

}
