/* Copyright 2022 ThatRedox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ThatRedox.OidnJna;

import com.sun.jna.Pointer;
import io.github.ThatRedox.OidnJna.internal.OidnJna;

/**
 * An OIDN image buffer.
 */
public class Buffer extends OidnObject {
    private final long bufferSize;
    private final Device device;

    protected Buffer(OidnJna.Oidn lib, Device device, long bufferSize) {
        super(lib, lib.oidnNewBuffer(device.ptr, new OidnJna.size_t(bufferSize)),
                lib::oidnReleaseBuffer);
        this.bufferSize = bufferSize;
        this.device = device;
    }

    /**
     * Overwrite the entire buffer with the contents of a float array.
     * @param contents Contents to set the buffer to
     */
    public void writeBuffer(float[] contents) {
        try (Device.ExceptionCatch e = device.catchException()) {
            Pointer mappedBuffer = lib.oidnMapBuffer(ptr, OidnJna.OIDN_ACCESS_WRITE_DISCARD,
                    OidnJna.ZERO, new OidnJna.size_t(4L * contents.length));
            mappedBuffer.write(0, contents, 0, contents.length);
            lib.oidnUnmapBuffer(ptr, mappedBuffer);
        }
    }

    /**
     * Read the entire buffer to a float array.
     * @return the contents of the buffer
     */
    public float[] readBuffer() {
        try (Device.ExceptionCatch e = device.catchException()) {
            float[] output = new float[(int) (this.bufferSize / 4)];
            Pointer mappedBuffer = lib.oidnMapBuffer(ptr, OidnJna.OIDN_ACCESS_READ,
                    OidnJna.ZERO, new OidnJna.size_t(this.bufferSize));
            mappedBuffer.read(0, output, 0, output.length);
            lib.oidnUnmapBuffer(ptr, mappedBuffer);
            return output;
        }
    }
}
