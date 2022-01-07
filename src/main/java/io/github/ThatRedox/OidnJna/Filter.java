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

/**
 * An OIDN filter.
 */
public class Filter implements AutoCloseable {
    private final Cleaner.Cleanable cleanable;
    private final OidnJna.Oidn lib;
    protected final Pointer ptr;

    protected Filter(OidnJna.Oidn lib, Device device, String type) {
        this.lib = lib;
        this.ptr = this.lib.oidnNewFilter(device.ptr, type);

        OidnJna.Oidn cleanerLib = this.lib;
        Pointer cleanerPtr = this.ptr;
        cleanable = Cleaner.CLEANER.create(this, () ->
                cleanerLib.oidnReleaseFilter(cleanerPtr));
    }

    /**
     * Set an image in the filter.
     * @param name   Name of the image to set
     * @param buffer Buffer of the image to set
     * @param width  Width of the image
     * @param height Height of the image
     */
    public void setFilterImage(String name, Buffer buffer, long width, long height) {
        lib.oidnSetFilterImage(ptr, name, buffer.ptr, 3,
                new OidnJna.size_t(width), new OidnJna.size_t(height),
                OidnJna.ZERO, OidnJna.ZERO, OidnJna.ZERO);
    }

    /**
     * Remove an image in this filter.
     * @param name Name of the image to remove
     */
    public void removeFilterImage(String name) {
        lib.oidnRemoveFilterimage(ptr, name);
    }

    /**
     * Set a filter parameter.
     * @param name  Filter parameter name
     * @param value Desired value
     */
    public void setFilterParam(String name, boolean value) {
        lib.oidnSetFilter1b(ptr, name, value);
    }

    /**
     * Set a filter parameter.
     * @param name  Filter parameter name
     * @param value Desired value
     */
    public void setFilterParam(String name, int value) {
        lib.oidnSetFilter1i(ptr, name, value);
    }

    /**
     * Set a filter parameter.
     * @param name  Filter parameter name
     * @param value Desired value
     */
    public void setFilterParam(String name, float value) {
        lib.oidnSetFilter1f(ptr, name, value);
    }

    /**
     * Get the value of a boolean filter parameter.
     * @param name Filter parameter name
     * @return     Boolean value of the filter parameter.
     */
    public boolean getFilterValueB(String name) {
        return lib.oidnGetFilter1b(ptr, name);
    }

    /**
     * Get the value of an integer filter parameter.
     * @param name Filter parameter name
     * @return     Integer value of the filter parameter.
     */
    public int getFilterValueI(String name) {
        return lib.oidnGetFilter1i(ptr, name);
    }

    /**
     * Get the value of a float filter parameter.
     * @param name Filter parameter name
     * @return     Float value of the filter parameter.
     */
    public float getFilterValueF(String name) {
        return lib.oidnGetFilter1f(ptr, name);
    }

    /**
     * Commit the changes to the filter.
     */
    public void commit() {
        lib.oidnCommitFilter(ptr);
    }

    /**
     * Execute the filter and denoise the image.
     */
    public void execute() {
        lib.oidnExecuteFilter(ptr);
    }

    public void close() {
        cleanable.clean();
    }
}
