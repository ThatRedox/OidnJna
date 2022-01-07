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

package io.github.ThatRedox.OidnJna.internal;

import com.sun.jna.*;
import com.sun.jna.ptr.PointerByReference;

/**
 * These are the raw JNA bindings to the underlying Intel Open Image Denoise library. For general use, please use the
 * wrapper classes instead.
 */
public class OidnJna {
    public static final int OIDN_ACCESS_READ = 0;
    public static final int OIDN_ACCESS_WRITE = 1;
    public static final int OIDN_ACCESS_READ_WRITE = 2;
    public static final int OIDN_ACCESS_WRITE_DISCARD = 3;

    public static final size_t ZERO = new size_t();

    public static class size_t extends IntegerType {
        public size_t(long value) {
            super(Native.SIZE_T_SIZE, value, true);
        }

        public size_t() {
            this(0);
        }
    }

    public interface OIDNProgressMonitorFunction extends Callback {
        boolean callback(Pointer userPtr, double n);
    }

    public interface OIDNErrorFunction extends Callback {
        void callback(Pointer userPtr, int code, String message);
    }

    public interface Oidn extends Library {
        Pointer oidnNewDevice(int type);
        void oidnReleaseDevice(Pointer device);
        void oidnRetainDevice(Pointer device);
        void oidnSetDevice1b(Pointer device, String name, boolean value);
        void oidnSetDevice1i(Pointer device, String name, int value);
        boolean oidnGetDevice1b(Pointer device, String name);
        int oidnGetDevice1i(Pointer device, String name);
        void oidnCommitDevice(Pointer device);

        int oidnGetDeviceError(Pointer device, PointerByReference outMessage);
        void oidnSetDeviceErrorFunction(Pointer device, OIDNErrorFunction func, Pointer userPtr);

        Pointer oidnNewBuffer(Pointer device, size_t byteSize);
        void oidnRetainBuffer(Pointer buffer);
        void oidnReleaseBuffer(Pointer buffer);
        Pointer oidnNewSharedBuffer(Pointer device, Pointer memory, size_t byteSize);
        Pointer oidnMapBuffer(Pointer buffer, int access, size_t byteoffset, size_t byteSize);
        void oidnUnmapBuffer(Pointer buffer, Pointer mappedPtr);

        Pointer oidnNewFilter(Pointer device, String type);
        void oidnSetFilterImage(Pointer filter, String name, Pointer buffer, int format,
                                size_t width, size_t height, size_t byteOffset,
                                size_t bytePixelStride, size_t byteRowStride);
        void oidnSetSharedFilterImage(Pointer filter, String name, Pointer ptr, int format,
                                      size_t width, size_t height, size_t byteOffset,
                                      size_t bytePixelStride, size_t byteRowStride);
        void oidnRemoveFilterimage(Pointer filter, String name);
        void oidnSetSharedFilterData(Pointer filter, String name, Pointer ptr, size_t byteSize);
        void oidnUpdateFilterData(Pointer filter, String name);
        void oidnRemoveFilterData(Pointer filter, String name);
        void oidnSetFilter1b(Pointer filter, String name, boolean value);
        void oidnSetFilter1i(Pointer filter, String name, int value);
        void oidnSetFilter1f(Pointer filter, String name, float value);
        boolean oidnGetFilter1b(Pointer filter, String name);
        int oidnGetFilter1i(Pointer filter, String name);
        float oidnGetFilter1f(Pointer filter, String name);
        void oidnSetFilterProgressMonitorFunction(Pointer filter, OIDNProgressMonitorFunction func,
                                                  Pointer userPtr);
        void oidnCommitFilter(Pointer filter);
        void oidnExecuteFilter(Pointer filter);
        void oidnRetainFilter(Pointer filter);
        void oidnReleaseFilter(Pointer filter);
    }
}
