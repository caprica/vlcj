package uk.co.caprica.vlcj.player.direct;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Factory for creating property aligned native byte buffers.
 */
public class ByteBufferFactory {

    /**
     * Alignment suitable for use by LibVLC video callbacks.
     */
    private static final int LIBVLC_ALIGNMENT = 32;

    /**
     * Allocate a properly aligned native byte buffer, suitable for use by the LibVLC video
     * callbacks.
     *
     * @param capacity size of the buffer
     * @return aligned byte buffer
     */
    public static ByteBuffer allocateAlignedBuffer(int capacity) {
        return allocateAlignedBuffer(capacity, LIBVLC_ALIGNMENT);
    }

    /**
     * Allocate a property aligned native byte buffer.
     * <p></p>
     * Original credit: http://psy-lob-saw.blogspot.co.uk/2013/01/direct-memory-alignment-in-java.html
     *
     * @param capacity size of the buffer
     * @param alignment alignment
     * @return aligned byte buffer
     */
    public static ByteBuffer allocateAlignedBuffer(int capacity, int alignment) {
        ByteBuffer result;
        ByteBuffer buffer = ByteBuffer.allocateDirect(capacity + alignment);
        long address = ((DirectBuffer) buffer).address();
        if ((address & (alignment - 1)) == 0) {
            buffer.limit(capacity);
            result = buffer.slice().order(ByteOrder.nativeOrder());
        }
        else {
            int newPosition = (int) (alignment - (address & (alignment - 1)));
            buffer.position(newPosition);
            buffer.limit(newPosition + capacity);
            result = buffer.slice().order(ByteOrder.nativeOrder());
        }
        return result;
    }
}
