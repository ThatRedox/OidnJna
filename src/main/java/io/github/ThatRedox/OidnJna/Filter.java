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

import io.github.ThatRedox.OidnJna.internal.OidnJna;

/**
 * An OIDN filter.
 */
public class Filter extends OidnObject {
    private final Device device;

    protected Filter(OidnJna.Oidn lib, Device device, String type) {
        super(lib, lib.oidnNewFilter(device.ptr, type),
                lib::oidnReleaseFilter);
        this.device = device;
    }

    /**
     * Set an image in the filter.
     * @param name   Name of the image to set
     * @param buffer Buffer of the image to set
     * @param width  Width of the image
     * @param height Height of the image
     */
    public void setFilterImage(String name, Buffer buffer, long width, long height) {
        try (Device.ExceptionCatch e = device.catchException()) {
            lib.oidnSetFilterImage(ptr, name, buffer.ptr, 3,
                    new OidnJna.size_t(width), new OidnJna.size_t(height),
                    OidnJna.ZERO, OidnJna.ZERO, OidnJna.ZERO);
        }
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
        try (Device.ExceptionCatch e = device.catchException()) {
            lib.oidnSetFilter1b(ptr, name, value);
        }
    }

    /**
     * Set a filter parameter. See https://www.openimagedenoise.org/documentation.html for a full list of parameters.
     * @param name  Filter parameter name
     * @param value Desired value
     */
    public void setFilterParam(String name, int value) {
        try (Device.ExceptionCatch e = device.catchException()) {
            lib.oidnSetFilter1i(ptr, name, value);
        }
    }

    /**
     * Set a filter parameter. See https://www.openimagedenoise.org/documentation.html for a full list of parameters.
     * @param name  Filter parameter name
     * @param value Desired value
     */
    public void setFilterParam(String name, float value) {
        try (Device.ExceptionCatch e = device.catchException()) {
            lib.oidnSetFilter1f(ptr, name, value);
        }
    }

    /**
     * Get the value of a boolean filter parameter. See https://www.openimagedenoise.org/documentation.html
     * for a full list of parameters.
     * @param name Filter parameter name
     * @return     Boolean value of the filter parameter.
     */
    public boolean getFilterValueB(String name) {
        try (Device.ExceptionCatch e = device.catchException()) {
            return lib.oidnGetFilter1b(ptr, name);
        }
    }

    /**
     * Get the value of an integer filter parameter. See https://www.openimagedenoise.org/documentation.html
     * for a full list of parameters.
     * @param name Filter parameter name
     * @return     Integer value of the filter parameter.
     */
    public int getFilterValueI(String name) {
        return lib.oidnGetFilter1i(ptr, name);
    }

    /**
     * Get the value of a float filter parameter. See https://www.openimagedenoise.org/documentation.html
     * for a full list of parameters.
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
        try (Device.ExceptionCatch e = device.catchException()) {
            lib.oidnCommitFilter(ptr);
        }
    }

    /**
     * Execute the filter and denoise the image.
     */
    public void execute() {
        try (Device.ExceptionCatch e = device.catchException()) {
            lib.oidnExecuteFilter(ptr);
        }
    }
}
