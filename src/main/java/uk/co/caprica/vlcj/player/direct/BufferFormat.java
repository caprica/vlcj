package uk.co.caprica.vlcj.player.direct;

import java.util.Arrays;

/**
 * Specifies the format of the buffers used by the DirectMediaPlayer.<p>
 * The buffer will contain data of the given width and height in the format
 * specified by the chroma parameter.  A buffer can consist of multiple planes depending
 * on the format of the data.  For each plane the pitch and height in lines must be
 * supplied.<p>
 * For example, RV32 format has only one plane.  Its pitch is width * 4, and its number of lines
 * is the same as the height of the buffer.<p>
 */
public class BufferFormat {
    private final String chroma;
    private final int width;
    private final int height;
    private final int[] pitches;
    private final int[] lines;

    /**
     * Constructs a new BufferFormat instance with the given parameters.
     *
     * @param chroma a VLC buffer type, must be exactly 4 characters and cannot contain non-ASCII characters
     * @param width the width of the buffer, must be > 0
     * @param height the height of the buffer, must be > 0
     * @param pitches the pitch of each plane that this buffer consists of (usually a multiple of width)
     * @param lines the number of lines of each plane that this buffer consists of (usually same as height)
     */
    public BufferFormat(String chroma, int width, int height, int[] pitches, int[] lines) {
        if(chroma == null || chroma.length() != 4) {
            throw new IllegalArgumentException("Parameter 'chroma' cannot be null and must be exactly 4 characters: " + chroma);
        }
        if(pitches == null || pitches.length == 0) {
            throw new IllegalArgumentException("Parameter 'pitches' cannot be null or zero-length");
        }
        if(lines == null || lines.length == 0) {
            throw new IllegalArgumentException("Parameter 'lines' cannot be null or zero-length");
        }
        if(width <= 0) {
            throw new IllegalArgumentException("Parameter 'width' cannot be 0 or negative: " + width);
        }
        if(height <= 0) {
            throw new IllegalArgumentException("Parameter 'height' cannot be 0 or negative: " + height);
        }
        if(pitches.length != lines.length) {
            throw new IllegalArgumentException("Parameter 'pitches' and 'lines' must be of same length");
        }

        for(int i = 0; i < pitches.length; i++) {
            if(pitches[i] <= 0) {
                throw new IllegalArgumentException("Parameter 'pitches' cannot elements that are 0 or negative: " + Arrays.toString(pitches));
            }
            if(lines[i] <= 0) {
                throw new IllegalArgumentException("Parameter 'lines' cannot elements that are 0 or negative: " + Arrays.toString(lines));
            }
        }

        this.chroma = chroma;
        this.width = width;
        this.height = height;
        this.pitches = pitches;
        this.lines = lines;
    }

    /**
     * Returns the pixel format of the buffer.
     *
     * @return the pixel format of the buffer
     */
    public String getChroma() {
        return chroma;
    }

    /**
     * Returns the width of the buffer.
     *
     * @return the width of the buffer
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the buffer.
     *
     * @return the height of the buffer
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the pitch of each plane of the buffer.
     *
     * @return the pitch of each plane of the buffer
     */
    public int[] getPitches() {
        return pitches;
    }

    /**
     * Returns the number of lines of each plane of the buffer.
     *
     * @return the number of lines of each plane of the buffer
     */
    public int[] getLines() {
        return lines;
    }

    /**
     * Returns the number of planes in the buffer.
     *
     * @return the number of planes in the buffer
     */
    public int getPlaneCount() {
        return pitches.length;
    }
}
