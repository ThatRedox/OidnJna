package io.github.ThatRedox.OidnJna;

import com.sun.jna.Pointer;
import io.github.ThatRedox.OidnJna.internal.Cleaner;
import io.github.ThatRedox.OidnJna.internal.OidnJna;

public class Buffer implements AutoCloseable {
    private final Cleaner.Cleanable cleanable;
    private final OidnJna.Oidn lib;
    protected final Pointer ptr;

    private final long bufferSize;

    protected Buffer(OidnJna.Oidn lib, Device device, long bufferSize) {
        this.lib = lib;
        this.ptr = this.lib.oidnNewBuffer(device.ptr, new OidnJna.size_t(bufferSize));
        this.bufferSize = bufferSize;

        OidnJna.Oidn cleanerLib = this.lib;
        Pointer cleanerPtr = this.ptr;
        cleanable = Cleaner.CLEANER.create(this, () ->
                cleanerLib.oidnReleaseBuffer(cleanerPtr));
    }

    /**
     * Overwrite the entire buffer with the contents of a float array.
     */
    public void writeBuffer(float[] contents) {
        Pointer mappedBuffer = lib.oidnMapBuffer(ptr, OidnJna.OIDN_ACCESS_WRITE_DISCARD,
                OidnJna.ZERO, new OidnJna.size_t(4L * contents.length));
        mappedBuffer.write(0, contents, 0, contents.length);
        lib.oidnUnmapBuffer(ptr, mappedBuffer);
    }

    /**
     * Read the entire buffer to a float array.
     */
    public float[] readBuffer() {
        float[] output = new float[(int) (this.bufferSize / 4)];
        Pointer mappedBuffer = lib.oidnMapBuffer(ptr, OidnJna.OIDN_ACCESS_READ,
                OidnJna.ZERO, new OidnJna.size_t(this.bufferSize));
        mappedBuffer.read(0, output, 0, output.length);
        lib.oidnUnmapBuffer(ptr, mappedBuffer);
        return output;
    }

    @Override
    public void close() {
        cleanable.clean();
    }
}
