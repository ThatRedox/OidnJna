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
import io.github.ThatRedox.OidnJna.internal.Cleaner;
import io.github.ThatRedox.OidnJna.internal.OidnJna;

public class Device implements AutoCloseable {
    private final Cleaner.Cleanable cleanable;
    private final OidnJna.Oidn lib;
    protected final Pointer ptr;

    private boolean committed = false;

    protected Device(OpenImageDenoise lib, DeviceType deviceType) {
        this.lib = lib.lib;
        this.ptr = this.lib.oidnNewDevice(deviceType.value);

        OidnJna.Oidn cleanerLib = this.lib;
        Pointer cleanerPtr = this.ptr;
        cleanable = Cleaner.CLEANER.create(this, () ->
                cleanerLib.oidnReleaseDevice(cleanerPtr));
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
     */
    public int versionMajor() {
        return lib.oidnGetDevice1i(ptr, "versionMajor");
    }

    /**
     * Minor version number.
     */
    public int versionMinor() {
        return lib.oidnGetDevice1i(ptr, "versionMinor");
    }

    /**
     * Patch version number.
     */
    public int versionPatch() {
        return lib.oidnGetDevice1i(ptr, "versionPatch");
    }

    /**
     * Get verbosity level. 0-4
     */
    public int getVerbose() {
        return lib.oidnGetDevice1i(ptr, "verbose");
    }

    /**
     * Set verbosity level. 0-4.
     */
    public void setVerbose(int verbose) {
        if (0 <= verbose && verbose <= 4) {
            lib.oidnSetDevice1i(ptr, "verbose", verbose);
        } else {
            throw new OidnException("Verbosity level must be between 0 and 4 (%d given)", verbose);
        }
    }

    /**
     * Commit settings. This can only be done once per device. Committing more than once
     * will throw an OidnException.
     */
    public void commit() {
        if (!committed) {
            lib.oidnCommitDevice(ptr);
            committed = true;
        } else {
            throw new OidnException("Device can only be committed once.");
        }
    }

    /**
     * Get a buffer that can be used with Open Image Denoise.
     *
     * @param floatCapacity Capacity of the buffer in floats
     */
    public Buffer createBuffer(int floatCapacity) {
        return new Buffer(lib, this, 4L * floatCapacity);
    }

    /**
     * Create an OIDN filter.
     *
     * @param type Filter type.
     */
    public Filter createFilter(String type) {
        return new Filter(lib, this, type);
    }

    @Override
    public void close() {
        cleanable.clean();
    }
}
