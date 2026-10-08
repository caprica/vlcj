package uk.co.caprica.vlcj.player.embedded.videosurface;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.Buffer;

final class NewByteBufferFactory {

    private static final VarHandle ADDRESS_HANDLE;

    private static final int LIBVLC_ALIGNMENT = 32;

    static {
        try {
            Field addressField = Buffer.class.getDeclaredField("address");
            addressField.setAccessible(true);
            ADDRESS_HANDLE = MethodHandles.privateLookupIn(Buffer.class, MethodHandles.lookup()).unreflectVarHandle(addressField);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private NewByteBufferFactory() { }

    static ByteBuffer allocateAlignedBuffer(int capacity) {
        return allocateAlignedBuffer(capacity, LIBVLC_ALIGNMENT);
    }

    static long getAddress(ByteBuffer buffer) {
        return (long) ADDRESS_HANDLE.get(buffer);
    }

    private static ByteBuffer allocateAlignedBuffer(int capacity, int alignment) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(capacity + alignment);
        long address = getAddress(buffer);

        int offset = (int) ((alignment - (address & (alignment - 1))) & (alignment - 1));
        ((Buffer) buffer).position(offset).limit(offset + capacity);

        return buffer.slice().order(ByteOrder.nativeOrder());
    }
}
