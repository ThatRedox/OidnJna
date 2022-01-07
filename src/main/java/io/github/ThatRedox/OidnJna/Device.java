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

import com.sun.jna.ptr.PointerByReference;
import io.github.ThatRedox.OidnJna.internal.OidnJna;

/**
 * An OIDN device.
 */
public class Device extends OidnObject {
    public class ExceptionCatch implements AutoCloseable {
        private final PointerByReference ref = new PointerByReference();

        protected ExceptionCatch() {
            // Clear exception
            lib.oidnGetDeviceError(ptr, ref);
        }

        @Override
        public void close() throws OidnException {
            int code = lib.oidnGetDeviceError(ptr, ref);
            if (code != 0) {
                throw OidnException.fromCode(code, ref.getPointer().getString(0));
            }
        }
    }

    protected Device(OidnJna.Oidn lib, DeviceType deviceType) {
        super(lib, lib.oidnNewDevice(deviceType.value), lib::oidnReleaseDevice);
    }

    public ExceptionCatch catchException() {
        return new ExceptionCatch();
    }

    /**
     * Combined version number with 2 decimal digits per component.
     *
     * @return major.minor.patch
     */
    public int versionNum() {
        return lib.oidnGetDevice1i(ptr, "version");
    }

    /**
     * Major version number.
     * @return major version number.
     */
    public int versionMajor() {
        return lib.oidnGetDevice1i(ptr, "versionMajor");
    }

    /**
     * Minor version number.
     * @return minor version number.
     */
    public int versionMinor() {
        return lib.oidnGetDevice1i(ptr, "versionMinor");
    }

    /**
     * Patch version number.
     * @return patch version number
     */
    public int versionPatch() {
        return lib.oidnGetDevice1i(ptr, "versionPatch");
    }

    /**
     * Get verbosity level. 0-4
     * @return verbosity level, 0-4
     */
    public int getVerbose() {
        return lib.oidnGetDevice1i(ptr, "verbose");
    }

    /**
     * Set verbosity level. 0-4.
     * @param verbose Verbosity level, 0-4.
     */
    public void setVerbose(int verbose) throws OidnException {
        try (ExceptionCatch e = catchException()) {
            lib.oidnSetDevice1i(ptr, "verbose", verbose);
        }
    }

    /**
     * Commit settings. This can only be done once per device.
     */
    public void commit() throws OidnException {
        try (ExceptionCatch e = new ExceptionCatch()) {
            lib.oidnCommitDevice(ptr);
        }
    }

    /**
     * Get a buffer that can be used with Open Image Denoise.
     *
     * @param floatCapacity Capacity of the buffer in floats
     * @return OIDN buffer.
     */
    public Buffer createBuffer(int floatCapacity) throws OidnException {
        try (ExceptionCatch e = catchException()) {
            return new Buffer(lib, this, 4L * floatCapacity);
        }
    }

    /**
     * Create an OIDN filter.
     *
     * @param type Filter type.
     * @return OIDN filter.
     */
    public Filter createFilter(String type) throws OidnException {
        try (ExceptionCatch e = catchException()) {
            return new Filter(lib, this, type);
        }
    }
}
