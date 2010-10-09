package uk.co.caprica.vlcj.test.list;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.list.MediaList;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;

public class TestMediaListPlayer {

  public static void main(String[] args) throws Exception {
    String[] vlcArgs = {};
    
    MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(vlcArgs);
    
    MediaListPlayer mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();
    
    mediaListPlayer.addMediaListPlayerEventListener(new MediaListPlayerEventListener() {
      @Override
      public void nextItem(MediaListPlayer mediaListPlayer) {
        System.out.println("nextItem()");
      }
    });
    
    MediaList mediaList = mediaPlayerFactory.newMediaList();
    mediaList.addMedia("/intro.mp4");
    mediaList.addMedia("/home/movie/1.mp4");
    
    mediaListPlayer.setMediaList(mediaList);

    mediaListPlayer.play();
    
//    Thread.sleep(5000);
    Thread.currentThread().join();
    
    mediaList.release();
    
    mediaListPlayer.release();
    
    mediaPlayerFactory.release();
  }
  
}
