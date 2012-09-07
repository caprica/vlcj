package uk.co.caprica.vlcj.player.direct;

/**
 * Convience class to create the RV32 buffer format.
 */
public class RV32BufferFormat extends BufferFormat {

    /**
     * Creates a RV32 BufferFormat with the given width and height.
     *
     * @param width width of the buffer
     * @param height height of the buffer
     */
    public RV32BufferFormat(int width, int height) {
        super("RV32", width, height, new int[] {width * 4}, new int[] {height});
    }
}
