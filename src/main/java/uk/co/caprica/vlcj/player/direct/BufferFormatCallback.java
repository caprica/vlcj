package uk.co.caprica.vlcj.player.direct;

/**
 * Gets called by DirectMediaPlayer when the format of the video changes.
 */
public interface BufferFormatCallback {

    /**
     * Returns a BufferFormat instance specifying how DirectMediaPlayer should structure its
     * buffers.
     * 
     * @param originalWidth the width of the video
     * @param originalHeight the height of the video
     * @return a BufferFormat instance
     */
    BufferFormat getBufferFormat(int originalWidth, int originalHeight);
}
