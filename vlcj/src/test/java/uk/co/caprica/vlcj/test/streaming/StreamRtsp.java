package uk.co.caprica.vlcj.test.streaming;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

/**
 * An example of how to stream a media file using RTSP.
 * <p>
 * The client specifies an MRL of <code>rtsp://@127.0.0.1:5555/demo</code>
 */
public class StreamRtsp {

  public static void main(String[] args) throws Exception {
    String media = "/home/movies/test.mp4";
    String options = formatRtspStream("127.0.0.1", 5555, "demo");

    System.out.println("Streaming '" + media + "' to '" + options + "'");
    
    MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(args);
    HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newMediaPlayer();
    mediaPlayer.playMedia(media,
      options,
      ":no-sout-rtp-sap", 
      ":no-sout-standard-sap", 
      ":sout-all", 
      ":sout-keep"
    );

    // Don't exit
    Thread.currentThread().join();
  }
  
  private static String formatRtspStream(String serverAddress, int serverPort, String id) {
    StringBuilder sb = new StringBuilder(60);
    sb.append(":sout=#rtp{sdp=rtsp://@");
    sb.append(serverAddress);
    sb.append(':');
    sb.append(serverPort);
    sb.append('/');
    sb.append(id);
    sb.append("}");
    return sb.toString();
  }
}
